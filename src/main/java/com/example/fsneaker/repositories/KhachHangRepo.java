package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface KhachHangRepo extends JpaRepository<KhachHang, Integer> {
    //Tìm kiếm khách hàng theo tên hoặc email hoăc số điện thoaị (Có chứa từ khóa tìm kiếm)
    //List<KhachHang> findByTenKhachHangContainingOrEmailContainingOrSoDienThoaiContaining(String tenKeyword, String emailKeyword, String sdtKeyword);
    //KhachHang findBySoDienThoai(String sdt);
    @Query("SELECT kh FROM KhachHang kh WHERE kh.tenKhachHang LIKE %:keyword% OR kh.soDienThoai LIKE %:keyword% OR kh.email LIKE %:keyword%")
    Page<KhachHang> searchByKhachHang(@Param("keyword") String keyword, Pageable pageable);

    Page<KhachHang> findAll(Pageable pageable);

    //Chỗ là code của trườởng nhóm code cấm đụng vào
    KhachHang findBySoDienThoaiOrEmail(String soDienThoai,String email);

    KhachHang findByEmail(String email);

    boolean existsByEmail(String email);
}
