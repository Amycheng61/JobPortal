package com.chengcode.jobportal.controller;


import com.chengcode.jobportal.entity.JobPostActivity;
import com.chengcode.jobportal.entity.RecruiterJobsDto;
import com.chengcode.jobportal.entity.RecruiterProfile;
import com.chengcode.jobportal.entity.Users;
import com.chengcode.jobportal.service.JobPostActivityService;
import com.chengcode.jobportal.service.UsersService;
import org.apache.tomcat.util.compat.Jre20Compat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

@Controller
public class JobPostActivityController {

    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;
    @Autowired
    public JobPostActivityController(UsersService usersService,JobPostActivityService jobPostActivityService) {
        this.usersService = usersService;
        this.jobPostActivityService=jobPostActivityService;
    }

    @GetMapping("/dashboard/")
    public String searchJobs(Model model){
        Object currentUserProfile=usersService.getCurrentUserProfile();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
            model.addAttribute("username",currentUsername);
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                List<RecruiterJobsDto> recruiterjobs = jobPostActivityService.getRecruiterJobs(((RecruiterProfile)currentUserProfile).getUserAccountId());
                model.addAttribute("jobPost",recruiterjobs);
            }
        }
        model.addAttribute("user",currentUserProfile);
        return "dashboard";
    }
    @GetMapping("/dashboard/add")
    public String addJobs(Model model){
        model.addAttribute("jobPostActivity",new JobPostActivity());
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "add-jobs";
    }
    @PostMapping("/dashboard/addNew")
    public String addNew(JobPostActivity jobPostActivity,Model model){
        Users user = (Users) usersService.getCurrentUser();
        if(user !=null){
            jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(new Date());
        model.addAttribute("jobPostActivity",jobPostActivity);
        JobPostActivity saved = jobPostActivityService.addNew(jobPostActivity);
        return "redirect:/dashboard/";
    }
}
