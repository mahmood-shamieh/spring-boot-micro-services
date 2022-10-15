package com.javainuse.controller;

import java.util.Map;
import java.util.Objects;

import com.javainuse.model.UserData;
import com.javainuse.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.javainuse.config.JwtTokenUtil;
import com.javainuse.model.JwtRequest;
import com.javainuse.model.JwtResponse;

@RestController
@RequestMapping("/security")
//@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/validateToken")
    boolean validateToken(@RequestParam String token) {
        try {
            authenticate(jwtTokenUtil.getUsernameFromToken(token), jwtTokenUtil.getPasswordFromToken(token));
            return true;
        } catch (BadCredentialsException e) {
            System.out.println("Bad Credintioal at value of token");
            return false;
        } catch (ExpiredJwtException e) {
            System.out.println("expired token sent");
            return false;
        } catch (JwtException e) {
            System.out.println("error in token value");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("there is no understand of the problem");
            return false;
        }

    }

    @PostMapping("/register")
    public ResponseEntity registerNewUser(@RequestBody UserData userData){
        return jwtUserDetailsService.registerUser(userData);
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
