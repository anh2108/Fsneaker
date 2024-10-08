package com.example.fsneaker.service.impl;

import com.example.fsneaker.entity.ThuongHieu;
import com.example.fsneaker.repositories.KichThuocRepo;
import com.example.fsneaker.repositories.ThuongHieuRepo;
import com.example.fsneaker.service.ThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThuongHieuServiceImpl implements ThuongHieuService {
    @Autowired
    private ThuongHieuRepo thuongHieuRepo;


    @Override
    public List<ThuongHieu> getAllThuongHieu() {
        return thuongHieuRepo.findAll();
    }

    @Override
    public Page<ThuongHieu> pageThuongHieu(Pageable pageable) {
        return null;
    }

    @Override
    public ThuongHieu findById(Integer idThuongHieu) {
        return thuongHieuRepo.findById(idThuongHieu).get();
    }

    @Override
    public ThuongHieu add(ThuongHieu thuongHieu) {
        thuongHieu.setTenThuongHieu(thuongHieu.getTenThuongHieu());
        return thuongHieuRepo.save(thuongHieu);
    }

    @Override
    public void delete(Integer idThuongHieu) {

    }
}
