package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Session.*;
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
                request.getTime(),
                "ACTIVE"
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

        Session fetchedSession = sessionRepository.findById(sessionID).orElseThrow(() ->
                new BadRequestException(404, "No session found", new HashMap<>())
        );
        if(!fetchedSession.getUserId().equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not authorized to perform this operation", new HashMap<>());
        }
        return new GetSessionResponse(
                fetchedSession.getSessionID(),
                fetchedSession.getTime(),
                fetchedSession.getDate(),
                fetchedSession.getStatus()
        );
    }

    @Override
    public Session updateSession (UpdateSessionRequest request){

        Session fetchedSession = sessionRepository.findById(request.getSessionID()).orElseThrow(() ->
                new BadRequestException(404, "No session found", new HashMap<>()));

        if(!fetchedSession.getUserId().equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not authorized to perform this operation", new HashMap<>());
        }

        fetchedSession.setTime(request.getTime());
        fetchedSession.setDate(request.getDate());

        try{
            sessionRepository.save(fetchedSession);
        }catch (Exception e){
            throw new BadRequestException(500, "", new HashMap<>());
        }

        return fetchedSession;
    }

    @Override
    public void deleteSession (String sessionID){

        Session fetchedSession = sessionRepository.findById(sessionID).orElseThrow(()->
                new BadRequestException(404, "No session found", new HashMap<>()));

        if(!fetchedSession.getUserId().equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not authorize to perform this operation", new HashMap<>());
        }
        try {
            sessionRepository.deleteById(sessionID);
        }catch (Exception e){
            throw new BadRequestException(500, e.getMessage() ,new HashMap<>());
        }
    }

    @Override
    public Session finishSession(String sessionID){

        Session fetchedSession = sessionRepository.findById(sessionID).orElseThrow(() ->
                new BadRequestException(404, "No session found", new HashMap<>()));

        if(!fetchedSession.getUserId().equals(getCurrentUserId())){
            throw  new BadRequestException(403, "Not authorized to perform this operation", new HashMap<>());
        }

        fetchedSession.setStatus("COMPLETED");
        sessionRepository.save(fetchedSession);

        return fetchedSession;
    }

    @Override
    public List<Session> getUserSessions(){
        return sessionRepository.findByUserId(getCurrentUserId());
    }

    @Override
    public List<Session> getAllSessions(){
        return sessionRepository.findAll();
    }

    @Override
    public ActiveSessionResponse getActiveSession(){

        Optional<Session> fetchedSession = sessionRepository.findActiveSession(getCurrentUserId());

        if(fetchedSession.isEmpty()){
            return new ActiveSessionResponse(
                    false,
                    null,
                    null,
                    null
            );
        }else{
            Session session = fetchedSession.get();
            return new ActiveSessionResponse(
                    true,
                    session.getSessionID(),
                    session.getDate(),
                    session.getStatus()
            );
        }

    }
}
