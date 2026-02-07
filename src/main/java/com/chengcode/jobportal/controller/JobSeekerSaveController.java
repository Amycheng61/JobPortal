package com.chengcode.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.*;
import com.chengcode.jobportal.entity.JobPostActivity;
import com.chengcode.jobportal.entity.JobSeekerProfile;
import com.chengcode.jobportal.entity.JobSeekerSave;
import com.chengcode.jobportal.entity.Users;
import com.chengcode.jobportal.service.JobPostActivityService;
import com.chengcode.jobportal.service.JobSeekerProfileService;
import com.chengcode.jobportal.service.JobSeekerSaveService;
import com.chengcode.jobportal.service.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerSaveController {
    private final UsersService usersService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerSaveService jobSeekerSaveService;
    @Autowired
    public JobSeekerSaveController(UsersService usersService, JobSeekerProfileService jobSeekerProfileService, JobPostActivityService jobPostActivityService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }
    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id") int id){
        JobSeekerSave jobSeekerSave =new JobSeekerSave();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername= authentication.getName();
            Users users=usersService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> seekerProfile=jobSeekerProfileService.getOne(users.getUserId());
            JobPostActivity jobPostActivity=jobPostActivityService.getOne(id);
            System.out.println("in jobSeekerSaveController id:"+id+";getUserId:"+users.getUserId());
            if(seekerProfile.isPresent()&&jobPostActivity!=null){
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUserId(seekerProfile.get());
            }else {
                throw new RuntimeException("User not found");
            }
            jobSeekerSaveService.addNew(jobSeekerSave);
        }
        return "redirect:/dashboard/";
    }
    @GetMapping("saved-jobs/")
    public String SavedJobs(Model model){
        List<JobPostActivity> jobPost=new ArrayList<>();
        Object currentProfile=usersService.getCurrentUserProfile();
        List<JobSeekerSave> jobSeekerSaveList=jobSeekerSaveService.
                getCandidatesJobByProfile((JobSeekerProfile) currentProfile);
        for(JobSeekerSave jobSeekerSave:jobSeekerSaveList){
            jobPost.add(jobSeekerSave.getJob());
        }
        model.addAttribute("jobPost",jobPost);
        model.addAttribute("user",currentProfile);

        return "saved-jobs";
    }
}
