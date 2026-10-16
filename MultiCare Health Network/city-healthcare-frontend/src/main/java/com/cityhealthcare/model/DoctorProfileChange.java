package com.cityhealthcare.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="doctor_profile_changes")
public class DoctorProfileChange {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="doctor_id",nullable=false) private Account doctor;
 @Column(nullable=false) private String fullName; @Column(nullable=false) private String mobile; @Column(nullable=false) private String specialization; @Column(nullable=false) private String degree;
 @Column(length=500) private String photoPath; private Integer consultationFee; private LocalDateTime submittedAt=LocalDateTime.now();
 public DoctorProfileChange(){} public DoctorProfileChange(Account d,String n,String m,String s,String degree,String photo,Integer fee){doctor=d;fullName=n;mobile=m;specialization=s;this.degree=degree;photoPath=photo;consultationFee=fee;}
 public Long getId(){return id;} public Account getDoctor(){return doctor;} public String getFullName(){return fullName;} public String getMobile(){return mobile;} public String getSpecialization(){return specialization;} public String getDegree(){return degree;} public String getPhotoPath(){return photoPath;} public Integer getConsultationFee(){return consultationFee;} public LocalDateTime getSubmittedAt(){return submittedAt;}
}
