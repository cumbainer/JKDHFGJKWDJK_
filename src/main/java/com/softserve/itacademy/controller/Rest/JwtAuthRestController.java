package com.softserve.itacademy.controller.Rest;

import com.softserve.itacademy.dto.AuthRequest;
import com.softserve.itacademy.dto.AuthResponse;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.security.JwtProvider;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthRestController {

    @Autowired
    private AuthenticationManager authenticate;
    private JwtProvider jwtProvider;
    private UserService userService;

    @Autowired
    public JwtAuthRestController(AuthenticationManager authenticate, JwtProvider jwtProvider, UserService userService) {
        this.authenticate = authenticate;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity login(@RequestBody AuthRequest request){
        try{
            String username = request.getUsername();
            authenticate.authenticate(new UsernamePasswordAuthenticationToken(username,request.getPassword()));
            Optional<User> users = userService.findByEmail(username);
            if(users == null){ //isEmpty
                throw new UsernameNotFoundException("Invalid username.");
            }
            String token = jwtProvider.createToken(username,users.get().getRole());
            AuthResponse responseData = new AuthResponse();
            responseData.setUsername(username);
            responseData.setToken(token);

            return ResponseEntity.ok(responseData);

        }catch(AuthenticationException e){
            throw new BadCredentialsException("Invalid data.");
        }
    }




}
