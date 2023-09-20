package com.timelyserver.timelyserver.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.mapping.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.timelyserver.timelyserver.entity.Activity;
import com.timelyserver.timelyserver.entity.TeamTask;
import com.timelyserver.timelyserver.entity.UserDetail;
import com.timelyserver.timelyserver.repository.ActivityRepository;
import com.timelyserver.timelyserver.repository.TeamTaskRepository;
import com.timelyserver.timelyserver.repository.UserRepository;

@RestController
@CrossOrigin(origins = "*")
public class TeamTaskController {
    @Autowired
    private TeamTaskRepository teamTaskRepository;

   

    @GetMapping("/api/team/{email}")
    List<TeamTask> getTeamActivity(@PathVariable String email) {
                System.out.println(email);
                List<TeamTask> taskList = teamTaskRepository.findByAssignedby(email);
            System.out.println(taskList.get(0).toString());
            return teamTaskRepository.findByAssignedby(email);


       
    //    List<TeamTask> arr = teamTaskRepository.findByAssignedby(assignedby);
    //     HashMap<String,Any> result = new HashMap<String,Any>();
    //     arr.forEach(task -> {
    //         List<String> members = new ArrayList<>();
    //         List<String> status = new ArrayList<>();
    //         List<UserDetail> users = userRepository.findAllById(task.getUserid());
    //         users.forEach(user -> {
    //             members.add(user.getEmail());
    //         });
    //         List<Activity> activities = teamTaskRepository.findStatus(task.getAssignedby(), task.getDate());
    //         activities.forEach(activity -> {
    //             if(activity.getStatus() == false){
    //                 status.add("Not Completed");
    //             } else {
    //                 status.add("Completed");
    //             }
               
    //         });
        
    //     } );

    //     return {}

        // return activityRepository.findUncompleteActivities(id);
    }

}
