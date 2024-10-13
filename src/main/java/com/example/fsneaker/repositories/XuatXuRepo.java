package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.XuatXu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface XuatXuRepo extends JpaRepository< XuatXu, Integer> {

    @Query("Select xx from XuatXu xx where xx.id = :id")
    public XuatXu getXuatXuById(int id);

}
