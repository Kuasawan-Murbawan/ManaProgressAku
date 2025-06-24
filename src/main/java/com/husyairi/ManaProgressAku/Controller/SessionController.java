package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.GetExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.InsertSessionRequest;
import com.husyairi.ManaProgressAku.DTO.InsertSessionResponse;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/mana-progress-aku")
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @PostMapping("/insertSession")
    public ResponseEntity<ApiSuccessResponse<InsertSessionResponse>> insertSession(@RequestBody InsertSessionRequest request){
        InsertSessionResponse data = sessionService.createSession(request);

        ApiSuccessResponse<InsertSessionResponse> response = new ApiSuccessResponse<>(
                "Session created successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
