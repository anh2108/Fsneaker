package com.example.fsneaker.service;

import com.example.fsneaker.entity.KichThuoc;
import com.example.fsneaker.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KichThuocService {

    List<KichThuoc> getAllkichThuoc() ;

    Page<KichThuoc> pageKichTHuoc(Pageable pageable);

    KichThuoc findById(Integer idKichThuoc);

    KichThuoc add(KichThuoc kichThuoc);

    void delete(Integer idKichhthuoc);
}
