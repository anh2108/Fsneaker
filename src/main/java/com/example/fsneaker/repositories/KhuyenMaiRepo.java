package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.KhuyenMai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface KhuyenMaiRepo extends JpaRepository<KhuyenMai,Integer> {
    // tìm kiếm theo  tên khuyến mại
    @Query("SELECT k FROM KhuyenMai k WHERE " +
            "(:keyword IS NULL OR k.tenKhuyenMai LIKE %:keyword% OR k.maKhuyenMai LIKE %:keyword% OR k.loaiKhuyenMai LIKE %:keyword%) " +
            "OR (CAST(k.ngayBatDau AS string) LIKE %:keyword% OR CAST(k.ngayKetThuc AS string) LIKE %:keyword%) " +
            "OR (:date IS NOT NULL AND (k.ngayBatDau = :date OR k.ngayKetThuc = :date))")
    Page<KhuyenMai> searchByKeywordAndDate(@Param("keyword") String keyword,
                                           @Param("date") LocalDate date,
                                           Pageable pageable);

   KhuyenMai findByMaKhuyenMai(String maKhuyenMai);

    // Truy vấn để sắp xếp theo trạng thái trước (Hoạt động lên đầu)
    @Query("SELECT k FROM KhuyenMai k ORDER BY k.trangThai DESC, k.id ASC")
    Page<KhuyenMai> findAllWithSorting(Pageable pageable);
   
 }
