package com.softserve.itacademy.service.details;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.security.JWT_USER;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public JwtUserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String username/*email*/) throws UsernameNotFoundException {
        Optional<User> user = userService.findByEmail(username);
        if (user == null) throw new UsernameNotFoundException("User not found!"); //isEmpty()

        User readUser = user.get();
        JWT_USER jwtUser = new JWT_USER(readUser.getId(),
                readUser.getFirstName(),
                readUser.getLastName(),
                readUser.getEmail(),
                readUser.getRole(),
                readUser.getPassword());

        return jwtUser;
    }
}
