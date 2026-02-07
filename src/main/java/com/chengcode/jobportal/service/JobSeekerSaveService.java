package com.chengcode.jobportal.service;

import com.chengcode.jobportal.entity.JobPostActivity;
import com.chengcode.jobportal.entity.JobSeekerProfile;
import com.chengcode.jobportal.entity.JobSeekerSave;
import com.chengcode.jobportal.repository.JobSeekerSaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {
    private final JobSeekerSaveRepository jobSeekerSaveRepository;
    @Autowired
    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
    }

    public List<JobSeekerSave> getCandidatesJobByProfile(JobSeekerProfile jobSeekerProfile){
        return jobSeekerSaveRepository.findByUserId(jobSeekerProfile);

    }

    public List<JobSeekerSave> getCandidatesByJob(JobPostActivity job){
        return jobSeekerSaveRepository.findByJob(job);
    }

    public void addNew(JobSeekerSave jobSeekerSave) {
        jobSeekerSaveRepository.save(jobSeekerSave);
    }
}
