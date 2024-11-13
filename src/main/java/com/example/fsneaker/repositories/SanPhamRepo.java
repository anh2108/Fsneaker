package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepo extends JpaRepository<SanPham, Integer> {


    @Query("select sp, km.tenKhuyenMai, th.tenThuongHieu, xx.tenXuatXu from SanPham sp " +
            "join sp.khuyenMai km  " +
            "join sp.thuongHieu th " +
            "join sp.xuatXu xx " +
            "where sp.id = :id order by sp.ngayTao desc ")
    public SanPham getSanPhamsById(int id);

    // Tìm theo mã sản phẩm và tên sản phẩm
    @Query("select sp from SanPham sp where sp.maSanPham like %:serchSanPham% or sp.tenSanPham like %:serchSanPham%")
    public Page<SanPham> serchSanPhamByCodeOrName(String serchSanPham, Pageable pageable);

    // Lọc theo kích thước
    @Query(" select sp from SanPham sp " +
            "join sp.thuongHieu th " +
            "where th.id = :idThuongHieu")
    public Page<SanPham> searchByIdThuongHieu(int idThuongHieu, Pageable pageable);

    // Lọc theo xuất xứ
    @Query("select sp from SanPham sp " +
            "join sp.xuatXu xx " +
            "where xx.id = :idXuatXu")
    public Page<SanPham> searchByIdXuatXu(int idXuatXu, Pageable pageable);
 }
