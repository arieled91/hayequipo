package org.arieled91.hayequipo.auth.controller;

import org.arieled91.hayequipo.auth.exception.AuthorizationException;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;


@Controller
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService userService) {
        super();
        this.authService = userService;
    }

    @RequestMapping(value = "/auth/users/current", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> currentUser() {
        return ResponseEntity.ok(authService.getCurrentUser().orElse(new User()));
    }

    @RequestMapping(value = "/auth/users/current/privileges", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<String>> currentUserPrivileges() {
        return ResponseEntity.ok(authService.getCurrentUser().orElse(new User()).getPrivileges());
    }
    
    @RequestMapping(value = "/auth/users/current/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUsername() {
        return authService.getCurrentUser().map(User::getEmail).orElse("guest");
    }

    @RequestMapping(value = "/auth/users/current/upgrade", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> upgradeUser(final String email) {
        try {
            authService.upgradeUser(email);
            return ResponseEntity.ok().build();
        }catch (final AuthorizationException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @RequestMapping(value = "/api/ping", method = RequestMethod.GET)
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }


    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }


}