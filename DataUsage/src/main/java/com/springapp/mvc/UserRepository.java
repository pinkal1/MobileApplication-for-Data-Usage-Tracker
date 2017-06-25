package com.springapp.mvc;

/**
 * Created by PinkalJay on 4/6/15.
 */
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by scsakhar on 4/1/2015.
 */
public interface UserRepository extends JpaRepository<User , Long>{
    List<User> findByAdminNo(String adminNo);
    User findByPhoneNo(String phone);
    // List<User>  findExis tsAdminNo(String adminNo);
    User findByEmailId(String email);

}
