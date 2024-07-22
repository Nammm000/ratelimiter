//package com.techprimers.ratelimiter.models;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Table(name = "users")
//@Data
//public class User {
//
//    public enum Role {
//        USER, ADMIN
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false, updatable = false)
//    private Long id;
//
//    private String name;
//
//    @Column(unique=true)
//    private String email;
//
//    private String phone;
//
//    private String imageUrl;
//
//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    private String password;
//
//    @Column(name = "status")
//    private String status;
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//}
