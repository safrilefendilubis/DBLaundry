package com.juaracoding.DBLaundry.repo;

import com.juaracoding.DBLaundry.model.Demo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemoRepo extends JpaRepository<Demo,Long> {

    Page<Demo> findByIsDelete(Pageable page , byte byteIsDelete);
    List<Demo> findByIsDelete(byte byteIsDelete);
    Page<Demo> findByIsDeleteAndNamaDemoContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Demo> findByIsDeleteAndIdDemo(Pageable page , byte byteIsDelete, Long values);

}
