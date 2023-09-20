package com.timelyserver.timelyserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.timelyserver.timelyserver.entity.UserDetail;

public interface UserRepository extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByEmail(String email);
    @Query(value = "SELECT * from user_detail u where u.id = ?1", nativeQuery = true)
    UserDetail findUser(Long id);
 

     @Query(value = "SELECT * from user_detail u where u.id in ?1", nativeQuery = true)
    List<UserDetail>  findAllByEmail(List<String> members);
   

}
