package com.example.finalproject;

import com.example.finalproject.API.ApiResponse;
import com.example.finalproject.DTO.FanDTO;
import com.example.finalproject.DTO.TeamAdminDTO;
import com.example.finalproject.Model.Fan;
import com.example.finalproject.Model.TeamAdmin;
import com.example.finalproject.Model.User;
import com.example.finalproject.Repository.FanRepository;
import com.example.finalproject.Repository.TeamAdminRepository;
import com.example.finalproject.Service.FanService;
import com.example.finalproject.Service.TeamAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FanRepositoryTest {
        @Autowired
        FanRepository fanRepository;
FanService fanService;

    User user1 , user2 , user3 ;
    Fan fan1, fan2, fan3 ;
    ApiResponse apiResponse ;
    List<Fan> fanList ;
    Fan fan;



    @BeforeEach
    void setUp() {


        fan1 = new Fan(1, "ali", "ali@1234", "ali@hotmail.com", 30, "Football", user1, null);
        fan2 = new Fan(2, "mohammed", "mohmed@1234", "mohamed@hotmail.com", 20, "Football", user2, null);
        fan3 = new Fan(3, "noura", "noura@1234", "noura@hotmail.com", 33, "BasketBall", user3, null);

        user1 = new User(null, null, null, null, fan1, null, null, null);
        user2 = new User(null, null, null, null, fan2, null, null, null);
        user3 = new User(null, null, null, null, fan2, null, null, null);
    }



        @Test
        public void TestfindFanByFanId(){
        fanRepository.save(fan1);
        fanRepository.save(fan2);
        fanRepository.save(fan3);

        fan = fanRepository.findFanByFanId(fan1.getFanId());
        }


   @Test
        public void getAllBookingByFanId(){
        fanRepository.save(fan1);
        fanRepository.save(fan2);
        fanRepository.save(fan3);

        fan = fanRepository.findFanByFanId(fan1.getFanId());
        }


}
