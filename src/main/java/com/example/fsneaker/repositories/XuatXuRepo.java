package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.XuatXu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XuatXuRepo extends JpaRepository<XuatXu, Integer> {
}
