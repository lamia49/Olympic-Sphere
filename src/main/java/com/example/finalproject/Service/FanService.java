package com.example.finalproject.Service;

import com.example.finalproject.API.ApiException;
import com.example.finalproject.DTO.FanDTO;
import com.example.finalproject.Model.*;
import com.example.finalproject.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FanService {
    private final FanRepository fanRepository;
    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;
    private final TeamAdminRepository teamAdminRepository;
    private final AuthRepository authRepository;
    private final SportRepository sportRepository;
    private  final  AthleteRepository athleteRepository;

    public List<Fan> getAllFans(){
        return fanRepository.findAll();
    }


    public void RegisterFan(FanDTO fanDTO){

        String hashPassword = new BCryptPasswordEncoder().encode(fanDTO.getPassword());
        fanDTO.setPassword(hashPassword);

        User user = new User();
        Fan fan = new Fan();

        user.setId(null);
        user.setUsername(fanDTO.getUsername());
        user.setPassword(hashPassword);
        user.setRole("FAN");


        fan.setFanId(fanDTO.getFan_id());
        fan.setUsername(fanDTO.getUsername());
        fan.setPassword(fanDTO.getPassword());
        fan.setEmail(fanDTO.getEmail());
        fan.setAge(fanDTO.getAge());
        fan.setFavoriteSport(fanDTO.getFavoriteSport());


        user.setFan(fan);
        fan.setUser(user);

        fanRepository.save(fan);
        authRepository.save(user);


    }

    public void updateFan(Integer fanId , FanDTO fanDTO){
        Fan fan = fanRepository.findFanByFanId(fanId);
        User user = authRepository.findUserByUsername(fanDTO.getUsername());

        if (fan.getUsername().equalsIgnoreCase(user.getFan().getUsername())) {
            fan.setAge(fanDTO.getAge());
            fan.setEmail(fanDTO.getEmail());
            fan.setPassword(fanDTO.getPassword());
            fan.setAge(fanDTO.getAge());
            fan.setFavoriteSport(fanDTO.getFavoriteSport());
            fanRepository.save(fan);
        }
    }

    public void deleteFan(Integer fanId){
        Fan fan1 = fanRepository.findFanByFanId(fanId);
        fanRepository.delete(fan1);
    }


//    endpoint


    public String FanLogIn(Integer fanId , String password){
        Fan fan1 = fanRepository.findFanByFanId(fanId);
        if (fan1 == null || !fan1.getPassword().equals(password)){
            throw new ApiException("Invalid Id or Password");
        }
        return "Welcome " + fan1.getUsername() + "To Olympic Sphere";
    }


    public List<Ticket> viewTicketToBook(){
        List<Ticket> tickets = ticketRepository.findAllTicketByTicketDateIsAfter(LocalDate.now());

        for (Ticket ticket : tickets){
            if (ticket.getQuantity() >=1){

               return tickets ;
            }
        }

        return tickets;
    }


    public Ticket getTicketDetails(Integer ticketId){
        Ticket ticket = ticketRepository.findTicketById(ticketId);
        if (ticket == null){
            throw new ApiException("Invalid Id");
        }
        return ticket;
    }


    public List<Booking> viewMyBookedTicket(){
        List<Booking> booking = bookingRepository.getBookingsByBookingStatusIgnoreCase("Attended");
        if (booking == null){
            throw new ApiException("Invalid Id");
        }
        return booking;
    }


    public List<Booking> viewBookingByStatus(String status){
        List<Booking> booking = bookingRepository.getBookingsByBookingStatusIgnoreCase(status);
        if (booking == null){
            throw new ApiException("Invalid Id");
        }
        return booking;
    }



    public void changeStatusToAttended(Integer BookingId){
        Booking booking = bookingRepository.findBookingById(BookingId);
        if (booking == null){
            throw new ApiException("Invalid Id");
        }
        booking.setBookingStatus("Attended");
        bookingRepository.save(booking);
    }


    public void cancelBooking(Integer BookingId){
        Booking booking = bookingRepository.findBookingById(BookingId);
        if (booking == null){
            throw new ApiException("Invalid Id");
        }
        booking.setBookingStatus("Canceled");
        bookingRepository.save(booking);
    }

    public List<String> getAllTeams(){
        List<TeamAdmin>teamAdmins=teamAdminRepository.findAll();
        List<String>teams=new ArrayList<>();
        for(TeamAdmin team:teamAdmins){
            teams.add("Name: "+team.getTeamName()+" Number of Athletes: "+team.getAthleteSet().size());
        }
        return teams;
    }

    public List<Sport> viewAllSports(){
        return sportRepository.findAll();
    }

    public String athleteAchievements(String athleteName){
        Athlete athlete= athleteRepository.findAthleteByUsername(athleteName);
        if(athlete==null){
            throw new ApiException("Athlete Not Found");
        }
        return "Athlete Achievements is: "+athlete.getAchievements();
    }





}
