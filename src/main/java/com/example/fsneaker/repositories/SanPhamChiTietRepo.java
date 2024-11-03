package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamChiTietRepo extends JpaRepository<SanPhamChiTiet,Integer> {

    @Query("select e from SanPhamChiTiet e where e.id = :id")
    public SanPhamChiTiet findById(int id);

    //lấy nhiều sản phẩm chi tiết
    @Query("select spct,sp.tenSanPham, ms.tenMauSac,kt.tenKichThuoc from SanPhamChiTiet spct " +
            "join spct.sanPham sp " +
            "join  spct.mauSac ms " +
            "join spct.kichThuoc kt " +
            "where spct.id = :spctId order by spct.ngayTao")
    public List<SanPhamChiTiet> findBySanPhamChitietId(int spctId);

    // Lấy một sản phẩm chi tiết
    @Query("select spct,sp.tenSanPham, ms.tenMauSac,kt.tenKichThuoc from SanPhamChiTiet spct " +
            "join spct.sanPham sp " +
            "join  spct.mauSac ms " +
            "join spct.kichThuoc kt " +
            "where spct.id = :id ")
    public SanPhamChiTiet findBySanPhamChitietI2d(int id);


    //lấy giá trị lớn nhất để sinh ra mã tự động
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(maSanPhamChiTiet, 5) AS integer)), 0) FROM SanPhamChiTiet")
    int findMaxStt();



    //Chỗ này là của trưởng nhóm code cấm đụng
    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.sanPham.tenSanPham LIKE %:keyword% OR " +
            " spct.sanPham.thuongHieu.tenThuongHieu LIKE %:keyword% OR" +
            " spct.sanPham.xuatXu.tenXuatXu LIKE %:keyword% OR" +
            " spct.kichThuoc.tenKichThuoc LIKE %:keyword% OR" +
            " spct.mauSac.tenMauSac LIKE %:keyword")
    Page<SanPhamChiTiet> searchSanPhamById(@Param("keyword") String keyword, Pageable pageable);
    Page<SanPhamChiTiet> findAll(Pageable pageable);

    //Chỗ trưởng nhóm code cấm đụng vào

    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac FROM SanPhamChiTiet spct WHERE spct.sanPham.thuongHieu.id = :id GROUP BY spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac")
    Page<Object[]> findBySanPhamThuongHieuTenThuongHieu(Integer id, Pageable pageable);
}
