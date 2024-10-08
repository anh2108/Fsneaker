package com.example.fsneaker.service.impl;

import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.repositories.MauSacRepo;
import com.example.fsneaker.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MauSacServiceImpl implements MauSacService {

    @Autowired
    private MauSacRepo mauSacRepo;

    @Override
    public List<MauSac> getAllMauSac() {
        return mauSacRepo.findAll();
    }

    @Override
    public Page<MauSac> pageMauSac(Pageable pageable) {
        return mauSacRepo.findAll(pageable);
    }

    @Override
    public MauSac findById(Integer idMauSac) {
        return mauSacRepo.findById(idMauSac).get();
    }

    @Override
    public MauSac add(MauSac mauSac) {
        mauSac.setMauSac(mauSac.getMauSac());
        return mauSacRepo.save(mauSac);
    }

    @Override
    public void delete(Integer idMauSac) {
        mauSacRepo.deleteById(idMauSac);
    }


}
