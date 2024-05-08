package com.example.finalproject.Service;


import com.example.finalproject.API.ApiException;
import com.example.finalproject.DTO.TeamAdminDTO;
import com.example.finalproject.Model.*;
import com.example.finalproject.Repository.*;
import com.example.finalproject.Model.SportAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamAdminService {

    private final TeamAdminRepository teamAdminRepository;
    private final AthleteRepository athleteRepository;
    private final TeamAdminRequestRepository teamAdminRequestRepository;
    private final SportAdminRepository sportAdminRepository;
    private final AuthRepository authRepository ;
    private final SportRepository sportRepository;
    private final SportAdminRequestRepository sportAdminRequestRepository;


    public List<TeamAdmin> getAllTeamAdmins() {
        return teamAdminRepository.findAll();
    }


    public void updateTeamAdmin(Integer teamAdminId ,TeamAdminDTO teamAdminDTO) {
        TeamAdmin teamAdmin = teamAdminRepository.findTeamAdminByTeamId(teamAdminId);
        User user = authRepository.findUserByUsername(teamAdminDTO.getTeamName());

        if (teamAdmin.getTeamName().equalsIgnoreCase(user.getTeamAdmin().getTeamName())) {

            teamAdmin.setTeamAdminName(teamAdminDTO.getTeamAdminName());
            teamAdmin.setTeamEmail(teamAdminDTO.getTeamEmail());
            teamAdmin.setPassword(teamAdminDTO.getPassword());
            teamAdmin.setPhoneNumber(teamAdminDTO.getPhoneNumber());

            teamAdminRepository.save(teamAdmin);
        }

    }



    public void deleteTeamAdmin(Integer teamAdminId){
        TeamAdmin teamAdmin1 = teamAdminRepository.findTeamAdminByTeamId(teamAdminId);
        teamAdminRepository.delete(teamAdmin1);
    }



//    endpoint


    public String TeamAdminLogIn(Integer teamAdminId , String password){
        TeamAdmin teamAdmin1 = teamAdminRepository.findTeamAdminByTeamId(teamAdminId);

        return "Welcome " + teamAdmin1.getTeamAdminName() + "To Olympic Sphere";
    }


    public void RegisterTeamAdmin(TeamAdminDTO teamAdminDTO) {


        String hashPassword = new BCryptPasswordEncoder().encode(teamAdminDTO.getPassword());
        teamAdminDTO.setPassword(hashPassword);

        User user = new User();
        TeamAdmin teamAdmin = new TeamAdmin();

        // Set user properties
        user.setId(null);
        user.setUsername(teamAdminDTO.getTeamName());
        user.setPassword(teamAdminDTO.getPassword());
        user.setRole("TEAM-ADMIN");



        // Set team admin properties
        teamAdmin.setTeamId(teamAdminDTO.getTeamId());
        teamAdmin.setTeamName(teamAdminDTO.getTeamName());
        teamAdmin.setPassword(teamAdminDTO.getPassword());
        teamAdmin.setTeamAdminName(teamAdminDTO.getTeamAdminName());
        teamAdmin.setTeamEmail(teamAdminDTO.getTeamEmail());
        teamAdmin.setPhoneNumber(teamAdminDTO.getPhoneNumber());
        teamAdmin.setTeamRank(teamAdminDTO.getTeamRank());

        // Create a new SportAdminRequest
        SportAdminRequest sportAdminRequest = new SportAdminRequest();
        sportAdminRequest.setRequestId(null);
        sportAdminRequest.setTeamName(teamAdminDTO.getTeamName());
        sportAdminRequest.setStatus("Pending");

        // Initialize teamAdminList if null
        if (sportAdminRequest.getTeamAdminList() == null) {
            sportAdminRequest.setTeamAdminList(new HashSet<>());
        }

        // Add team admin to the SportAdminRequest
        user.setTeamAdmin(teamAdmin);

        teamAdmin.setUser(user);
        teamAdminRepository.save(teamAdmin);
        authRepository.save(user);

        sportAdminRequest.getTeamAdminList().add(teamAdmin);
        sportAdminRequestRepository.save(sportAdminRequest);



    }




    public void teamAdminApprovesAthlete(Integer teamAdminId ,Integer requestId){
        TeamAdmin teamAdmin = teamAdminRepository.findTeamAdminByTeamId(teamAdminId);
        TeamAdminRequest teamAdminRequest = teamAdminRequestRepository.findTeamAdminRequestById(requestId);

        if (teamAdmin == null || teamAdminRequest == null){
            throw new ApiException("Invalid team admin id or request id");
        }
        if(teamAdminRequest.getStatus().equals("Declined")){
            teamAdminRequest.setStatus("Approved");
        } else {
            throw new ApiException("request status not declined");
        }

    }





    public void teamAdminDeclinesAthlete(Integer teamAdminId ,Integer requestId){
        TeamAdmin teamAdmin = teamAdminRepository.findTeamAdminByTeamId(teamAdminId);
        TeamAdminRequest teamAdminRequest = teamAdminRequestRepository.findTeamAdminRequestById(requestId);

        if (teamAdmin == null || teamAdminRequest == null){
            throw new ApiException("Invalid team admin id or request id");
        }
        if(teamAdminRequest.getStatus().equals("Approved")){
            teamAdminRequest.setStatus("Declined");
        } else {
            throw new ApiException("request status not approved");
        }

    }



    public List<TeamAdminRequest> getAllTeamAdminRequests(Integer TeamAdminId) {
        TeamAdmin teamAdmin = teamAdminRepository.findTeamAdminByTeamId(TeamAdminId);

        return teamAdminRequestRepository.findAll();
    }



    public List<TeamAdmin> top5Teams(){
        return teamAdminRepository.findTeamAdminByTeamRankBetween(1,5);
    }

    public List<TeamAdmin> findTeamByName(String teamName){
        return teamAdminRepository.findTeamAdminByTeamName(teamName);
    }

    public void setAthleteRank(Integer teamAdminId ,Integer athleteId , Integer rank){
        TeamAdmin teamAdmin = teamAdminRepository.findTeamAdminByTeamId(teamAdminId);
        Athlete athlete = athleteRepository.findAthleteByAthleteId(athleteId);
        if (athlete == null|| teamAdmin == null){
            throw new ApiException("invalid athlete id or team admin id");
        }
        athlete.setRank(rank);
        athleteRepository.save(athlete);
    }



    public void addAthleteAchievements(Integer teamAdminId ,Integer AthleteId){
        Athlete athlete=athleteRepository.findAthleteByLicenseNumber(AthleteId);
        TeamAdmin teamAdmin=teamAdminRepository.findTeamAdminByTeamId(athlete.getTeamAdmin().getTeamId());
        if(teamAdminId!=teamAdmin.getTeamId()){
            throw new ApiException("Invalid");
        }else
            athlete.setAchievements(athlete.getAchievements()+1);
        athleteRepository.save(athlete);
    }

    public List searchAllSportsInTeam(Integer teamAdminId){
        TeamAdmin teamAdmin=teamAdminRepository.findTeamAdminByTeamId(teamAdminId);
        List<SportAdmin> sport=sportAdminRepository.findAll();
        List<String> sports=new ArrayList<>();
        if(teamAdmin==null){
            throw new ApiException("Invalid");
        }else
            for(SportAdmin sport1:sport){
                sports.add(sport1.getSportType());
            }return sports;
    }

    public List<Sport> viewAllSports(){
        return sportRepository.findAll();
    }



    public String athleteAchievements(String athleteName){
            Athlete athlete=athleteRepository.findAthleteByUsername(athleteName);
            if(athlete==null){
                throw new ApiException("Athlete Not Found");
            }
            return "Athlete Achievements  is: "+athlete.getAchievements();
    }


    public List<String> getAllTeams(){
        List<TeamAdmin>teamAdmins=teamAdminRepository.findAll();
        List<String>teams=new ArrayList<>();
        for(TeamAdmin team:teamAdmins){
            teams.add("Name: "+team.getTeamName()+" Number of Athletes: "+team.getAthleteSet().size());
        }
        return teams;
    }

}
