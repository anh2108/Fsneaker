package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.KhuyenMai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhuyenMaiRepo extends JpaRepository<KhuyenMai,Integer> {
    // tìm kiếm theo mã khuyến mại hoặc tên khuyến mại
    @Query("SELECT k FROM KhuyenMai k WHERE k.tenKhuyenMai LIKE %:keyword% OR k.maKhuyenMai LIKE %:keyword%" )
    Page<KhuyenMai> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    KhuyenMai findByMaKhuyenMai(String maKhuyenMai);
 }
