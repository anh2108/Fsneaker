package com.example.fsneaker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "KichThuoc")

public class KichThuoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "MaKichThuoc")
    private String makichThuoc;

    @Column(name = "KichThuoc")
    private String tenKichThuoc;

    @Column(name = "TrangThai")
    private int TrangThai;

}
