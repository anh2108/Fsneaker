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
    List<DonHangChiTiet> findAllByDonHangId(int id);
    //Kiểm tra nếu sản phẩm đã có trong hóa đơn
    DonHangChiTiet findByDonHangIdAndSanPhamChiTietId(int idDonHang, int idSanPhamChiTiet);
    @Query("SELECT c.sanPhamChiTiet.sanPham.id ,c.sanPhamChiTiet.sanPham.tenSanPham,c.sanPhamChiTiet.ngayTao,MAX(c.gia),SUM(c.soLuong), MAX(c.sanPhamChiTiet.soLuong) FROM DonHangChiTiet c GROUP BY c.sanPhamChiTiet.sanPham.id, c.sanPhamChiTiet.id,c.sanPhamChiTiet.sanPham.tenSanPham,c.sanPhamChiTiet.ngayTao ORDER BY SUM(c.soLuong) DESC")
    Page<Object[]> findBestSellingProducts(Pageable pageable);
    @Query("SELECT c.sanPhamChiTiet.sanPham.id ,c.sanPhamChiTiet.sanPham.tenSanPham,c.sanPhamChiTiet.ngayTao,c.gia, SUM(c.soLuong), c.sanPhamChiTiet.soLuong FROM DonHangChiTiet c GROUP BY c.sanPhamChiTiet.sanPham.id,c.sanPhamChiTiet.sanPham.tenSanPham,c.sanPhamChiTiet.ngayTao,c.gia, c.sanPhamChiTiet.soLuong ORDER BY (c.gia * SUM(c.soLuong)) DESC")
    Page<Object[]> sanPhamDoanhThuCaoNhat(Pageable pageable);
    //Tìm tất cả các chi tiết đơn hàng theo DonHang
    List<DonHangChiTiet> findByDonHang(DonHang donHang);

    //Truy vấn số lượng sản phẩm bán được theo thương hiệu
    @Query("SELECT dhct.sanPhamChiTiet.id ,dhct.sanPhamChiTiet.sanPham.tenSanPham, dhct.sanPhamChiTiet.giaBan, dhct.sanPhamChiTiet.mauSac, SUM(dhct.soLuong) as totalQuantity FROM DonHangChiTiet dhct WHERE dhct.sanPhamChiTiet.sanPham.thuongHieu.id = :id GROUP BY dhct.sanPhamChiTiet.id, dhct.sanPhamChiTiet.sanPham.tenSanPham, dhct.sanPhamChiTiet.giaBan , dhct.sanPhamChiTiet.mauSac ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProductsByBrand(@Param("id") Integer id, Pageable pageable);

    DonHangChiTiet findByDonHangIdAndSanPhamChiTietId(Integer idDonHang, Integer idSanPhamChiTiet);

    @Query("SELECT dhct.sanPhamChiTiet.sanPham.tenSanPham, dhct.sanPhamChiTiet.giaBan, dhct.sanPhamChiTiet.mauSac.tenMauSac, SUM(dhct.soLuong), dhct.sanPhamChiTiet.id FROM DonHangChiTiet dhct WHERE dhct.sanPhamChiTiet.sanPham.thuongHieu.id = :idThuongHieu GROUP BY dhct.sanPhamChiTiet.sanPham.tenSanPham, dhct.sanPhamChiTiet.giaBan, dhct.sanPhamChiTiet.mauSac.tenMauSac, dhct.sanPhamChiTiet.id ORDER BY SUM(dhct.soLuong) DESC")
    Page<Object[]> findNikeByPopularity(@Param("idThuongHieu")Integer idThuongHieu, Pageable pageable);

    void deleteDonHangChiTietByDonHangId(Integer idDonHang);
    //Từ chỗ này đi ai code của ai thì note lại tên tránh nhầm lẫn
}

