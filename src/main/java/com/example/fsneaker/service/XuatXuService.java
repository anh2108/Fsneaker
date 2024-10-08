package com.example.fsneaker.service;

import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.entity.XuatXu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface XuatXuService {
    List<XuatXu> getAllXuatXu() ;

    Page<XuatXu> pageXuatXu(Pageable pageable);

    XuatXu findById(Integer idXuatXu);

    XuatXu add(XuatXu xuatXu);

    void delete(Integer idXuatXu);
}
