package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.Session.GetSessionResponse;
import com.husyairi.ManaProgressAku.DTO.Session.InsertSessionRequest;
import com.husyairi.ManaProgressAku.DTO.Session.InsertSessionResponse;
import com.husyairi.ManaProgressAku.DTO.Session.UpdateSessionRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Session;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getSession/{sessionID}")
    public ResponseEntity<ApiSuccessResponse<GetSessionResponse>> getSession(@PathVariable String sessionID){

        GetSessionResponse data = sessionService.getSession(sessionID);

        ApiSuccessResponse<GetSessionResponse> response = new ApiSuccessResponse<>(
                "Session fetched!",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateSession")
    public ResponseEntity<ApiSuccessResponse<Session>> updateSession (@RequestBody UpdateSessionRequest request){

        Session data = sessionService.updateSession(request);

        ApiSuccessResponse<Session> response = new ApiSuccessResponse<>(
                "Session updated!",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteSession/{sessionID}")
    public ResponseEntity<ApiSuccessResponse<String>> deleteSession(@PathVariable String sessionID){
        sessionService.deleteSession(sessionID);

        ApiSuccessResponse<String> response = new ApiSuccessResponse<>(
                "Session deleted!",
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllSessions")
    public List<Session> getAllSession(){
        return sessionService.getAllSessions();
    }
}
