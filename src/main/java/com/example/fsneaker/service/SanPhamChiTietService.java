package com.example.fsneaker.service;

import com.example.fsneaker.entity.SanPham;
import com.example.fsneaker.entity.SanPhamChiTiet;
import com.example.fsneaker.repositories.SanPhamChiTietRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Object[]> getThuongHieuAndMauSac(Integer idThuongHieu, String tenMauSac, int page, int size){
        return sanPhamChiTietRepo.findByThuongHieuAndMauSac(idThuongHieu, tenMauSac, PageRequest.of(page,size));
    }
    public Page<Object[]> getThuongHieuAndKichThuoc(Integer idThuongHieu, String tenKichThuoc,int page, int size){
        return sanPhamChiTietRepo.findByThuongHieuAndKichThuoc(idThuongHieu, tenKichThuoc, PageRequest.of(page,size));
    }
    public Page<Object[]> getSanPhamTheoThuongHieuVaGia(int idThuongHieu, int minGia,int maxGia,int page, int size){
        return  sanPhamChiTietRepo.findByThuongHieuAndGiaBanBetween(idThuongHieu,minGia,maxGia,PageRequest.of(page,size));
    }
    public Page<Object[]> getSanPhamNikeSorted(Integer idThuongHieu, Pageable pageable){
        return sanPhamChiTietRepo.findByThuongHieuId(idThuongHieu,pageable);
    }

    //Sắp xếp theo sản phẩm mới
    public Page<Object[]> getNikeNyNewest(Integer idThuongHieu, int page, int size){
        return sanPhamChiTietRepo.findNikeByNewest(idThuongHieu, PageRequest.of(page,size));
    }
    //Sắp xếp theo giá từ thấp lên cáo
    public Page<Object[]> getNikeByPriceAsc(Integer idThuongHieu, int page, int size){
        return sanPhamChiTietRepo.findNikeByPriceAsc(idThuongHieu, PageRequest.of(page,size));
    }
    //Sắp xếp theo giá từ cao xuống thấp
    public Page<Object[]> getNikeByPriceDesc(Integer idThuongHieu , int page, int size){
        return sanPhamChiTietRepo.findNikeByPriceDesc(idThuongHieu, PageRequest.of(page, size));
    }
    //Sắp xếp theo tên
    public Page<Object[]> getNikeByName(Integer idThuongHieu, int page, int size){
        return sanPhamChiTietRepo.findNikeByName(idThuongHieu, PageRequest.of(page, size));
    }

    //code của luận

    //Sắp xếp theo sản phẩm mới
    public Page<Object[]> getAdidasNyNewest(Integer idThuongHieu, int page, int size){
        return sanPhamChiTietRepo.findAdidasByNewest(idThuongHieu, PageRequest.of(page,size));
    }

    //Sắp xếp theo giá từ thấp lên cáo
    public Page<Object[]> getAdidasByPriceAsc(Integer idThuongHieu, int page, int size){
        return sanPhamChiTietRepo.findAdidasByPriceAsc(idThuongHieu, PageRequest.of(page,size));
    }
    //Sắp xếp theo giá từ cao xuống thấp
    public Page<Object[]> getAdidasByPriceDesc(Integer idThuongHieu , int page, int size){
        return sanPhamChiTietRepo.findAdidasByPriceDesc(idThuongHieu, PageRequest.of(page, size));
    }
    //Sắp xếp theo tên
    public Page<Object[]> getAdidasByName(Integer idThuongHieu, int page, int size){
        return sanPhamChiTietRepo.findAdidasByName(idThuongHieu, PageRequest.of(page, size));
    }

}
