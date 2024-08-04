package com.musala.services.booking.services.Impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.musala.services.booking.Enums.UserCategories;
import com.musala.services.booking.models.User;
import com.musala.services.booking.repository.UserDao;
import com.musala.services.booking.services.CustomUserDetailsService;

import jakarta.transaction.Transactional;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByEmail(username);
        if(user == null) {
           throw new UsernameNotFoundException("User not found with username: " + username);
        }

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(user.getRole().equals(UserCategories.Admin.toString())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
        true, true, true, true, authorities);
    }
}
