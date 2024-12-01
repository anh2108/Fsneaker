package com.example.fsneaker.service;

import com.example.fsneaker.entity.NhanVien;
import com.example.fsneaker.repositories.NhanVienRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NhanVienService {
    @Autowired
    private NhanVienRepo nhanVienRepo;

    public Page<NhanVien> searchNhanVien(String keyword, Pageable pageable) {
        return nhanVienRepo.findByKeyword(keyword, pageable);
    }
    public NhanVien getByIdNhanVien(Integer idNhanVien){
        return nhanVienRepo.findById(idNhanVien).orElse(null);
    }
}
