package com.example.fsneaker.service;

import com.example.fsneaker.entity.DonHang;
import com.example.fsneaker.entity.Voucher;
import com.example.fsneaker.repositories.VoucherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoucherService {
    //Chỗ này là của trướng nhóm code cấm đụng vô
    @Autowired
    private VoucherRepo voucherRepo;
    public Voucher getVoucherById(int id){
        return voucherRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Mã voucher không hợp lệ" + id));
    }
    public Voucher findByMaVoucher(String maVoucher){
        return  voucherRepo.findByMaVoucher(maVoucher);
    }
    public double applyVoucher(DonHang donHang, Voucher voucher){
        double discount = 0.0;
        if(voucher.getLoaiVoucher().equals("Giảm giá %")){
            discount = donHang.getTongTien() * (voucher.getGiaTri() / 100);
        }else if(voucher.getLoaiVoucher().equals("Giảm giá số tiền")){
            discount = voucher.getGiaTri();
        }
        //Tính tổng tiền sau khi giảm
        double tongTienGiamGia = donHang.getTongTien() - discount;
        return tongTienGiamGia > 0 ? tongTienGiamGia : 0;
    }


    //Ai code thì cOde vô đây
}
