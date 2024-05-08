package com.example.finalproject.Service;

import com.example.finalproject.API.ApiException;
import com.example.finalproject.DTO.AthleteDTO;
import com.example.finalproject.Model.*;
import com.example.finalproject.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AthleteService {
    private final AthleteRepository athleteRepository;
    private final TeamAdminRepository teamAdminRepository;
    private final TeamAdminRequestRepository teamAdminRequestRepository;
    private final AuthRepository authRepository;
    private  final SportRepository sportRepository;

    public List<Athlete> getAllAthletes(){
        return athleteRepository.findAll();
    }



    public void RegisterAthlete(AthleteDTO athleteDTO) {
        String hashPassword = new BCryptPasswordEncoder().encode(athleteDTO.getPassword());
        athleteDTO.setPassword(hashPassword);

        User user = new User() ;
        Athlete athlete = new Athlete();

        user.setId(null);
        user.setUsername(athleteDTO.getUsername());
        user.setPassword(athleteDTO.getPassword());
        user.setRole("ATHLETE");

        athlete.setAthleteId(athleteDTO.getAthleteId());
        athlete.setUsername(athleteDTO.getUsername());
        athlete.setPassword(hashPassword);
        athlete.setEmail(athleteDTO.getEmail());
        athlete.setAge(athleteDTO.getAge());
        athlete.setWeight(athleteDTO.getWeight());
        athlete.setHeight(athleteDTO.getHeight());
        athlete.setGender(athleteDTO.getGender());
        athlete.setSport(athleteDTO.getSport());
        athlete.setCategory(athleteDTO.getCategory());
        athlete.setAchievements(athleteDTO.getAchievements());
        athlete.setLicenseNumber(athleteDTO.getLicenseNumber());
        athlete.setRank(athleteDTO.getRank());

        user.setAthlete(athlete);
        athlete.setUser(user);

        athleteRepository.save(athlete);
        authRepository.save(user);



    }



// test
    public void TeamAdminRegisterAthlete(Integer teamAdminId, AthleteDTO athleteDTO) {
        TeamAdmin teamAdmin= teamAdminRepository.findTeamAdminByTeamId(teamAdminId);


        String hashPassword = new BCryptPasswordEncoder().encode(athleteDTO.getPassword());
        athleteDTO.setPassword(hashPassword);

        User user = new User() ;
        Athlete athlete = new Athlete();

        user.setId(null);
        user.setUsername(athleteDTO.getUsername());
        user.setPassword(hashPassword);
        user.setRole("ATHLETE");

        athlete.setAthleteId(athleteDTO.getAthleteId());
        athlete.setUsername(athleteDTO.getUsername());
        athlete.setPassword(hashPassword);
        athlete.setEmail(athleteDTO.getEmail());
        athlete.setAge(athleteDTO.getAge());
        athlete.setWeight(athleteDTO.getWeight());
        athlete.setHeight(athleteDTO.getHeight());
        athlete.setGender(athleteDTO.getGender());
        athlete.setSport(athleteDTO.getSport());
        athlete.setCategory(athleteDTO.getCategory());
        athlete.setAchievements(athleteDTO.getAchievements());
        athlete.setLicenseNumber(athleteDTO.getLicenseNumber());
        athlete.setRank(athleteDTO.getRank());

        user.setAthlete(athlete);
        athlete.setUser(user);
        athlete.setTeamAdmin(teamAdmin);
        athleteRepository.save(athlete);
        authRepository.save(user);
        teamAdminRepository.save(teamAdmin);

    }

    public String updateAthlete(Integer athleteId ,  AthleteDTO athleteDTO){
        Athlete athlete = athleteRepository.findAthleteByAthleteId(athleteId);
        User user = authRepository.findUserByUsername(athleteDTO.getUsername());

        if (athlete.getUsername().equalsIgnoreCase(user.getAthlete().getUsername())) {
            athlete.setPassword(athleteDTO.getPassword());
            athlete.setEmail(athleteDTO.getEmail());
            athlete.setAge(athleteDTO.getAge());
            athlete.setGender(athleteDTO.getGender());
            athlete.setSport(athleteDTO.getSport());
            athlete.setWeight(athleteDTO.getWeight());
            athlete.setHeight(athleteDTO.getHeight());
            athlete.setCategory(athleteDTO.getCategory());
            athlete.setLicenseNumber(athleteDTO.getLicenseNumber());
            athleteRepository.save(athlete);
            return "Athlete Updated";
        }

        return "Athlete Not Found";
    }

    public void deleteAthlete(Integer athleteId){
        Athlete athlete1 = athleteRepository.findAthleteByAthleteId(athleteId);
        athleteRepository.delete(athlete1);
    }


//    endpoint

    public String AthleteLogIn(Integer AthleteId , String password){
        Athlete athlete = athleteRepository.findAthleteByAthleteId(AthleteId);
        if (athlete == null || !athlete.getPassword().equals(password)){
            throw new ApiException("invalid user id or password");
        }

        return "Welcome " + athlete.getUsername() + "To Olympic Sphere";
    }

    public List<Athlete>findAthleteBySportName(String sportName){
        return athleteRepository.findAthleteBySport(sportName);
    }

    public List<Athlete> top5Athletes(){
        return athleteRepository.findAthleteByRankBetween(1,5);
    }



    public Athlete viewDetails(Integer athleteId){
        Athlete athlete=athleteRepository.findAthleteByAthleteId(athleteId);
        return athlete;
    }

    public List<String> getAllTeams(){
        List<TeamAdmin>teamAdmins=teamAdminRepository.findAll();
        List<String>teams=new ArrayList<>();
        for(TeamAdmin team:teamAdmins){
            teams.add("Name: "+team.getTeamName()+" Number of Athletes: "+team.getAthleteSet().size());
        }
        return teams;
    }

    public Integer getAchievements(Integer athleteId){
        Athlete athlete=athleteRepository.findAthleteByAthleteId(athleteId);
        return athlete.getAchievements();
    }


    public String SearchAthleteByName(String AthleteName){
        Athlete athlete=athleteRepository.findAthleteByUsername(AthleteName);
        return " Name: "+athlete.getUsername()+" Sport: "+athlete.getSport()+" Weight: "+athlete.getWeight()+
                " Height: "+athlete.getHeight()+" Category: "+athlete.getCategory()+" "+athlete.getSport()+
                " Achievements: "+athlete.getAchievements()+" Gender:  "+athlete.getGender()+
                " Age: "+athlete.getAge()+" LicenseNumber: "+athlete.getLicenseNumber();
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



}



