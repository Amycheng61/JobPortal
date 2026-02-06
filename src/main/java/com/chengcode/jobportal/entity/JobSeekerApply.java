package com.chengcode.jobportal.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames={"userId","job"})
})
public class JobSeekerApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userId",referencedColumnName = "user_account_id")
    private JobSeekerProfile userId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="job" ,referencedColumnName = "jobPostId")
    private JobPostActivity job;
    @DateTimeFormat(pattern="dd-MM-yyyy")
    private Date applyDate;
    private String coverLetter;

    public JobSeekerApply() {
    }

    public JobSeekerApply(Integer id, com.chengcode.jobportal.entity.JobSeekerProfile jobSeekerProfile, JobPostActivity job, Date applyDate, String coverLetter) {
        this.id = id;
        userId = jobSeekerProfile;
        this.job = job;
        this.applyDate = applyDate;
        this.coverLetter = coverLetter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.chengcode.jobportal.entity.JobSeekerProfile getUserId() {
        return userId;
    }

    public void setUserId(com.chengcode.jobportal.entity.JobSeekerProfile jobSeekerProfile) {
        userId = jobSeekerProfile;
    }

    public JobPostActivity getJob() {
        return job;
    }

    public void setJob(JobPostActivity job) {
        this.job = job;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    @Override
    public String toString() {
        return "JobSeekApply{" +
                "id=" + id +
                ", JobSeekerProfile=" + userId +
                ", job=" + job +
                ", applyDate=" + applyDate +
                ", coverLetter='" + coverLetter + '\'' +
                '}';
    }
}
