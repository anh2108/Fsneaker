package com.example.fsneaker.service;

import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ThuongHieuService {
    List<ThuongHieu> getAllThuongHieu() ;

    Page<ThuongHieu> pageThuongHieu(Pageable pageable);

    ThuongHieu findById(Integer idThuongHieu);

    ThuongHieu add(ThuongHieu thuongHieu);

    void delete(Integer idThuongHieu);
}
