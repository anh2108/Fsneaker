package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.DonHang;
import com.example.fsneaker.entity.DonHangChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface DonHangChiTietRepo extends JpaRepository<DonHangChiTiet, Integer> {
    //Chô này là code của trưởng nhóm câm đụng
    //Truy vấn số lượng sản phẩm bán được theo thương hiệu
    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan, SUM(dhct.soLuong) as totalQuantity FROM DonHangChiTiet dhct JOIN SanPhamChiTiet spct ON dhct.sanPhamChiTiet.id = spct.id JOIN SanPham sp ON spct.id = sp.id JOIN ThuongHieu th ON sp.thuongHieu.id = th.id WHERE th.tenThuongHieu = :brandName GROUP BY spct.sanPham.tenSanPham, spct.giaBan ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProductsByBrand(@Param("brandName") String brandName, Pageable pageable);




    //Từ chỗ này đi ai code của ai thì note lại tên tránh nhầm lẫn
}

