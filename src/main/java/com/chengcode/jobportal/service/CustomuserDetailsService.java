package com.chengcode.jobportal.service;

import com.chengcode.jobportal.entity.Users;
import com.chengcode.jobportal.repository.UsersRepository;
import com.chengcode.jobportal.util.CustomerUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomuserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    @Autowired
    public CustomuserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Users users=usersRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Could not found user"));
        return new CustomerUserDetails(users);
    }
}
