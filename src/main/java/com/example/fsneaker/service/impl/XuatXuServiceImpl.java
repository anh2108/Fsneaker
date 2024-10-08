package com.example.fsneaker.service.impl;

import com.example.fsneaker.entity.XuatXu;
import com.example.fsneaker.repositories.XuatXuRepo;
import com.example.fsneaker.service.XuatXuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XuatXuServiceImpl implements XuatXuService {

    @Autowired
    private XuatXuRepo xuatXuRepo;


    @Override
    public List<XuatXu> getAllXuatXu() {
        return xuatXuRepo.findAll();
    }

    @Override
    public Page<XuatXu> pageXuatXu(Pageable pageable) {
        return null;
    }

    @Override
    public XuatXu findById(Integer idXuatXu) {
        return xuatXuRepo.findById(idXuatXu).get();
    }

    @Override
    public XuatXu add(XuatXu xuatXu) {
        xuatXu.setXuatXu(xuatXu.getXuatXu());
        return xuatXuRepo.save(xuatXu);
    }

    @Override
    public void delete(Integer idXuatXu) {

    }
}
