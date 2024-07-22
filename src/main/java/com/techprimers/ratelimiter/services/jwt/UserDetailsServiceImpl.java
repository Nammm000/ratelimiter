//package com.techprimers.ratelimiter.services.jwt;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import com.techprimers.ratelimiter.models.User;
//import com.techprimers.ratelimiter.repo.UserRepo;
//
//import java.util.ArrayList;
//
//@Slf4j
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        //Write Logic to get the user from the DB
//        User user = userRepo.findFirstByEmail(email);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found", null);
//        }
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
//    }
//}
