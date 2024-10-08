package com.example.fsneaker.service.impl;

import com.example.fsneaker.entity.KichThuoc;
import com.example.fsneaker.repositories.KichThuocRepo;
import com.example.fsneaker.service.KichThuocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KichThuocServiceImpl implements KichThuocService {

    @Autowired
    private KichThuocRepo kichThuocRepo;


    @Override
    public List<KichThuoc> getAllkichThuoc() {
        return kichThuocRepo.findAll();
    }

    @Override
    public Page<KichThuoc> pageKichTHuoc(Pageable pageable) {
        return null;
    }

    @Override
    public KichThuoc findById(Integer idKichThuoc) {
        return kichThuocRepo.findById(idKichThuoc).get();
    }

    @Override
    public KichThuoc add(KichThuoc kichThuoc) {
        kichThuoc.setKichThuoc(kichThuoc.getKichThuoc());
        return kichThuocRepo.save(kichThuoc);
    }

    @Override
    public void delete(Integer idKichhthuoc) {

    }
}
