package com.juaracoding.DBLaundry.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MstPaketLayanan")
public class PaketLayanan {

    @Id
    @Column(name = "IDListHarga")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idListHarga;

    @Column(name = "NamaPaket")
    private String namaPaket;

    @Column(name = "Harga")
    private Double hargaPerKilo;

    @Column(name = "TipeLayanan")
    private String tipeLayanan;//komponen select di

    @Column(name ="CreatedDate" , nullable = false)
    private Date createdDate = new Date();

    @Column(name = "CreatedBy", nullable = false)
    private Integer createdBy=1;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;
    @Column(name = "ModifiedBy")
    private Integer modifiedBy;

    @Column(name = "IsDelete", nullable = false)
    private Byte isDelete = 1;

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}
