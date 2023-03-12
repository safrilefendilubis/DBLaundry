package com.juaracoding.DBLaundry.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TrxPesanan")
public class Pesanan {


    @Id
    @Column(name = "IDPesanan")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPesanan;

    @ManyToOne
    @JoinColumn(name = "IDListHarga")
    private PaketLayanan paketLayanan;

    @ManyToOne
    @JoinColumn(name = "IDPelanggan")
    private Pelanggan pelanggan;

    @ManyToOne
    @JoinColumn(name = "IDPembayaran")
    private Pembayaran pembayaran;

    private Double berat;

    @Transient
    private Double totalHarga;

    public Double getBerat() {
        return berat;
    }

    public void setBerat(Double berat) {
        this.berat = berat;
    }

    public Double getTotalHarga() {
        return berat*paketLayanan.getHargaPerKilo();
    }

    public void setTotalHarga(Double totalHarga) {
        this.totalHarga = totalHarga;
    }

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
    /*
        end audit trails
     */

    public Long getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(Long idPesanan) {
        this.idPesanan = idPesanan;
    }

    public PaketLayanan getPaketLayanan() {
        return paketLayanan;
    }

    public void setPaketLayanan(PaketLayanan paketLayanan) {
        this.paketLayanan = paketLayanan;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(Pelanggan pelanggan) {
        this.pelanggan = pelanggan;
    }

    public Pembayaran getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
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
