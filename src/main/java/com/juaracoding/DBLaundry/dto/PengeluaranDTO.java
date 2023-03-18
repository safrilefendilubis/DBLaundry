package com.juaracoding.DBLaundry.dto;/*
Created By IntelliJ IDEA 2022.3.2 (Ultimate Edition)
Build #IU-223.8617.56, built on January 26, 2023
@Author Syarifudin a.k.a. Muhamad Syarifuidn
Java Developer
Created on 10/03/2023 15:39
@Last Modified 10/03/2023 15:39
Version 1.1
*/

public class PengeluaranDTO {

    private Long idPengeluaran;

    private String namaPengeluaran;

    private String biaya;

    public Long getIdPengeluaran() {
        return idPengeluaran;
    }

    public void setIdPengeluaran(Long idPengeluaran) {
        this.idPengeluaran = idPengeluaran;
    }

    public String getNamaPengeluaran() {
        return namaPengeluaran;
    }

    public void setNamaPengeluaran(String namaPengeluaran) {
        this.namaPengeluaran = namaPengeluaran;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }
}
