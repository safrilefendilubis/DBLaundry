package com.juaracoding.DBLaundry.repo;/*
IntelliJ IDEA 2022.3.2 (Ultimate Edition)
Build #IU-223.8617.56, built on January 26, 2023
@Author User a.k.a. Safril Efendi Lubis
Java Developer
Created on 14/03/2023 12:52
@Last Modified 14/03/2023 12:52
Version 1.1
*/

import com.juaracoding.DBLaundry.model.Divisi;
import com.juaracoding.DBLaundry.model.Tipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipeRepo extends JpaRepository<Tipe,Long> {

    Page<Tipe> findByIsDelete(Pageable page , byte byteIsDelete);

    List<Tipe> findByIsDelete(byte byteIsDelete);
    Page<Tipe> findByIsDeleteAndNamaTipeContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Tipe> findByIsDeleteAndIdTipe(Pageable page , byte byteIsDelete, Long values);
}
