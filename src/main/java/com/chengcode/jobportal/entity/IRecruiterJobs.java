package com.chengcode.jobportal.entity;

public interface IRecruiterJobs {
    Long getTotalCandidates();
    int getJob_post_id();
    String getJob_title();
    int getLocationId();
    String getCity();
    String getState();
    String getContry();
    int getCompanyId();
    String getName();

}
