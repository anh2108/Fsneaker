package com.example.fsneaker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "SanPhamChiTiet")

public class SanPhamChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "MaSanPhamChiTiet")
    private String maSanPhamChiTiet;

    @Column(name = "SoLuong")
    private int soLuong;

    @Column(name = "NgaySanXuat")
    private java.sql.Date ngaySanXuat;

    @ManyToOne
    @JoinColumn(name = "IdSanPham",referencedColumnName = "Id")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "IdKichThuoc",referencedColumnName = "Id")
    private KichThuoc kichThuoc;

    @ManyToOne
    @JoinColumn(name = "IdMauSac",referencedColumnName = "Id")
    private MauSac mauSac;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "TrangThai")
    private int trangThai;

    @Column(name = "Images")
    private String imanges;

    @Column(name = "GiaBan")
    private double giaBan;

    @Column(name = "GiaBanGiamGia")
    private Double giaBanGiamGia;

}
