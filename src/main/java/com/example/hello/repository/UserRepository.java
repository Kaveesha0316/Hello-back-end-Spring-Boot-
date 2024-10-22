package com.example.hello.repository;

import com.example.hello.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

//    List<User> findAllByFirstnameAndLastname(String firstname, String lastname);
//
//    User findTopByOrderByIdDesc();
//    List<User> findAll(Sort sort);

    //    void deleteByFirstname(String firstname);
    Optional<User> findByMobile(String mobile);

    Optional<User> findByMobileAndPassword(String mobile, String password);

    List<User> findAllByIdNotAndFirstnameStartingWith(int id, String firstname);
}
