package com.example.fsneaker.service;

import com.example.fsneaker.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MauSacService {
    List<MauSac> getAllMauSac() ;

    Page<MauSac> pageMauSac(Pageable pageable);

    MauSac findById(Integer idMauSac);

    MauSac add(MauSac mauSac);

    void delete(Integer idMauSac);
}
