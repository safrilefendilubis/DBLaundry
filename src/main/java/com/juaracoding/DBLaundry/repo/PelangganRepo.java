package com.juaracoding.DBLaundry.repo;

import com.juaracoding.DBLaundry.model.Pelanggan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PelangganRepo extends JpaRepository<Pelanggan,Long> {

    Page<Pelanggan> findByIsDelete(Pageable page , byte byteIsDelete);

    List<Pelanggan> findByIsDelete(byte byteIsDelete);
    Page<Pelanggan> findByIsDeleteAndNamaLengkapContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Pelanggan> findByIsDeleteAndIdPelanggan(Pageable page , byte byteIsDelete, Long values);
}
