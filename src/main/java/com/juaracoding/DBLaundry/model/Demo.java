package com.juaracoding.DBLaundry.model;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "MstDemo")
public class Demo {


    @Id
    @Column(name = "IdDemo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDemo;


    @Column(name = "NamaDemo")
    private String namaDemo;


    @Column(name ="CreatedDate" , nullable = false)
    private Date createdDate = new Date();

    @Column(name = "CreatedBy", nullable = false)
    private Integer createdBy=1;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;
    @Column(name = "ModifiedBy")
    private Integer modifiedBy;

    @Column(name = "IsDelete", nullable = false)
    private Byte isDelete = 1;//khusus disini default 0 karena setelah verifikasi baru di update menjadi 1

    public Long getIdDemo() {
        return idDemo;
    }

    public void setIdDemo(Long idDemo) {
        this.idDemo = idDemo;
    }

    public String getNamaDemo() {
        return namaDemo;
    }

    public void setNamaDemo(String namaDemo) {
        this.namaDemo = namaDemo;
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
