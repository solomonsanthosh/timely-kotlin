package com.timelyserver.timelyserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.timelyserver.timelyserver.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    
    List<Activity> findByUserid(Long userid);

    @Query(value = "SELECT * from activity A where A.userid[1] = ?1 and A.status = 'Not Completed' order by (case when A.pin then 1  else 3 end) asc", nativeQuery = true)
    List<Activity> findUncompleteActivities(Long userid);

    @Query(value = "SELECT * from activity A where A.userid[1] = ?1", nativeQuery = true)
    List<Activity> findAllByUserId(Long userid);
    
    @Query(value = "SELECT * from activity A where A.userid[1] = ?1", nativeQuery = true)
    Activity findActivity(Long id);

    @Query(value = "SELECT COUNT(pin) from activity A where A.userid[1] = ?1 AND pin = true", nativeQuery = true)
    Integer checkPinned(Long id);
    
}
