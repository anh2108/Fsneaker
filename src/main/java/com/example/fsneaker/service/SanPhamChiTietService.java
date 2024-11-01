package com.example.fsneaker.service;

import com.example.fsneaker.entity.SanPham;
import com.example.fsneaker.entity.SanPhamChiTiet;
import com.example.fsneaker.repositories.SanPhamChiTietRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamChiTietService {
    //Chỗ này là của trướng nhóm code cấm đụng
    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;
    public List<SanPhamChiTiet> getSanPhamChiTiet(){
        return sanPhamChiTietRepo.findAll();
    }
    public Page<SanPhamChiTiet> searchPaginated(String keyword, int page, int size){
        return sanPhamChiTietRepo.searchSanPhamById(keyword, PageRequest.of(page,size));
    }
    public Page<SanPhamChiTiet> findPaginated(int page, int size){
        return sanPhamChiTietRepo.findAll(PageRequest.of(page,size));
    }
    public SanPhamChiTiet getSanPhamChiTietById(Integer id){
        return sanPhamChiTietRepo.findById(id).orElse(null);
    }
    public void capNhatSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet){
        sanPhamChiTietRepo.save(sanPhamChiTiet);
    }
    public Page<Object[]> getThuongHieuTenThuongHieu(Integer id, int page, int size){
        return sanPhamChiTietRepo.findBySanPhamThuongHieuTenThuongHieu(id, PageRequest.of(page,size));
    }
}
