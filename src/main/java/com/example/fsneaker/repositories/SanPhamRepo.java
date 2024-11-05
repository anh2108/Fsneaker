package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SanPhamRepo extends JpaRepository<SanPham, Integer> {


    @Query("select sp, km.tenKhuyenMai, th.tenThuongHieu, xx.tenXuatXu from SanPham sp " +
            "join sp.khuyenMai km  " +
            "join sp.thuongHieu th " +
            "join sp.xuatXu xx " +
            "where sp.id = :id order by sp.ngayTao")
    public SanPham getSanPhamsById(int id);

}
