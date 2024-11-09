package com.example.fsneaker.repositories;

import com.example.fsneaker.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VoucherRepo extends JpaRepository<Voucher,Integer> {
    @Query("select o from Voucher o where o.loaiVoucher like ?1 or o.maVoucher like ?1 " +
            " and (?2 is null or o.ngayBatDau >=?2) " +
            " and (?3 is null or o.ngayKetThuc <=?3) " +
            " and (?4 is null or o.trangThai = ?4) " +
            " order by o.id desc ")
    public Page<Voucher> searchPage(String keyword, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer trangThai, PageRequest p);

    List<Voucher> findByNgayBatDauAfter(LocalDateTime today);

    List<Voucher> findByNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(LocalDateTime today, LocalDateTime today2);

    List<Voucher> findByNgayKetThucBefore(LocalDateTime today);


    //Chỗ này là của trướng nhóm code cấm đụng vào
    //Voucher findById(int idVoucher); //Tìm voucher theo mã;
    @Query("SELECT v FROM Voucher v WHERE v.trangThai = :trangThai ORDER BY v.giaTri DESC")
    List<Voucher> findAllVoucherByTrangThaiAndAndGiaTri(@Param("trangThai") Integer trangThai);

}
