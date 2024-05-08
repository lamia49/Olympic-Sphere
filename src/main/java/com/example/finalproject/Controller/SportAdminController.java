package com.example.finalproject.Controller;


import com.example.finalproject.API.ApiResponse;
import com.example.finalproject.DTO.SportAdminDTO;


import com.example.finalproject.Model.User;
import com.example.finalproject.Service.SportAdminService;
import com.example.finalproject.Service.SportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sport-admin")
@RequiredArgsConstructor
public class SportAdminController {


    private final SportAdminService sportAdminService;


    @GetMapping("/get")
    public ResponseEntity getAllSportAdmin(){
        return ResponseEntity.status(200).body(sportAdminService.getAllSportAdmin());
    }


    @PostMapping("/register")
    public ResponseEntity RegisterSportAdmin(@RequestBody @Valid SportAdminDTO sportAdminDTO){
        sportAdminService.RegisterSportAdmin(sportAdminDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Sport Admin added successfully"));
    }


    @PutMapping("/update")
    public ResponseEntity updateSportAdmin(@AuthenticationPrincipal User user , @RequestBody @Valid SportAdminDTO sportAdminDTO){
        return ResponseEntity.status(200).body(sportAdminService.updateSportAdmin(user.getSportAdmin().getAdminId() ,sportAdminDTO));
    }


    @DeleteMapping("/delete")
    public ResponseEntity deleteSportAdmin(@AuthenticationPrincipal User user ){
        sportAdminService.deleteSportAdmin(user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Sport Admin deleted successfully"));
    }

    @GetMapping("/logIn")
    public ResponseEntity SportAdminLogIn(@AuthenticationPrincipal User user ){
        return ResponseEntity.status(200).body(sportAdminService.SportAdminLogIn(user.getSportAdmin().getAdminId() , user.getPassword()));
    }

    @PutMapping("/decline/{teamName}")
    public ResponseEntity DeclineTeamAdminRequest(@AuthenticationPrincipal User user , @PathVariable String teamName){
        sportAdminService.sportAdminDeclinesTeamAdmin(user.getId(),teamName);
        return ResponseEntity.status(200).body(new ApiResponse("Sport Admin decline Team Athlete successfully"));

    }


    @PutMapping("/set-rank/{teamAdminId}/{rank}")
    public ResponseEntity SetTeamRank(@AuthenticationPrincipal User user , @PathVariable Integer teamAdminId , @PathVariable Integer rank){
        sportAdminService.setTeamRank(user.getId(), teamAdminId,rank);
        return ResponseEntity.status(200).body(new ApiResponse("Team Rank set successfully"));

    }

    @GetMapping("/all-sports")
    public ResponseEntity ViewAllSports(){
        return ResponseEntity.status(200).body(sportAdminService.viewAllSports());
    }

    @GetMapping("/search-name/{name}")
    public ResponseEntity SearchSportByName(@PathVariable String name){
        return ResponseEntity.status(200).body(sportAdminService.searchSportByName(name));
    }

    @PutMapping("/accept/{teamName}")
    public ResponseEntity sportAdminAcceptedTeamAdmin(@AuthenticationPrincipal User user , @PathVariable String teamName){
        sportAdminService.sportAdminAcceptTeamAdmin(user.getId(),teamName);
        return ResponseEntity.status(200).body(new ApiResponse("Sport Admin approved Team successfully"));
    }


    @GetMapping("/logout")
    public ResponseEntity SportAdminLogout(){
        return ResponseEntity.status(200).body(new ApiResponse("Team Admin logged out"));
    }
    @GetMapping("/view-requests")
    public ResponseEntity viewRequest(@AuthenticationPrincipal User user){
        return ResponseEntity.status(200).body(sportAdminService.viewRequest(user.getId()));
    }

}
