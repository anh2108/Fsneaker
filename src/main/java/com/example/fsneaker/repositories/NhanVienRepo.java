package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NhanVienRepo extends JpaRepository<NhanVien, Integer> {
    @Query("SELECT nv FROM NhanVien nv WHERE nv.tenNhanVien LIKE %:keyword% OR nv.maNhanVien LIKE %:keyword%" )
    Page<NhanVien> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    NhanVien findByMaNhanVien(String maNhanVien);
}
