package com.juaracoding.DBLaundry.repo;


import com.juaracoding.DBLaundry.model.MenuHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuHeaderRepo extends JpaRepository<MenuHeader,Long> {

    Page<MenuHeader> findByIsDelete(Pageable page , byte byteIsDelete);
    List<MenuHeader> findByIsDelete(byte byteIsDelete);
    Page<MenuHeader> findByIsDeleteAndIdMenuHeader(Pageable pageable, Byte isDelete, Long valueparamValue);
    Page<MenuHeader> findByIsDeleteAndNamaMenuHeader(Pageable pageable,Byte isDelete,String valueparamValue);
    Page<MenuHeader> findByIsDeleteAndDeskripsiMenuHeader(Pageable pageable,Byte isDelete,String valueparamValue);

}
