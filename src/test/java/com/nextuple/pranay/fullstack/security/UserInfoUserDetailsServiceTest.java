package com.nextuple.pranay.fullstack.security;

import com.nextuple.pranay.fullstack.model.Users;
import com.nextuple.pranay.fullstack.repo.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserInfoUserDetailsServiceTest {

    @Mock
    private UsersRepo usersRepo;

    @InjectMocks
    private UserInfoUserDetailsService userInfoUserDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername() {
        String usernameOrEmail = "username";
        Users user = new Users();
        user.setUsername(usernameOrEmail);
        user.setPassword("password");
        user.setRoles("ROLE_USER");

        when(usersRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)).thenReturn(Optional.of(user));

        UserDetails userDetails = userInfoUserDetailsService.loadUserByUsername(usernameOrEmail);
        assertEquals(usernameOrEmail, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(usersRepo, times(1)).findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        String usernameOrEmail = "username";

        when(usersRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userInfoUserDetailsService.loadUserByUsername(usernameOrEmail));

        verify(usersRepo, times(1)).findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }
}
