package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.GetSessionResponse;
import com.husyairi.ManaProgressAku.DTO.InsertSessionRequest;
import com.husyairi.ManaProgressAku.DTO.InsertSessionResponse;
import com.husyairi.ManaProgressAku.DTO.UpdateSessionRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Session;

import java.util.List;

public interface SessionService {

    public InsertSessionResponse createSession(InsertSessionRequest request);

    public GetSessionResponse getSession(String sessionID);

    public Session updateSession(UpdateSessionRequest req);

    public void deleteSession(String sessionID);

    public List<Session> getAllSessions();
}
