package com.example.finalproject.Controller;

import com.example.finalproject.API.ApiResponse;
import com.example.finalproject.DTO.AthleteDTO;
import com.example.finalproject.Model.Athlete;
import com.example.finalproject.Model.User;

import com.example.finalproject.Service.AthleteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/athlete")
@RequiredArgsConstructor
public class AthleteController {
    private final AthleteService athleteService;
    @GetMapping("/get")
    public ResponseEntity getAllAthletes(){
        return ResponseEntity.status(200).body(athleteService.getAllAthletes());
    }

    @PutMapping("/update")
    public ResponseEntity updateAthlete(@AuthenticationPrincipal User user , @RequestBody @Valid AthleteDTO athleteDTO){
        return ResponseEntity.status(200).body(athleteService.updateAthlete(user.getId(), athleteDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteAthlete(@AuthenticationPrincipal User user ){
        athleteService.deleteAthlete(user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Athlete deleted"));
    }


    //ENDPOINTS

    @PostMapping("/register-athlete")
    public ResponseEntity RegisterAthlete( @RequestBody @Valid AthleteDTO athleteDTO){
        athleteService.RegisterAthlete(athleteDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Athlete Register successful"));
    }


    @PostMapping("/register-team-athlete")
    public ResponseEntity TeamAdminRegisterAthlete(@AuthenticationPrincipal User user, @RequestBody @Valid AthleteDTO athleteDTO){
        athleteService.TeamAdminRegisterAthlete(user.getId(), athleteDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Team Admin Athlete Registration successful"));
    }

    @GetMapping("/logIn")
    public ResponseEntity AthleteLogIn(@AuthenticationPrincipal User user){
        athleteService.AthleteLogIn(user.getId(), user.getPassword());
        return ResponseEntity.status(200).body(new ApiResponse("Athlete Login Successful"));
    }

    @GetMapping("/logout")
    public ResponseEntity AthleteLogOut(@AuthenticationPrincipal User user){
        return ResponseEntity.status(200).body(new ApiResponse("Athlete LogOut Successful"));
    }

    @GetMapping("/find-athlete/{name}")
    public ResponseEntity SearchAthleteByName(@PathVariable String name){
        return ResponseEntity.status(200).body(athleteService.SearchAthleteByName(name));
    }
    @GetMapping("/get-top5")
    public ResponseEntity top5Athletes(){
        return ResponseEntity.status(200).body(athleteService.top5Athletes());
    }

    @GetMapping("/view-details")
    public ResponseEntity viewDetails(@AuthenticationPrincipal User user){
        return ResponseEntity.status(200).body(athleteService.viewDetails(user.getId()));
    }

    @GetMapping("/view-teams")
    public ResponseEntity getAllTeams(){
        return ResponseEntity.status(200).body("The Teams is:"+athleteService.getAllTeams());
    }



    @GetMapping("/get-achievements")
    public ResponseEntity getAchievements(@AuthenticationPrincipal User user){
        return ResponseEntity.status(200).body(athleteService.getAchievements(user.getId()));
    }


    @GetMapping("/all-sports")
    public ResponseEntity ViewAllSports(){
        return ResponseEntity.status(200).body(athleteService.viewAllSports());
    }

    @GetMapping("/get-achievements-by-name/{athleteName}")
    public ResponseEntity athleteAchievements(@PathVariable String athleteName){
        return ResponseEntity.status(200).body(athleteService.athleteAchievements(athleteName));
    }
}
