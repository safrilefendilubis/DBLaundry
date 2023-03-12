package com.juaracoding.DBLaundry.repo;

import com.juaracoding.DBLaundry.model.Akses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AksesRepo extends JpaRepository<Akses,Long> {

    Page<Akses> findByIsDelete(Pageable page , byte byteIsDelete);
    Page<Akses> findByIsDeleteAndNamaAksesContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Akses> findByIsDeleteAndIdAkses(Pageable page , byte byteIsDelete, Long values);
}
