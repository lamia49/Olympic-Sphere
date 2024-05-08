package com.example.finalproject.SecurityConfig;


import com.example.finalproject.Service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Security {
    private final MyUserDetailsService myUserDetailsService;


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()

//      Registers
                .requestMatchers("api/v1/fan/register").permitAll()
                .requestMatchers("api/v1/athlete/register-athlete" ).permitAll()
                .requestMatchers("api/v1/sport-admin/register").permitAll()
                .requestMatchers("api/v1/team-admin/register").permitAll()

//      Athlete
                .requestMatchers("api/v1/athlete/update" , "api/v1/athlete/get","api/v1/athlete/logIn", "api/v1/athlete/logout", "api/v1/athlete/join/{teamName}" ).hasAuthority("ATHLETE")
                .requestMatchers("api/v1/athlete/find-athlete/{name}", "api/v1/athlete/get-top5","api/v1/athlete/view-details", "api/v1/athlete/view-teams","api/v1/athlete/get-achievements" ,"api/v1/athlete/all-sports","api/v1/athlete/get-achievements-by-name/{athleteName}").permitAll()
                .requestMatchers("api/v1/athlete/register-team-athlete" , "api/v1/athlete/delete").hasAuthority("TEAM-ADMIN")
                .requestMatchers("api/v1/athlete//find-athlete/{name}").permitAll()

//      Fans
                .requestMatchers("api/v1/fan/update","api/v1/fan/logIn","api/v1/fan/logout","api/v1/fan/view-ticket", "api/v1/fan/my-booked","api/v1/fan/get-by-status/{status}", "api/v1/fan/change-status-to-attended/{bookingId}").hasAuthority("FAN")
                .requestMatchers("api/v1/fan/get","api/v1/fan/delete").hasAuthority("SPORT-ADMIN")
                .requestMatchers("api/v1/fan/get-teams","api/v1/fan/all-sports","api/v1/fan/view-achievements/{athleteName}").permitAll()

//      Sport Admin
                .requestMatchers("api/v1/sport-admin/update","api/v1/sport-admin/accept-request/{teamName}" , "api/v1/sport-admin/decline-request/{requestId}" , "api/v1/sport-admin/logIn" , "api/v1/sport-admin/delete" , "api/v1/sport-admin/set-rank/{teamAdminId}/{rank}" ,"api/v1/sport-admin/view-requests","api/v1/sport-request/get","api/v1/sport-admin/logout" ).hasAuthority("SPORT-ADMIN")
                .requestMatchers("api/v1/sport-admin/view-all-sports" , "api/v1/sport-admin/search-sport-name/{name}" ).permitAll()

//      Team Admin
                .requestMatchers("api/v1/team-admin/update", "api/v1/team-admin/get","api/v1/team-admin/logIn","api/v1/team-admin/approve/{requestId}", "api/v1/team-admin/declines/{requestId}", "api/v1/team-admin/get-all-request", "api/v1/team-admin/set-athlete-rank/{athleteId}/{rank}","api/v1/team-admin/logout","api/v1/team-admin/add-achievements/{AthleteId}").hasAuthority("TEAM-ADMIN")
                .requestMatchers("api/v1/team-admin/delete").hasAuthority("SPORT-ADMIN")
                .requestMatchers("api/v1/team-admin/top-teams" , "api/v1/team-admin/get-team-name/{name}","api/v1/team-admin/search-all-sports" , "api/v1/team-admin/view-all-sports" , "api/v1/team-admin/view-achievements/{athleteName}" , "api/v1/team-admin/view-all-teams").permitAll()
//      Sport
                .requestMatchers("api/v1/sports/add" , "api/v1/sports/get").hasAuthority("SPORT-ADMIN")

//      Booking
                .requestMatchers("api/v1/booking/{bookingId}", "api/v1/booking/get" , "api/v1/booking/delete/{id}", "api/v1/booking/add/{fanId}" , "api/v1/booking/change-status/{bookingId}/{bookingStatus}" , "api/v1/booking/all-bookings/{fanId}").permitAll()
//      Ticket
                .requestMatchers("api/v1/ticket/get", "api/v1/ticket/add", "api/v1/ticket/delete/{id}", "api/v1/ticket/update/{id}").permitAll()

//
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .httpBasic();
        return httpSecurity.build();
    }
}