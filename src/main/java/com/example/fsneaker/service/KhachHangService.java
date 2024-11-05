package com.example.fsneaker.service;


import com.example.fsneaker.entity.KhachHang;
import com.example.fsneaker.repositories.KhachHangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class KhachHangService {


    @Autowired
    private KhachHangRepo khachHangRepo;

    //public List<KhachHang> getAllKhachHang(){
//        return khachHangRepo.findAll();
//    }

    public KhachHang themKH(KhachHang khachHang){
        return khachHangRepo.save(khachHang);
    }


    public KhachHang getKhachHangById(int id){
        return khachHangRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Mã khách hàng không hợp lệ: " + id));
    }
//    public List<KhachHang> timKiemKhachHang(String keyword){
//        return khachHangRepo.findByTenKhachHangContainingOrEmailContainingOrSoDienThoaiContaining(keyword, keyword,keyword);
//    }
//    public KhachHang getKhachHangBySoDienThoai(String sdt){
//        return khachHangRepo.findBySoDienThoai(sdt);
//    }


    public Page<KhachHang> searchPaginated(String keyword, int page , int size){
        return khachHangRepo.searchByKhachHang(keyword, PageRequest.of(page,size));
    }
    public Page<KhachHang> findPaginated(int page, int  size){
        return khachHangRepo.findAll(PageRequest.of(page,size));
    }
    public long countTotalKhachHang(){
        return khachHangRepo.count();
    }

    //Chỗ này là code của trưởng nhóm code cấm đụng vào
    public KhachHang timKiemTheoSoDienThoaiHoacEmail(String keyword){
        return khachHangRepo.findBySoDienThoaiOrEmail(keyword,keyword);
    }
}
