package com.cityhealthcare.model;
import jakarta.persistence.*;
@Entity @Table(name="doctors")
public class DoctorProfile {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(optional=false) @JoinColumn(name="account_id",nullable=false,unique=true) private Account account;
 private String aadhaarNumber; private String panNumber; private String degree;
 @Column(length=500) private String degreeCertificatePath;
 private String specialization; private String mobile;
 public DoctorProfile(){} public DoctorProfile(Account a){account=a;aadhaarNumber=a.getAadhaarNumber();panNumber=a.getPanNumber();degree=a.getDegree();degreeCertificatePath=a.getDegreeCertificatePath();specialization=a.getSpecialization();mobile=a.getMobile();}
 public Long getId(){return id;} public Account getAccount(){return account;} public String getAadhaarNumber(){return aadhaarNumber;} public String getPanNumber(){return panNumber;} public String getDegree(){return degree;} public String getDegreeCertificatePath(){return degreeCertificatePath;} public String getSpecialization(){return specialization;} public String getMobile(){return mobile;} public void setDegree(String v){degree=v;} public void setSpecialization(String v){specialization=v;} public void setMobile(String v){mobile=v;}
}
