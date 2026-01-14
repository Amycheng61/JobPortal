package com.chengcode.jobportal.service;

import com.chengcode.jobportal.entity.JobSeekerProfile;
import com.chengcode.jobportal.entity.RecruiterProfile;
import com.chengcode.jobportal.entity.Users;
import com.chengcode.jobportal.repository.JobSeekerProfileRepository;
import com.chengcode.jobportal.repository.RecruiterProfileRepository;
import com.chengcode.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UsersService(UsersRepository usersRepository,
                        RecruiterProfileRepository recruiterProfileRepository,
                        JobSeekerProfileRepository jobSeekerProfileRepository,
                        PasswordEncoder passwordEncoder){
        this.usersRepository=usersRepository;
        this.recruiterProfileRepository=recruiterProfileRepository;
        this.jobSeekerProfileRepository=jobSeekerProfileRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public Users addNew(Users users){
        users.setActive(true);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        Users savedUsers=usersRepository.save(users);
        int userTypeId=savedUsers.getuserTypeId().getUserTypeId();
        if(userTypeId==1){
            recruiterProfileRepository.save(new RecruiterProfile(savedUsers));
        }else {
            jobSeekerProfileRepository.save(new JobSeekerProfile(savedUsers));
        }
        return savedUsers;
    }

    public Optional<Users> getUserByEmail(String email){
        return usersRepository.findByEmail(email);
    }

    public Object getCurrentUserProfile(){
        System.out.println("in userService, getCurrentUserProfile()-------------");
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username=authentication.getName();
            Users users=usersRepository.findByEmail(username).orElseThrow(()->new
                    UsernameNotFoundException("Could not found "+ "user"));
            int userId=users.getUserId();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                RecruiterProfile recruiterProfile=recruiterProfileRepository.
                        findById(userId).orElse(new RecruiterProfile());
                return recruiterProfile;
            }else{
                JobSeekerProfile jobSeekerProfile=jobSeekerProfileRepository.
                        findById(userId).orElse(new JobSeekerProfile());
                return jobSeekerProfile;
            }

        }
        return null;
    }
}
