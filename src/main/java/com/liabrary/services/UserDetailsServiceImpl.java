package com.liabrary.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.liabrary.dao.UserRepository;
import com.liabrary.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService {
	
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(email);
        if (user == null) throw new UsernameNotFoundException("user is not there in database");
        
        CustomUserDetails customUserDetails=new CustomUserDetails(user);
        return customUserDetails;


    }

}
