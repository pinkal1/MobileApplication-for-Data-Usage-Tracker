package com.springapp.mvc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by PinkalJay on 5/5/15.
 */

public interface DataUsageDumyRepository extends JpaRepository<DataUsageDumy, Long>{
    public List<DataUsageDumy> findByPhoneNo(String phno);
    public List<DataUsageDumy> findByPhoneNoAndUsageDateBetween(String phno,Date date1, Date date2);
    //public List<DataUsageDumy>
}
