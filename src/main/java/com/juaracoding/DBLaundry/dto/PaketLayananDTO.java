package com.juaracoding.DBLaundry.dto;

public class PaketLayananDTO {

    private Long idListHarga;

    private String namaPaket;

    private Double hargaPerKilo;

    private String tipeLayanan;//komponen select di

    public Long getIdListHarga() {
        return idListHarga;
    }

    public void setIdListHarga(Long idListHarga) {
        this.idListHarga = idListHarga;
    }

    public String getNamaPaket() {
        return namaPaket;
    }

    public void setNamaPaket(String namaPaket) {
        this.namaPaket = namaPaket;
    }

    public Double getHargaPerKilo() {
        return hargaPerKilo;
    }

    public void setHargaPerKilo(Double hargaPerKilo) {
        this.hargaPerKilo = hargaPerKilo;
    }

    public String getTipeLayanan() {
        return tipeLayanan;
    }

    public void setTipeLayanan(String tipeLayanan) {
        this.tipeLayanan = tipeLayanan;
    }
}
