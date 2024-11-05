package com.example.fsneaker.service;

import com.example.fsneaker.repositories.KichThuocRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KichThuocService {
    @Autowired
    private KichThuocRepo kichThuocRepo;

    public List<Object[]> getKichThuocVoiSanPham(Integer idThuongHieu){
        return kichThuocRepo.findKichThuocWithSanPham(idThuongHieu);
    }
}
