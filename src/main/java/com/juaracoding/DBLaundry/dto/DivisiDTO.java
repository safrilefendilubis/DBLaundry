package com.juaracoding.DBLaundry.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DivisiDTO {


    private Long idDivisi;

    @NotNull
    @NotEmpty
    private String namaDivisi;

    @NotNull
    @NotEmpty
    private String kodeDivisi;

    public Long getIdDivisi() {
        return idDivisi;
    }

    public void setIdDivisi(Long idDivisi) {
        this.idDivisi = idDivisi;
    }

    public String getNamaDivisi() {
        return namaDivisi;
    }

    public void setNamaDivisi(String namaDivisi) {
        this.namaDivisi = namaDivisi;
    }

    public String getKodeDivisi() {
        return kodeDivisi;
    }

    public void setKodeDivisi(String kodeDivisi) {
        this.kodeDivisi = kodeDivisi;
    }
}
