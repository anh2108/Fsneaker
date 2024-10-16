package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MauSacRepo extends JpaRepository<MauSac, Integer> {

    @Query("Select ms from MauSac ms where ms.id = :id")
    public MauSac getMauSacById(int id);

}
