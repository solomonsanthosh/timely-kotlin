package com.timelyserver.timelyserver.repository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.timelyserver.timelyserver.entity.TeamTask;

public interface TeamTaskRepository extends JpaRepository<TeamTask,Long>{
     List<TeamTask> findByAssignedby(String assignedby);


     @Query(value="Select * from team_task t where t.assignedby = ?1 AND t.date = ?2 AND ?3 IN (select unnest(members) from team_task t where t.assignedby = ?1 AND t.date = ?2)",nativeQuery = true)
     Optional<TeamTask> findTeamTask(String assignedby,Date date,String email);

}
