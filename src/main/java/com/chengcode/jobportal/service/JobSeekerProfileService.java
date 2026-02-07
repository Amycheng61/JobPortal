package com.chengcode.jobportal.service;

import com.chengcode.jobportal.entity.JobSeekerProfile;
import com.chengcode.jobportal.entity.Users;
import com.chengcode.jobportal.repository.JobSeekerProfileRepository;
import com.chengcode.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UsersRepository usersRepository;
    @Autowired
    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UsersRepository usersRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<JobSeekerProfile> getOne(Integer id){
        return jobSeekerProfileRepository.findById(id);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile){
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
            Users users=usersRepository.findByEmail(currentUsername).orElseThrow(()->
                    new UsernameNotFoundException("user not found"));
            Optional<JobSeekerProfile> seekerProfile=getOne(users.getUserId());
            return seekerProfile.orElse(null);
        }
        return null;
    }
}
