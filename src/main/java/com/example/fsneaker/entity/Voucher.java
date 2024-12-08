package com.example.fsneaker.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Voucher")

public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Size(max = 10, message = "Mã voucher không được dài quá 10 ký tự")
    @NotBlank(message = "MaVoucher không được bỏ trống")
    @Column(name = "MaVoucher")
    private String maVoucher;

    @NotBlank(message = "Tên Voucher không được bỏ trống")
    @Column(name = "TenVoucher")
    private String tenVoucher;

    @NotBlank(message = "LoaiVoucher không được bỏ trống")
    @Column(name = "LoaiVoucher")
    private String loaiVoucher;

    @Column(name = "MoTa")
    private String moTa;

    //    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    @NotNull(message = "SoLuong không được bỏ trống")
    @Column(name = "SoLuong")
    private Integer soLuong;

    @Positive(message = "Gia Tri phải là số dương")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá trị phải lớn hơn 0")
    @NotNull(message = "GiaTri không được bỏ trống")
    @Column(name = "GiaTri")
    private BigDecimal giaTri;

    @Positive(message = "Đơn tối thiểu phải là số dương")
    @NotNull(message = "DonToiThieu không được bỏ trống")
    @Column(name = "DonToiThieu")
    private BigDecimal donToiThieu;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    @NotNull(message = "NgayBatDau không được bỏ trống")
    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    @NotNull(message = "NgayKetThuc không được bỏ trống")
    @Column(name = "NgayKetThuc")
    private LocalDateTime ngayKetThuc;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @CreationTimestamp
    @Column(name = "NgayTao")
    private LocalDate ngayTao;

    @NotNull(message = "NhanVien không được bỏ trống")
    @ManyToOne
    @JoinColumn(name = "IdNhanVien")
    private NhanVien nhanVien;

    //    @NotNull(message = "Trang Thai không được bỏ trống")
    @Column(name = "TrangThai")
    private Integer trangThai;

    @AssertTrue(message = "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc")
    public boolean isNgayBatDauTruocNgayKetThuc() {
        return ngayBatDau != null && ngayKetThuc != null && !ngayBatDau.isAfter(ngayKetThuc);
    }

    @AssertTrue(message = "Đơn tối thiểu phải lớn hơn giá trị giảm")
    public boolean isDonToiThieuLonHonGiaTriGiam() {
        return donToiThieu != null && giaTri != null && donToiThieu.compareTo(giaTri) > 0;
    }

    public int getTrangThai() {
        LocalDateTime today = LocalDateTime.now();
        if (today.isBefore(ngayBatDau)) {
            return 2;
        } else if (today.isAfter(ngayKetThuc) || getSoLuong() == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
