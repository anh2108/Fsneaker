package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MauSacRepo extends JpaRepository<MauSac, Integer> {

    @Query("Select ms from MauSac ms where ms.id = :id")
    public MauSac getMauSacById(int id);

    //Chỗ này là code của trưởng nhóm cẩm đụng vào
    @Query("SELECT ms.tenMauSac, COUNT(spct.id) FROM MauSac ms join SanPhamChiTiet spct ON ms.id = spct.mauSac.id GROUP BY ms.tenMauSac HAVING COUNT(spct.id) > 0")
    List<Object[]> findByMauSacWithProduct();
}
