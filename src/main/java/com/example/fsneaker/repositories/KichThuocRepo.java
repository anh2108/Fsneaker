package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.KichThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KichThuocRepo extends JpaRepository<KichThuoc, Integer> {

    @Query("select kt from KichThuoc kt where kt.id = :id")
    public KichThuoc getKichThuocById(int id);

}
