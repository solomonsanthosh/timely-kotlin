package com.timelyserver.timelyserver.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.timelyserver.timelyserver.entity.Groups;
import com.timelyserver.timelyserver.repository.GroupRepository;

@RestController
public class GroupController {
    @Autowired
    GroupRepository groupRepository;
    

    @GetMapping("/api/group/{id}")
    List<Groups> getGroup(@PathVariable Long id){

        return groupRepository.findByUserid(id);

    }

    @PostMapping("/api/group")
    Groups postGroup(@RequestBody Groups group){
        System.out.println(group.getTitle().toString());
        System.out.println(group.getMembers().toString());
        System.out.println(group.getUserid().toString());
        return groupRepository.save(group);

    }
}
