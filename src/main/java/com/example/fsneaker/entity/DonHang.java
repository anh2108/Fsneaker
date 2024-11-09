package com.example.fsneaker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String maDonHang;

    @ManyToOne
    @JoinColumn(name = "idNhanVien")
    private NhanVien nhanVien;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idKhachHang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "idGiamGia")
    private Voucher giamGia;
    private LocalDate ngayMua;
    private LocalDate ngayTao;
    private String loaiDonHang;
    private String trangThai;
    private double tongTien;
    private Double tongTienGiamGia;
    @OneToMany(mappedBy = "donHang")
    private List<DonHangChiTiet> donHangChiTiets;
}
