package com.timelyserver.timelyserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timelyserver.timelyserver.entity.Groups;

public interface GroupRepository extends JpaRepository<Groups,Long> {
    List<Groups> findByUserid(Long userid);
}
