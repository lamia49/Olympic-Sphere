package com.example.finalproject;

import com.example.finalproject.API.ApiResponse;
import com.example.finalproject.Model.Fan;
import com.example.finalproject.Model.SportAdmin;
import com.example.finalproject.Model.User;
import com.example.finalproject.Repository.FanRepository;
import com.example.finalproject.Repository.SportAdminRepository;
import com.example.finalproject.Repository.SportRepository;
import com.example.finalproject.Service.FanService;
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
public class TestSportAdminRepository {
      @Autowired
 SportAdminRepository sportAdminRepository;

    User user1 , user2 , user3 ;
    SportAdmin sportAdmin1, sportAdmin2, sportAdmin3 ;
    ApiResponse apiResponse ;
    List<SportAdmin> sportAdmins ;
    SportAdmin sportAdmin;



    @BeforeEach
    void setUp() {


        sportAdmin1 = new SportAdmin(1,"mohamed1","mohamed@1234",12345,"052366544","mohamed@hotmail.com","karate",null,user1,null,null);
        sportAdmin2 = new SportAdmin(2,"mohamed2","mohamed2@1234",122345,"05552366544","mohamed2@hotmail.com","karate",null,user2,null,null);
        sportAdmin1 = new SportAdmin(3,"mohamed3","mohamed3@1234",12345,"0588855","mohamed3@hotmail.com","karate",null,user3,null,null);

        user1 = new User(null, null, null, null, null, sportAdmin1, null, null);
        user2 = new User(null, null, null, null, null, sportAdmin2, null, null);
        user3 = new User(null, null, null, null, null, sportAdmin3, null, null);
    }



        @Test
        public void TestfindSportAdminByAdminId(){
        sportAdminRepository.save(sportAdmin1);
        sportAdminRepository.save(sportAdmin2);
        sportAdminRepository.save(sportAdmin3);

        sportAdmin = sportAdminRepository.findSportAdminByAdminId(sportAdmin1.getAdminId());
        }


   @Test
        public void TestfindSportAdminBySportType(){
        sportAdminRepository.save(sportAdmin1);
        sportAdminRepository.save(sportAdmin2);
        sportAdminRepository.save(sportAdmin3);

        sportAdmin = sportAdminRepository.findSportAdminBySportType(sportAdmin1.getSportType());
        }

}
