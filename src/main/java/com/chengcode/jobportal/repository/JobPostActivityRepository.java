package com.chengcode.jobportal.repository;

import com.chengcode.jobportal.entity.IRecruiterJobs;
import com.chengcode.jobportal.entity.JobPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface JobPostActivityRepository extends JpaRepository <JobPostActivity,Integer> {
    @Query(value = "select count(s.user_id) as totalCandidates,j.job_post_id,j.job_title,l.id as" +
            " locationId,l.city,l.state,l.country,c.id as companyId,c.name from job_post_activity j " +
            " inner join job_location l " +
            " on j.job_location_id=l.id " +
            " inner join job_company c " +
            " on j.job_company_id=c.id " +
            " left join job_seeker_apply s " +
            " on s.job=j.job_post_id " +
            " where  j.posted_by_id=:recruiter " +
            " group by j.job_post_id", nativeQuery = true)
    List<IRecruiterJobs> getRecruiterJobs(@Param("recruiter") int recruiter);

    @Query(value = "select * from job_post_activity j inner join job_location l " +
            " on j.job_location_id=l.id where " +
            " j.job_title like %:job% " +
            " and (l.city like %:location% " +
            " or l.country like %:location% " +
            " or l.state like %:location% ) " +
            " and (j.job_type in (:type)) " +
            " and (j.remote in (:remote))",nativeQuery = true)
    List<JobPostActivity> searchWithoutDate(@Param("job") String job,
                                            @Param("location") String location,
                                            @Param("remote") List<String> remote,
                                            @Param("type") List<String> type);
    @Query(value = "select * from job_post_activity j inner join job_location l " +
            " on j.job_location_id=l.id where " +
            " j.job_title like %:job% " +
            " and (l.city like %:location% " +
            " or l.country like %:location% " +
            " or l.state like %:location% ) " +
            " and (j.job_type in (:type)) " +
            " and (j.remote in (:remote)) " +
            " and (posted_date >=:searchDate)",nativeQuery = true)
    List<JobPostActivity> search(
            @Param("job") String job,
            @Param("location") String location,
            @Param("remote") List<String> remote,
            @Param("type") List<String> type,
            @Param("searchDate") LocalDate searchDate);
}
