package com.example.finalproject.Service;


import com.example.finalproject.API.ApiException;
import com.example.finalproject.DTO.SportAdminDTO;
import com.example.finalproject.Model.*;
import com.example.finalproject.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SportAdminService {

    private final SportAdminRepository sportAdminRepository;
    private final SportAdminRequestRepository sportAdminRequestRepository;
    private final TeamAdminRepository teamAdminRepository;
    private final SportRepository sportRepository;
    private final AuthRepository authRepository;




    public List<SportAdmin> getAllSportAdmin() {
        return sportAdminRepository.findAll();
    }





    public String updateSportAdmin(Integer sportAdminId ,SportAdminDTO sportAdminDTO) {
        SportAdmin sportAdmin = sportAdminRepository.findSportAdminByAdminId(sportAdminId);
        User user = authRepository.findUserByUsername(sportAdminDTO.getSportAdminName());

        if (sportAdmin.getAdminId().equals(user.getSportAdmin().getAdminId())) {

            sportAdmin.setSportAdminName(sportAdminDTO.getSportAdminName());
            sportAdmin.setPassword(sportAdminDTO.getPassword());
            sportAdmin.setLicenseNumber(sportAdminDTO.getLicenseNumber());
            sportAdmin.setPhoneNumber(sportAdminDTO.getPhoneNumber());
            sportAdmin.setEmail(sportAdminDTO.getEmail());
            sportAdmin.setSportType(sportAdminDTO.getSportType());

            sportAdminRepository.save(sportAdmin);

            return "sport admin successfully updated";
        }

        return "sport admin not found";
    }



    public void deleteSportAdmin(Integer sportAdminId) {
        SportAdmin sportAdmin = sportAdminRepository.findSportAdminByAdminId(sportAdminId);
        if(sportAdmin == null) {
            throw new ApiException("Invalid SportAdmin ID");
        }
        sportAdminRepository.delete(sportAdmin);
    }


//     endpoint

    public void RegisterSportAdmin(SportAdminDTO sportAdminDTO) {

        String hashPassword = new BCryptPasswordEncoder().encode(sportAdminDTO.getPassword());
        sportAdminDTO.setPassword(hashPassword);

        User user = new User() ;
        SportAdmin sportAdmin = new SportAdmin();


        user.setId(null);
        user.setUsername(sportAdminDTO.getSportAdminName());
        user.setPassword(sportAdminDTO.getPassword());
        user.setRole("SPORT-ADMIN");


        sportAdmin.setAdminId(sportAdminDTO.getAdmin_id());
        sportAdmin.setSportAdminName(sportAdminDTO.getSportAdminName());
        sportAdmin.setPassword(sportAdminDTO.getPassword());
        sportAdmin.setLicenseNumber(sportAdminDTO.getLicenseNumber());
        sportAdmin.setPhoneNumber(sportAdminDTO.getPhoneNumber());
        sportAdmin.setEmail(sportAdminDTO.getEmail());
        sportAdmin.setSportType(sportAdminDTO.getSportType());


        user.setSportAdmin(sportAdmin);
        sportAdmin.setUser(user);

        sportAdminRepository.save(sportAdmin);
        authRepository.save(user);
    }


    public String SportAdminLogIn(Integer sportAdminId , String password){
        SportAdmin sportAdmin = sportAdminRepository.findSportAdminByAdminId(sportAdminId);
        if (sportAdmin == null || !sportAdmin.getPassword().equals(password)){
            throw new ApiException("invalid user id or password");
        }

        return "Welcome " + sportAdmin.getSportAdminName() + "To Olympic Sphere";
    }

    public void sportAdminDeclinesTeamAdmin(Integer sportAdminId , String teamName){
        SportAdmin sportAdmin = sportAdminRepository.findSportAdminByAdminId(sportAdminId);
        SportAdminRequest sportAdminRequest = sportAdminRequestRepository.findSportAdminRequestByTeamName(teamName);
        TeamAdmin teamAdmin = teamAdminRepository.findTeamAdminByteamName(teamName);

        if (sportAdmin == null || sportAdminRequest == null || teamAdmin == null) {
            throw new ApiException("Invalid request id or sport admin id");
        }

        sportAdminRequest.setStatus("Declined");
        sportAdminRequestRepository.save(sportAdminRequest);
    }

    public Sport searchSportByName(String sportName){
        return sportRepository.findSportByName(sportName);
    }

    public List<Sport> viewAllSports(){
        return sportRepository.findAll();
    }

    public void setTeamRank(Integer sportAdminId , Integer teamAdminId , Integer rank){
        SportAdmin sportAdmin = sportAdminRepository.findSportAdminByAdminId(sportAdminId);
        TeamAdmin teamAdmin= teamAdminRepository.findTeamAdminByTeamId(teamAdminId);
        if (sportAdmin == null || teamAdmin == null){
            throw new ApiException("invalid request team admin id or sport admin id");
        }
        teamAdmin.setTeamRank(rank);
        teamAdminRepository.save(teamAdmin);
    }


    public void sportAdminAcceptTeamAdmin(Integer sportAdminId , String teamName){

            SportAdmin sportAdmin = sportAdminRepository.findSportAdminByAdminId(sportAdminId);
            SportAdminRequest sportAdminRequest = sportAdminRequestRepository.findSportAdminRequestByTeamName(teamName);
            TeamAdmin teamAdmin = teamAdminRepository.findTeamAdminByteamName(teamName);

            if (sportAdmin == null || sportAdminRequest == null || teamAdmin == null) {
                throw new ApiException("Invalid request id or sport admin id");
            }

            sportAdminRequest.setStatus("Accepted");

            // Initialize teamAdminSet only if it's null
            sportAdmin.setTeamAdminSet(sportAdmin.getTeamAdminSet() == null? new HashSet<>() : sportAdmin.getTeamAdminSet());

            // Check if teamAdmin is not already assigned to sportAdmin
            if (!sportAdmin.getTeamAdminSet().contains(teamAdmin)) {
                sportAdmin.getTeamAdminSet().add(teamAdmin);
            }

            // Save changes in a single transaction
            sportAdminRepository.saveAndFlush(sportAdmin);
            sportAdminRequestRepository.saveAndFlush(sportAdminRequest);



        if (sportAdmin == null || sportAdminRequest == null || teamAdmin == null){
            throw new ApiException("invalid request id or sport admin id");
        }
        sportAdminRequest.setStatus("Accepted");


        if (sportAdmin.getTeamAdminSet() == null) {
            sportAdmin.setTeamAdminSet(new HashSet<>());
        }

        sportAdmin.getTeamAdminSet().add(teamAdmin);


        teamAdminRepository.save(teamAdmin);
        sportAdminRepository.save(sportAdmin);
        sportAdminRequestRepository.save(sportAdminRequest);
    }

    public List<String> viewRequest(Integer sportAdminId){
        SportAdmin sportAdmin = sportAdminRepository.findSportAdminByAdminId(sportAdminId);
        List<SportAdminRequest>request=sportAdminRequestRepository.findSportAdminRequestByStatus("Pending");
        List<String>requests=new ArrayList<>();
        for (SportAdminRequest sportAdminRequest : request){
            requests.add("Admin Name of Team: "+sportAdmin.getSportAdminName()+" Status: "+sportAdminRequest.getStatus()+"Team Name: "+sportAdminRequest.getTeamName());
        }

        return requests;
    }

}
