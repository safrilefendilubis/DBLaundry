package com.juaracoding.DBLaundry.model;
/*Created By IntelliJ IDEA 2022.3.2 (Ultimate Edition)
Build #IU-223.8617.56, built on January 26, 2023
@Author Syarifudin a.k.a. Muhamad Syarifuidn
Java Developer
Created on 01/03/2023 22:27
@Last Modified 01/03/2023 22:27
Version 1.1
*/

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "MstPengeluaran")
public class Pengeluaran {

    @Id
    @Column(name = "IDPengeluaran",unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPengeluaran;

    @Column(name = "NamaPengeluaran")
    private String namaPengeluaran;

    @Column(name = "Biaya")
    private String biaya;

    /*
     * Start Audit Trails
     * */
    @Column(name = "CreatedDate")
    private Date createdDate = new Date();

    @Column(name = "ModifiedBy")
    private byte modifiedBy = 1;

    @Column(name = "IsDelete")
    private byte isDelete = 1;

    @Column(name = "CreatedBy")
    private byte createdBy;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;
    /*
     * End Audit Trails
     * */

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public byte getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(byte modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(byte isDelete) {
        this.isDelete = isDelete;
    }

    public byte getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(byte createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}