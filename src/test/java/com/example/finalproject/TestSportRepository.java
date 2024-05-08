package com.example.finalproject;

import com.example.finalproject.API.ApiResponse;
import com.example.finalproject.Model.Sport;
import com.example.finalproject.Model.SportAdmin;
import com.example.finalproject.Model.User;
import com.example.finalproject.Repository.SportAdminRepository;
import com.example.finalproject.Repository.SportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestSportRepository {
     @Autowired
     SportRepository sportRepository;

    User user1 , user2 , user3 ;
    Sport sport1, sport2, sport3 ;
    ApiResponse apiResponse ;
    List<Sport> sports ;
    Sport sport;



    @BeforeEach
    void setUp() {


        sport1 = new Sport(1,"BasketBall",null);
        sport2 = new Sport(2,"footBall",null);
        sport3 = new Sport(3,"Sweeming",null);

    }



        @Test
        public void TestfindSportById(){
        sportRepository.saveAndFlush(sport1);
        sportRepository.saveAndFlush(sport2);
        sportRepository.saveAndFlush(sport3);

        sport = sportRepository.findById(sport1.getId()).orElse(null);
        }


   @Test
        public void TestfindSportByName(){
        sportRepository.save(sport1);
        sportRepository.save(sport2);
        sportRepository.save(sport3);

        sport = sportRepository.findSportByName("Sweeming");
        }


}
