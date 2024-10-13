package com.example.fsneaker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "MauSac")

public class MauSac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "MaMauSac")
    private String maMauSac;

    @Column(name = "MauSac")
    private String mauSac;

    @Column(name = "TrangThai")
    private int trangThai;



}
