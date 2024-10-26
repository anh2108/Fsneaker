package com.example.fsneaker.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "XuatXu")

public class XuatXu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "MaXuatXu")
    private String maXuatXu;

    @Column(name = "TenXuatXu")
    private String tenXuatXu;

    @Column(name = "TrangThai")
    private int trangThai;

}
