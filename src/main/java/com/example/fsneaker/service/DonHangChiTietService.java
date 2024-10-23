package com.example.fsneaker.service;

import com.example.fsneaker.entity.DonHang;
import com.example.fsneaker.entity.DonHangChiTiet;
import com.example.fsneaker.repositories.DonHangChiTietRepo;
import com.example.fsneaker.repositories.DonHangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class DonHangChiTietService {
    @Autowired
    private DonHangChiTietRepo donHangChiTietRepo;
    //Chỗ này là của trưởng nhóm code cấm đụng

    //Phương thức hiển thị sản phẩm bán được theo thương hiệu
    public List<Object[]> getTopSellingProductsByBrand(String brandName, int limit){
        Pageable pageable = PageRequest.of(0, limit);
        return donHangChiTietRepo.findTopSellingProductsByBrand(brandName, pageable);
    }

    //Từ đây trở đi ai code gì thì note người code lại tránh nhầm lẫn

}
