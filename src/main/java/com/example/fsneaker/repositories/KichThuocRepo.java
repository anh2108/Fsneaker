package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.KichThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KichThuocRepo extends JpaRepository<KichThuoc, Integer> {

    @Query("select kt from KichThuoc kt where kt.id = :id")
    public KichThuoc getKichThuocById(int id);


    //Chỗ này là code của trưởng nhóm cấm đụng vào
    @Query("SELECT kt.tenKichThuoc, COUNT(spct.id) FROM KichThuoc kt JOIN SanPhamChiTiet spct ON kt.id = spct.kichThuoc.id WHERE spct.sanPham.thuongHieu.id = :idThuongHieu GROUP BY kt.tenKichThuoc HAVING COUNT(spct.id) > 0")
    List<Object[]> findKichThuocWithSanPham(@Param("idThuongHieu") Integer idThuongHieu);
}
