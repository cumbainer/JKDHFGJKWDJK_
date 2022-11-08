//package com.softserve.itacademy.security;
//
//import com.softserve.itacademy.model.User;
//import com.softserve.itacademy.repository.UserRepository;
//import com.softserve.itacademy.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomUserDetailsService implements UserDetailsService {
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.getUserByEmail(username);
//        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
//    }
//}
