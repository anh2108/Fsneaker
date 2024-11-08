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

    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac FROM SanPhamChiTiet spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu AND spct.mauSac.tenMauSac = :tenMauSac GROUP BY spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac")
    Page<Object[]> findByThuongHieuAndMauSac(@Param("idThuongHieu")Integer idThuongHieu,@Param("tenMauSac")String tenMauSac, Pageable pageable);

    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac FROM SanPhamChiTiet spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu AND spct.kichThuoc.tenKichThuoc = :tenKichThuoc GROUP BY spct.sanPham.tenSanPham,spct.giaBan,spct.mauSac.tenMauSac")
    Page<Object[]> findByThuongHieuAndKichThuoc(@Param("idThuongHieu") Integer idThuongHieu, @Param("tenKichThuoc")String tenKichThuoc, Pageable pageable);

    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan , spct.mauSac.tenMauSac FROM SanPhamChiTiet spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu AND spct.giaBan BETWEEN :minGia AND :maxGia GROUP BY spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac")
    Page<Object[]> findByThuongHieuAndGiaBanBetween(@Param("idThuongHieu") int idThuongHieu,@Param("minGia") int minGia, @Param("maxGia")int maxGia, Pageable pageable);

    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac FROM SanPhamChiTiet spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu GROUP BY spct.sanPham.tenSanPham, spct.giaBan ,spct.mauSac.tenMauSac")
    Page<Object[]> findByThuongHieuId(@Param("idThuongHieu")Integer idThuongHieu, Pageable pageable);

    //Sắp xếp theo sản phẩm mới
    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac, spct.ngayTao  FROM SanPhamChiTiet spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu GROUP BY spct.sanPham.tenSanPham,spct.giaBan,spct.mauSac.tenMauSac, spct.ngayTao ORDER BY spct.ngayTao DESC")
    Page<Object[]> findNikeByNewest(@Param("idThuongHieu")Integer idThuongHieu, Pageable pageable);

    //Sắp xếp theo giá từ thấp đến cao
    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan , spct.mauSac.tenMauSac FROM SanPhamChiTiet  spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu GROUP BY spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac ORDER BY spct.giaBan ASC")
    Page<Object[]> findNikeByPriceAsc(@Param("idThuongHieu") Integer idThuongHieu, Pageable pageable);

    //Sắp xếp theo giá từ cáo xuống thấp
    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac FROM SanPhamChiTiet spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu GROUP BY spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac ORDER BY spct.giaBan DESC")
    Page<Object[]> findNikeByPriceDesc(@Param("idThuongHieu") Integer idThuongHieu, Pageable pageable);

    //Sắp xếp theo tên
    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac FROM SanPhamChiTiet spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu GROUP BY spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac ORDER BY spct.sanPham.tenSanPham ASC")
    Page<Object[]> findNikeByName(@Param("idThuongHieu") Integer idThuongHieu, Pageable pageable);

    //coda của luận
    //Sắp xếp theo sản phẩm mới
    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac, spct.ngayTao  FROM SanPhamChiTiet spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu GROUP BY spct.sanPham.tenSanPham,spct.giaBan,spct.mauSac.tenMauSac, spct.ngayTao ORDER BY spct.ngayTao DESC")
    Page<Object[]> findAdidasByNewest(@Param("idThuongHieu")Integer idThuongHieu, Pageable pageable);

    //Sắp xếp theo giá từ thấp đến cao
    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan , spct.mauSac.tenMauSac FROM SanPhamChiTiet  spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu GROUP BY spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac ORDER BY spct.giaBan ASC")
    Page<Object[]> findAdidasByPriceAsc(@Param("idThuongHieu") Integer idThuongHieu, Pageable pageable);

    //Sắp xếp theo giá từ cáo xuống thấp
    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac FROM SanPhamChiTiet spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu GROUP BY spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac ORDER BY spct.giaBan DESC")
    Page<Object[]> findAdidasByPriceDesc(@Param("idThuongHieu") Integer idThuongHieu, Pageable pageable);

    //Sắp xếp theo tên
    @Query("SELECT spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac FROM SanPhamChiTiet spct WHERE spct.sanPham.thuongHieu.id = :idThuongHieu GROUP BY spct.sanPham.tenSanPham, spct.giaBan, spct.mauSac.tenMauSac ORDER BY spct.sanPham.tenSanPham ASC")
    Page<Object[]> findAdidasByName(@Param("idThuongHieu") Integer idThuongHieu, Pageable pageable);



}
