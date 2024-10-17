package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SanPhamRepo extends JpaRepository<SanPham, Integer> {

    @Query("select e from SanPham e where e.id= :id")
    public SanPham getSanPhamById(int id);

}
