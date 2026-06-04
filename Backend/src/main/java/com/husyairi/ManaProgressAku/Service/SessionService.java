package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.Session.*;
import com.husyairi.ManaProgressAku.DTO.Session.UpdateSessionRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Session;

import java.util.List;

public interface SessionService {

    public InsertSessionResponse createSession(InsertSessionRequest request);

    public GetSessionResponse getSession(String sessionID);

    public Session updateSession(UpdateSessionRequest req);

    public void deleteSession(String sessionID);

    public Session finishSession(String sessionID);

    public List<Session> getAllSessions();

    public List<Session> getUserSessions();

    public ActiveSessionResponse getActiveSession();

    public SessionDetailsResponse getSessionDetails(String sessionID);
}
