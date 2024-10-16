package com.example.fsneaker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "ThuongHieu")

public class ThuongHieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "MaThuongHieu")
    private String maThuongHieu;

    @Column(name = "TenThuongHieu")
    private String tenThuongHieu;

    @Column(name = "TrangThai")
    private int trangThai;

}
