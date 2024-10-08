package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThuongHieuRepo extends JpaRepository<ThuongHieu, Integer> {
}
