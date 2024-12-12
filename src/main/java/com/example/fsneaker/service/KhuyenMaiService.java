package com.example.fsneaker.service;

import com.example.fsneaker.entity.KhuyenMai;
import com.example.fsneaker.repositories.KhuyenMaiRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class KhuyenMaiService {
    @Autowired
    private KhuyenMaiRepo khuyenMaiRepository;

    public Page<KhuyenMai> searchKhuyenMai(String keyword, LocalDate ngayBatDau, LocalDate ngayKetThuc, Integer trangThai,  Pageable pageable) {

        return khuyenMaiRepository.searchByKeywordAndDate(keyword, ngayBatDau, ngayKetThuc, trangThai, pageable);
    }
}
