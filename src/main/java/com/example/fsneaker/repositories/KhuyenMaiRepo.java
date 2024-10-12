package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhuyenMaiRepo extends JpaRepository<KhuyenMai,Integer> {
    // tìm kiếm theo mã khuyến mại hoặc tên khuyến mại
    List<KhuyenMai> findByMaKhuyenMaiContainingIgnoreCaseOrTenKhuyenMaiContainingIgnoreCase(String maKhuyenMaiKeyword, String tenKhuyenMaiKeyword);

    KhuyenMai findByMaKhuyenMai(String maKhuyenMai);
 }
