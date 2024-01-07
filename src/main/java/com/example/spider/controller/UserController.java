package com.example.spider.controller;

import com.example.spider.model.DTOs.*;
import com.example.spider.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends AbstractController{
    @Autowired
    private UserService userService;
    private LoginDTO loginDTO;
    private HttpSession session;

    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@Valid @RequestBody final LoginDTO loginDTO, final HttpSession session){
        this.loginDTO = loginDTO;
        this.session = session;
        UserWithoutPasswordDTO user=userService.login(loginDTO);
        session.setAttribute(Util.LOGGED,true);
        session.setAttribute(Util.LOGGED_ID,user.getId());
        return user;
    }
    @PostMapping("/users/register")
    public UserWithoutPasswordDTO register(@Valid @RequestBody final UserRegisterDTO registerData){
        return userService.register(registerData);
    }
    @PostMapping("/users/logout")
    public ResponseEntity<String> logout(final HttpSession session) {

        invalidateSession(session);
        return ResponseEntity.ok("Logged out successfully");
    }
    @PutMapping("/users/changePass")
    public UserWithoutPasswordDTO changePassword(@Valid @RequestBody final ChangePassDTO changePassData, final HttpSession session){
        final long id=loggedId(session);
        return userService.changePassword(changePassData,id);
    }
    @PutMapping("/users/edit")
    public UserWithoutPasswordDTO editProfile(@Valid @RequestBody final EditProfileDTO editProfilDTO, final  HttpSession session){
        final long id=loggedId(session);
        return userService.editProfile(editProfilDTO,id);
    }
    @GetMapping("/confirm")
    public String confirmEmail(@RequestParam("token") final String token){
    if(userService.confirmEmail(token)){
        return "Email confirmed";
    }else {
        return "Invalid confirmation";
    }
    }
    @DeleteMapping("/users")
    public ResponseEntity<String> delete(@RequestBody final UserPasswordDTO userPasswordDTO, final HttpSession session){
        final long userId=loggedId(session);
        userService.delete(userPasswordDTO,userId);
        invalidateSession(session);
        return  ResponseEntity.ok("Account deleted");
    }
}
