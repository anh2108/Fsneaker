package com.example.fsneaker.service;

import com.example.fsneaker.entity.DonHang;
import com.example.fsneaker.entity.Voucher;
import com.example.fsneaker.repositories.VoucherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherService {
    //Chỗ này là của trướng nhóm code cấm đụng vô
    @Autowired
    private VoucherRepo voucherRepo;
    public Voucher getVoucherById(int id){
        return voucherRepo.findById(id).orElse(null);
    }
    public Voucher getByIdVoucher(int idVoucher){
        return  voucherRepo.findById(idVoucher).orElseThrow(() -> new IllegalArgumentException("Mã voucher không hợp lệ: " + idVoucher));
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
    public double calculateDiscount(Voucher voucher, double totalAmount){
        if(voucher.getLoaiVoucher().equals("Giảm giá %")){
            return totalAmount * (voucher.getGiaTri() / 100);
        }else if(voucher.getLoaiVoucher().equals("Giảm giá số tiền")){
            return Math.min(voucher.getGiaTri(), totalAmount);
        }
        return 0;
    }
    public List<Voucher> getAllVoucherByTrangThaiAndGiaTri(Integer trangThai){
        return voucherRepo.findAllVoucherByTrangThaiAndAndGiaTri(trangThai);
    }
    public Voucher themVoucher(Voucher voucher){
        return voucherRepo.save(voucher);
    }
    //Ai code thì cOde vô đây
}
