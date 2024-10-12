package com.example.fsneaker.service;

import com.example.fsneaker.entity.KhuyenMai;
import com.example.fsneaker.repositories.KhuyenMaiRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhuyenMaiService {
    @Autowired
    private KhuyenMaiRepo khuyenMaiRepository;

    public List<KhuyenMai> searchKhuyenMai(String keyword){
        return khuyenMaiRepository.findByMaKhuyenMaiContainingIgnoreCaseOrTenKhuyenMaiContainingIgnoreCase(keyword,keyword );
    }
}
