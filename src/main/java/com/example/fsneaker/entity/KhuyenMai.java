package com.example.fsneaker.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name="KhuyenMai")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    @NotBlank
    @Column(name="MaKhuyenMai")
    private String maKhuyenMai;

    @NotBlank
    @Column(name="TenKhuyenMai")
    private String tenKhuyenMai;

    @NotBlank
    @Column(name="LoaiKhuyenMai")
    private String loaiKhuyenMai;

    @NotBlank
    @Column(name="MoTa")
    private String moTa;

    @NotNull
    @Column(name="GiaTri")
    private float giaTri;


    @Temporal(TemporalType.DATE)
    @Column(name="NgayBatDau")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayBatDau;

    @Temporal(TemporalType.DATE)
    @Column(name="NgayKetThuc")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayKetThuc;

    @NotNull
    @Column(name="TrangThai")
    private int trangThai;

}
