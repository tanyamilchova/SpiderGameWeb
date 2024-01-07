package com.example.spider;

import com.example.spider.controller.Util;
import com.example.spider.model.DTOs.*;
import com.example.spider.model.entities.User;
import com.example.spider.model.exceptions.BadRequestException;
import com.example.spider.model.exceptions.NotFoundException;
import com.example.spider.model.exceptions.UnauthorizedException;
import com.example.spider.model.repositories.UserRepository;
import com.example.spider.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper mapper;
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    private UserService userService;
    @Test
    public void testSuccessfulLogin1() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(Util.EMAIL);
        loginDTO.setPassword(Util.PASS);

        User user = new User();
        user.setEmail(Util.EMAIL);
        user.setPassword(Util.PASS);
        user.setEnable(true);

        UserWithoutPasswordDTO expected=new UserWithoutPasswordDTO(1, Util.NAME,Util.EMAIL);

        when(userRepository.existsByEmail(loginDTO.getEmail())).thenReturn(true);
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(true);
        when(userService.login(loginDTO)).thenReturn(expected);

        UserWithoutPasswordDTO result = userService.login(loginDTO);

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    public void testWrongEmail() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(Util.EMAIL);
        loginDTO.setPassword(Util.PASS);

        when(userRepository.existsByEmail(loginDTO.getEmail())).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> userService.login(loginDTO));
    }
    @Test
    public void testWrongPassword() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(Util.EMAIL);
        loginDTO.setPassword("3456Sbb#");

        User user = new User();
        user.setEmail(Util.EMAIL);
        user.setPassword(Util.PASS);
        user.setEnable(true);

        when(userRepository.existsByEmail(loginDTO.getEmail())).thenReturn(true);
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> userService.login(loginDTO));
    }
    @Test
    public void testLoginUserNotEnabled() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(Util.EMAIL);
        loginDTO.setPassword(Util.PASS);

        User user = new User();
        user.setEmail(Util.EMAIL);
        user.setPassword(Util.PASS);
        user.setEnable(false);

        when(userRepository.existsByEmail(loginDTO.getEmail())).thenReturn(true);
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        assertThrows(UnauthorizedException.class, () -> userService.login(loginDTO));
    }
    @Test
    void successfulRegistration() {
        UserRegisterDTO registerData = new UserRegisterDTO();
        registerData.setEmail(Util.EMAIL);
        registerData.setPassword(Util.PASS);
        registerData.setConfirmPassword(Util.PASS);
        registerData.setName(Util.NAME);

        User u = new User();
        u.setEmail(registerData.getEmail());
        u.setPassword(registerData.getPassword());
        u.setName(registerData.getName());

        UserWithoutPasswordDTO expectedUser=new UserWithoutPasswordDTO();
        expectedUser.setEmail("test@example.com");
        expectedUser.setName(registerData.getName());

        when(userRepository.existsByEmail(registerData.getEmail())).thenReturn(false);
        when(mapper.map(registerData, User.class)).thenReturn(u);
        when(passwordEncoder.encode(registerData.getPassword())).thenReturn(Util.PASS);
        when(userRepository.save(any(User.class))).thenReturn(u);
        when(mapper.map(u, UserWithoutPasswordDTO.class)).thenReturn(expectedUser);

        UserWithoutPasswordDTO result = userService.register(registerData);

        assertNotNull(result);
        assertEquals(expectedUser, result);
    }
    @Test
    void registerPassMismatch() {
        UserRegisterDTO registerData = new UserRegisterDTO();
        registerData.setPassword(Util.PASS);
        registerData.setConfirmPassword("mismatchedPassword");

        assertThrows(BadRequestException.class, () -> userService.register(registerData));
    }
    @Test
    void registerEmailExists() {
        UserRegisterDTO registerData = new UserRegisterDTO();
        registerData.setEmail(Util.EMAIL);
        registerData.setPassword(Util.PASS);
        registerData.setConfirmPassword(Util.PASS);

        when(userRepository.existsByEmail(registerData.getEmail())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> userService.register(registerData));
    }
    @Test
    void changePassSuccessfully() {
        long userId = 1;
        ChangePassDTO changePassData = new ChangePassDTO();
        changePassData.setNewPassword("newPassword123");
        changePassData.setConfirmNewPassword("newPassword123");
        User u = new User();
        u.setId(userId);
        UserWithoutPasswordDTO expectedUser=new UserWithoutPasswordDTO();
        expectedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(u));
        when(passwordEncoder.matches(changePassData.getCurrentPassword(),u.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedPassword");
        when(userRepository.save(u)).thenReturn(u);
        when(mapper.map(u,UserWithoutPasswordDTO.class)).thenReturn(expectedUser);
        UserWithoutPasswordDTO result = userService.changePassword(changePassData, userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("encodedPassword", u.getPassword());
    }
    @Test
    void changePassInvalidUserId() {
        long userId = 1;
        String newPassword = "newPassword123";

        ChangePassDTO changePassData = new ChangePassDTO();
        changePassData.setNewPassword(newPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.changePassword(changePassData, userId));
    }
    @Test
    void editProfileSuccessfully() {
        long userId = 1;
        String newEmail = "newemail@example.com";

        EditProfileDTO editProfilDTO = new EditProfileDTO();
        editProfilDTO.setEmail(newEmail);
        editProfilDTO.setName(Util.NAME);

        User u = new User();
        u.setId(userId);
        u.setEmail(Util.EMAIL);
        u.setEnable(true);

        UserWithoutPasswordDTO expected = new UserWithoutPasswordDTO();
        expected.setId(userId);
        expected.setName(editProfilDTO.getName());
        expected.setEmail(editProfilDTO.getEmail());

        when(userRepository.existsByEmail(newEmail)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(u));
        when(mapper.map(u, UserWithoutPasswordDTO.class)).thenReturn(expected);

        UserWithoutPasswordDTO result = userService.editProfile(editProfilDTO, userId);

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    void deleteUserSuccessfully() {
        long userId = 1;
        String password = Util.PASS;

        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setPassword(password);

        User u = new User();
        u.setId(userId);
        u.setPassword(passwordEncoder.encode(password));

        when(userRepository.findById(userId)).thenReturn(Optional.of(u));
        when(passwordEncoder.matches(password, u.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> userService.delete(userPasswordDTO, userId));
    }
    @Test
    void deleteInvalidPass() {
        long userId = 1;
        String password = Util.PASS;

        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setPassword(password);

        User u = new User();
        u.setId(userId);
        u.setPassword(passwordEncoder.encode("wrongpassword"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(u));
        when(passwordEncoder.matches(password, u.getPassword())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> userService.delete(userPasswordDTO, userId));
    }
    @Test
    void deleteUserNotFound() {
        long userId = 1;
        String password = Util.PASS;

        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setPassword(password);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.delete(userPasswordDTO, userId));
    }
}


