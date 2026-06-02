package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.Session.*;
import com.husyairi.ManaProgressAku.Entity.Model.Session;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Session", description = "CRUD for Session")
@CrossOrigin("*")
@RestController
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @Operation(
            summary = "Create a new session",
            description = "Create new session using current Date and Time"
    )
    @PostMapping("/insertSession")
    public ResponseEntity<ApiSuccessResponse<InsertSessionResponse>> insertSession(@RequestBody InsertSessionRequest request){
        InsertSessionResponse data = sessionService.createSession(request);

        ApiSuccessResponse<InsertSessionResponse> response = new ApiSuccessResponse<>(
                "Session created successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @Operation(
            summary = "Get a session",
            description = "Return a session using session ID"
    )
    @GetMapping("/getSession/{sessionID}")
    public ResponseEntity<ApiSuccessResponse<GetSessionResponse>> getSession(@PathVariable String sessionID){

        GetSessionResponse data = sessionService.getSession(sessionID);

        ApiSuccessResponse<GetSessionResponse> response = new ApiSuccessResponse<>(
                "Session fetched!",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Update session",
            description = "Update session using request body"
    )
    @PutMapping("/updateSession")
    public ResponseEntity<ApiSuccessResponse<Session>> updateSession (@RequestBody UpdateSessionRequest request){

        Session data = sessionService.updateSession(request);

        ApiSuccessResponse<Session> response = new ApiSuccessResponse<>(
                "Session updated!",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete session",
            description = "Delete a session using session ID"
    )
    @DeleteMapping("/deleteSession/{sessionID}")
    public ResponseEntity<ApiSuccessResponse<DeleteSessionResponse>> deleteSession(@PathVariable String sessionID){
        sessionService.deleteSession(sessionID);

        DeleteSessionResponse response = new DeleteSessionResponse(sessionID);

        return ResponseEntity.ok(
                new ApiSuccessResponse<>("Session deleted successfully", response)
        );
    }

    @Operation(
            summary = "Get user's past session",
            description = "Return all sessions from current user"
    )
    @GetMapping("/getUserSessions")
    public ResponseEntity<ApiSuccessResponse<List<Session>>> getUserSessions(){
        List<Session> sessionsFetched = sessionService.getUserSessions();

        return ResponseEntity.ok(
                new ApiSuccessResponse<>("Sessions fetched! " , sessionsFetched)
        );
    }

    @PatchMapping("/finishSession/{sessionID}")
    public ResponseEntity<ApiSuccessResponse<Session>> finishSession (@PathVariable String sessionID){

        Session data = sessionService.finishSession(sessionID);

        ApiSuccessResponse<Session> response = new ApiSuccessResponse<>(
                "Session completed successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/session/active")
    public ResponseEntity<ApiSuccessResponse<ActiveSessionResponse>> getActiveSession(){

        ActiveSessionResponse data = sessionService.getActiveSession();

        ApiSuccessResponse<ActiveSessionResponse> response = new ApiSuccessResponse<>(
                "Active session have been checked.",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllSessions")
    public ResponseEntity<ApiSuccessResponse<List<Session>>> getAllSession(){

//        List<Session> allSessions = sessionService.getAllSessions();

        List<Session> allSessions = null;

        return ResponseEntity.ok(
                new ApiSuccessResponse<>("All sessions fetched successfully!", allSessions)
        );
    }
}
