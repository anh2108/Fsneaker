package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.DonHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonHangRepo extends JpaRepository<DonHang, Integer> {
    List<DonHang> findByTrangThai(String trangThai);

    //    DonHang findById(int id);
    //Đếm số đơn hàng đã thanh toán
    Long countByTrangThaiEquals(String trangThai);

    List<DonHang> findByTrangThaiEquals(String trangThai);
    //Lấy tất cả các đơn hàng đã thanh toán trong vòng 1 tuần qua
    List<DonHang> findByNgayMuaBetweenAndTrangThaiEquals(LocalDate startDate, LocalDate endDate, String trangThai);

    @Query("SELECT dh.trangThai, COUNT(dh) FROM DonHang dh GROUP BY dh.trangThai")
    List<Object[]> demDonHangTheoTrangThai();
    //Lấy tổng thu nhập trong tháng hiện tại
    @Query("SELECT FUNCTION('DAY',dh.ngayMua), SUM(COALESCE(dh.tongTienGiamGia,dh.tongTien)) FROM DonHang dh WHERE MONTH(dh.ngayMua) = MONTH(CURRENT_DATE ) AND YEAR(dh.ngayMua) = YEAR(CURRENT_DATE ) GROUP BY FUNCTION('DAY',dh.ngayMua)")
    List<Object[]> findThuNhapTheoThang();
    //Lấy số lượng đơn hàng trong tháng hiện tại
    @Query("SELECT FUNCTION('DAY',dh.ngayMua), COUNT(dh) FROM DonHang dh WHERE MONTH(dh.ngayMua) = MONTH(CURRENT_DATE ) AND YEAR(dh.ngayMua) = YEAR(CURRENT_DATE) GROUP BY FUNCTION('DAY',dh.ngayMua)")
    List<Object[]> findDonHangTheoThang();
    //Lấy tổng thu nhập trong năm hiện tại
    @Query("SELECT FUNCTION('MONTH',dh.ngayMua), SUM(COALESCE(dh.tongTienGiamGia,dh.tongTien)) FROM DonHang dh WHERE YEAR(dh.ngayMua) = YEAR(CURRENT_DATE ) GROUP BY FUNCTION('MONTH',dh.ngayMua)")
    List<Object[]> findThuNhapTheoNam();
    //Lấy số lượng đơn hàng trong năm hiện tại
    @Query("SELECT FUNCTION('MONTH',dh.ngayMua), COUNT(dh) FROM DonHang dh WHERE YEAR(dh.ngayMua) = YEAR(CURRENT_DATE) GROUP BY FUNCTION('MONTH',dh.ngayMua)")
    List<Object[]> findDonHangTheoNam();
    @Query("SELECT dh.khachHang.tenKhachHang,dh.khachHang.email,dh.khachHang.soDienThoai, count(dh.id) as soLanMua,SUM(COALESCE(dh.tongTienGiamGia,dh.tongTien)) as tongTienMua from DonHang dh WHERE dh.ngayMua BETWEEN :startDate AND :endDate GROUP BY dh.khachHang.tenKhachHang ,dh.khachHang.email,dh.khachHang.soDienThoai ORDER BY soLanMua DESC")
    Page<Object[]> thongKeKhachHangTheoSoLanMua(@Param("startDate") LocalDate startDate, @Param("endDate")LocalDate endDate, Pageable pageable);
    @Query("SELECT dh.khachHang.tenKhachHang,dh.khachHang.email,dh.khachHang.soDienThoai, count(dh.id) as soLanMua,SUM(COALESCE(dh.tongTienGiamGia,dh.tongTien)) as tongTienMua from DonHang dh WHERE dh.ngayMua BETWEEN :startDate AND :endDate GROUP BY dh.khachHang.tenKhachHang ,dh.khachHang.email,dh.khachHang.soDienThoai ORDER BY tongTienMua DESC")
    Page<Object[]> thongKeKhachHangTheoTongTien(@Param("startDate") LocalDate startDate, @Param("endDate")LocalDate endDate, Pageable pageable);
    @Query("SELECT dh.khachHang.tenKhachHang,dh.khachHang.email,dh.khachHang.soDienThoai, count(dh.id) as soLanMua,SUM(COALESCE(dh.tongTienGiamGia,dh.tongTien)) as tongTienMua from DonHang dh WHERE dh.trangThai = :trangThai GROUP BY dh.khachHang.tenKhachHang ,dh.khachHang.email,dh.khachHang.soDienThoai ORDER BY soLanMua DESC")
    Page<Object[]> thongKeKHTheoSoLanMua(@Param("trangThai")String trangThai, Pageable pageable);
    @Query("SELECT dh.khachHang.tenKhachHang,dh.khachHang.email,dh.khachHang.soDienThoai, count(dh.id) as soLanMua,SUM(COALESCE(dh.tongTienGiamGia,dh.tongTien)) as tongTienMua from DonHang dh WHERE dh.trangThai = :trangThai GROUP BY dh.khachHang.tenKhachHang ,dh.khachHang.email,dh.khachHang.soDienThoai ORDER BY tongTienMua DESC")
    Page<Object[]> thongKeKHTheoTongTien(@Param("trangThai")String trangthai, Pageable pageable);
    DonHang findByMaDonHang(String maDonHang);

    //Tính tổng thu nhập trong khoảng thời gian
    @Query("SELECT SUM(coalesce(d.tongTienGiamGia,d.tongTien)) FROM DonHang d WHERE d.ngayMua BETWEEN :startDate AND :endDate")
    Double tinhTongThuNhap(@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate);

    //Đếm số lượng khách hàng duy nhất trong khoảng thời gian
    @Query("SELECT COUNT(distinct d.khachHang.id) FROM DonHang d WHERE d.ngayMua BETWEEN :startDate AND :endDate")
    Long tinhTongKhachHang(@Param("startDate")LocalDate startDate,LocalDate endDate);

    //Đếm số đơn hàng trong khoảng thời gian
    @Query("SELECT COUNT(d.id) FROM DonHang d WHERE d.ngayMua between :startDate AND :endDate")
    Long tinhTongDonHang(@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate);

    //Tính tổng số sản phẩm đã bán trong khoảng thời gian
    @Query("SELECT SUM(chiTiet.soLuong) FROM DonHang d JOIN d.donHangChiTiets chiTiet WHERE d.ngayMua BETWEEN :startDate AND :endDate")
    Long tinhTongSanPhamDaBan(@Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate);
}
