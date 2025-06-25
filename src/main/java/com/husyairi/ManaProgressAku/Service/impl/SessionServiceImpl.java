package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.GetExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.GetSessionResponse;
import com.husyairi.ManaProgressAku.DTO.InsertSessionRequest;
import com.husyairi.ManaProgressAku.DTO.InsertSessionResponse;
import com.husyairi.ManaProgressAku.Entity.Model.Session;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.ExerciseRepository;
import com.husyairi.ManaProgressAku.Repository.SessionRepository;
import com.husyairi.ManaProgressAku.Service.ExerciseService;
import com.husyairi.ManaProgressAku.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private String generateSessionID(){
        /*
        TODO
        - currently we have numbering as session id to signify urutan
        - but do we need it? why not just generate random string
        - for the urutan, why not just use the date?
         */
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

        if(!exerciseRepository.existsById(request.getExerciseID())){
            throw new BadRequestException(404, "Exercise ID does not exist", new HashMap<>());
        }

        Session newSession = new Session(
                request.getExerciseID(),
                request.getDate(),
                request.getSets(),
                request.getWeight(),
                request.getRep()
        );

        // generate new Session ID
        newSession.setSessionID(generateSessionID());


        try {
            Session savedSession = sessionRepository.save(newSession);
            return new InsertSessionResponse("SUCCESS", "Session saved with ID: " + savedSession.getSessionID());
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

        Session session = retrievedSession.get();

        return new GetSessionResponse(
                session.getSessionID(),
                session.getExerciseID(),
                session.getDate(),
                session.getSets(),
                session.getWeight(),
                session.getRep()
        );
    }
}
