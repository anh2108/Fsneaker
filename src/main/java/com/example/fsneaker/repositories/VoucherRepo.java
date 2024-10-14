package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VoucherRepo extends JpaRepository<Voucher,Integer> {
    @Query("select o from Voucher o where o.loaiVoucher like ?1 " +
            " and (?2 is null or o.ngayBatDau >=?2) " +
            " and (?3 is null or o.ngayKetThuc <=?3) " +
            " and (?4 is null or o.trangThai = ?4) " +
            " order by o.id desc ")
    public Page<Voucher> searchPage(String keyword, LocalDate ngayBatDau, LocalDate ngayKetThuc, Integer trangThai, PageRequest p);

    List<Voucher> findByNgayBatDauAfter(LocalDate today);

    List<Voucher> findByNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(LocalDate today, LocalDate today2);

    List<Voucher> findByNgayKetThucBefore(LocalDate today);
}
