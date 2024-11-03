package com.example.fsneaker.service;

import com.example.fsneaker.repositories.MauSacRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MauSacService {
    @Autowired
    private MauSacRepo mauSacRepo;

    public List<Object[]> getMauSacWithSanPham(Integer idThuongHieu){
        return mauSacRepo.findByMauSacWithProduct(idThuongHieu);
    }
}
