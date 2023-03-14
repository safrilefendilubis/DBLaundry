package com.juaracoding.DBLaundry.dto;/*
IntelliJ IDEA 2022.3.2 (Ultimate Edition)
Build #IU-223.8617.56, built on January 26, 2023
@Author User a.k.a. Safril Efendi Lubis
Java Developer
Created on 14/03/2023 12:58
@Last Modified 14/03/2023 12:58
Version 1.1
*/

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TipeDTO {

    private Long idTipe;

    @NotNull
    @NotEmpty
    private String namaTipe;

    public Long getIdTipe() {
        return idTipe;
    }

    public void setIdTipe(Long idTipe) {
        this.idTipe = idTipe;
    }

    public String getNamaTipe() {
        return namaTipe;
    }

    public void setNamaTipe(String namaTipe) {
        this.namaTipe = namaTipe;
    }
}
