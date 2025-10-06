package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Session.GetSessionResponse;
import com.husyairi.ManaProgressAku.DTO.Session.InsertSessionRequest;
import com.husyairi.ManaProgressAku.DTO.Session.InsertSessionResponse;
import com.husyairi.ManaProgressAku.DTO.Session.UpdateSessionRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Session;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.ExerciseRepository;
import com.husyairi.ManaProgressAku.Repository.SessionRepository;
import com.husyairi.ManaProgressAku.Repository.UserRepository;
import com.husyairi.ManaProgressAku.Service.ExerciseService;
import com.husyairi.ManaProgressAku.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    private Long getCurrentUserId() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                "User not found"
        ));

        return currentUser.getId();
    }

    private String generateSessionID(){

        Session latestSession = sessionRepository.findTopByOrderBySessionIDDesc();
        // example returned data: Session{ sessionID = "SESS003", sessionName = "Evening Workout" }

        /*
        This works only if your session IDs are formatted consistently, like SESS001, SESS002, etc.
        If mess up the format (e.g., SESS5 vs SESS005), ordering will break.
         */

        String newSessionID;

        if(latestSession == null){
            newSessionID = "SESS001";
        }else{
            String lastID = latestSession.getSessionID(); // eg. "SESS005"
            int num = Integer.parseInt(lastID.substring(4)); // get 005
            newSessionID = String.format("SESS%03d", num + 1);
        }

        return newSessionID;
    }

    public InsertSessionResponse createSession(InsertSessionRequest request){

        Session newSession = new Session(
                request.getDate(),
                request.getTime()
        );

        // generate new Session ID
        newSession.setSessionID(generateSessionID());
        newSession.setUserId(getCurrentUserId());

        try {
            Session savedSession = sessionRepository.save(newSession);
            return new InsertSessionResponse( savedSession.getSessionID());
        } catch (Exception e) {
            throw new BadRequestException(400, "Error saving session: " + e.getMessage(), new HashMap<>());
        }

    }

    @Override
    public GetSessionResponse getSession (String sessionID) {

        Optional<Session> retrievedSession;
        try {
            retrievedSession = sessionRepository.findById(sessionID);
        } catch (Exception e) {
            throw new BadRequestException(500, e.getMessage() , new HashMap<>());
        }

        if (retrievedSession.isEmpty()) {
            throw new BadRequestException(404, "Session ID not found.", new HashMap<>());
        }

        Long sessionUserId = retrievedSession.get().getUserId();

        if(sessionUserId == null || !sessionUserId.equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not Authorized", new HashMap<>());
        }

        Session session = retrievedSession.get();

        return new GetSessionResponse(
                session.getSessionID(),
                session.getTime(),
                session.getDate()
        );
    }

    @Override
    public Session updateSession (UpdateSessionRequest request){

        Optional<Session> isExist = sessionRepository.findById(request.getSessionID());

        if(isExist.isEmpty()){
            throw new BadRequestException(404, "Session ID not found", new HashMap<>());
        }

        // TODO: make this checker a function
        Long sessionUserId = isExist.get().getUserId();

        if(sessionUserId == null || !sessionUserId.equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not Authorized", new HashMap<>());
        }

        Session updatedSession = isExist.get();
        updatedSession.setTime(request.getTime());
        updatedSession.setDate(request.getDate());

        try{
            sessionRepository.save(updatedSession);
        }catch (Exception e){
            throw new BadRequestException(500, "", new HashMap<>());
        }

        return updatedSession;
    }

    @Override
    public void deleteSession (String sessionID){

        Optional<Session> isExist = sessionRepository.findById(sessionID);

        if(isExist.isEmpty()){
            throw new BadRequestException(404, "Session ID not found", new HashMap<>());
        }

        Long sessionUserId = isExist.get().getUserId();


        if(sessionUserId == null || !sessionUserId.equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not Authorized", new HashMap<>());
        }

        try {
            sessionRepository.deleteById(sessionID);
        }catch (Exception e){
            throw new BadRequestException(500, e.getMessage() ,new HashMap<>());
        }
    }

    @Override
    public List<Session> getUserSessions(){
        return sessionRepository.findByUserId(getCurrentUserId());
    }

    @Override
    public List<Session> getAllSessions(){
        return sessionRepository.findAll();
    }
}
