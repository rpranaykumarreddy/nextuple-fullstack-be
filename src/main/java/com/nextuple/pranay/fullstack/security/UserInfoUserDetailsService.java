package com.nextuple.pranay.fullstack.security;

import com.nextuple.pranay.fullstack.model.Users;
import com.nextuple.pranay.fullstack.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Users userInfo= usersRepo.findByUsernameOrEmailIgnoreCase(usernameOrEmail,usernameOrEmail).orElseThrow(()->
                new UsernameNotFoundException("User not found with Username or Email" + usernameOrEmail)
        );
        Set<GrantedAuthority> authorityList= Arrays.stream(userInfo.getRoles()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                usernameOrEmail,userInfo.getPassword(),authorityList
        );
    }

}
