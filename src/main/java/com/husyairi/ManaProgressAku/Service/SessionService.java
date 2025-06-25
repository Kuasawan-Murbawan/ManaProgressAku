package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.GetSessionResponse;
import com.husyairi.ManaProgressAku.DTO.InsertSessionRequest;
import com.husyairi.ManaProgressAku.DTO.InsertSessionResponse;

public interface SessionService {

    public InsertSessionResponse createSession(InsertSessionRequest request);

    public GetSessionResponse getSession(String sessionID);
}
