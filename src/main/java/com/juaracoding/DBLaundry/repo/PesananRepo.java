package com.juaracoding.DBLaundry.repo;

import com.juaracoding.DBLaundry.model.Pesanan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PesananRepo extends JpaRepository<Pesanan,Long> {
    Page<Pesanan> findByIsDelete(Pageable page , byte byteIsDelete);

    List<Pesanan> findByIsDelete(byte byteIsDelete);
    Page<Pesanan> findByIsDeleteAndIdPesanan(Pageable page , byte byteIsDelete, Long values);


    List<Pesanan> findByIsDeleteAndPelangganNamaLengkapContainsIgnoreCase(byte byteIsDelete, String values);
    List<Pesanan> findByIsDeleteAndIdPesanan(byte byteIsDelete, Long values);



}
