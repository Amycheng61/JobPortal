package com.chengcode.jobportal.controller;

import ch.qos.logback.core.util.StringUtil;
import com.chengcode.jobportal.entity.RecruiterProfile;
import com.chengcode.jobportal.entity.Users;
import com.chengcode.jobportal.repository.UsersRepository;
import com.chengcode.jobportal.service.RecruiterProfileService;
import com.chengcode.jobportal.service.UsersService;
import com.chengcode.jobportal.util.FileUploadUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final UsersService usersService;
    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(UsersService usersService,RecruiterProfileService recruiterProfileService) {
        this.usersService = usersService;
        this.recruiterProfileService = recruiterProfileService;
    }
    @GetMapping("/")
    public String recruiterProfile(Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentname = authentication.getName();
            Users users=  usersService.getUserByEmail(currentname).orElseThrow(()->
                    new UsernameNotFoundException("Could not found user") );
            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getUserId());
            if(!recruiterProfile.isEmpty()){
                model.addAttribute("profile", recruiterProfile.get());
            }
        }
        System.out.println();
        return "recruiter_profile";
    }
    @PostMapping("/addNew")
    public String addNew(RecruiterProfile recruiterProfile, @RequestParam("image")MultipartFile multipartFile,Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername =authentication.getName();
            Users users=usersService.getUserByEmail(currentUsername).orElseThrow(()->
                    new UsernameNotFoundException("Could not found user"));
            recruiterProfile.setUserId(users);
            recruiterProfile.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile",recruiterProfile);
        String filename="";
        if(!multipartFile.getOriginalFilename().equals("")){
            filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(filename);
        }
        RecruiterProfile savedUser = recruiterProfileService.addNew(recruiterProfile);
        String uploadDir = "photos/recuiter/"+savedUser.getUserAccountId();
        try {
            FileUploadUtil.saveFile(uploadDir,filename,multipartFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/dashboard/";
    }
}
