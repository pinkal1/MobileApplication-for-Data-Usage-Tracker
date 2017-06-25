package com.springapp.mvc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by scsakhar on 4/24/2015.
 */
public interface UserQuotaRepository extends JpaRepository<UserQuota, Long> {
    public  UserQuota  findByPhoneNo(String phno);

}
