package com.springapp.mvc;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by scsakhar on 4/28/2015.
 */
public interface AdminQuotaRepository extends JpaRepository <AdminQuota, Long> {
    public AdminQuota  findByAdminNo(String adminNo);
}
