package com.cityhealthcare.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="accounts", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class Account {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String fullName;
 @Column(nullable=false,unique=true) private String email;
 @Column(nullable=false) private String passwordHash;
 @Column(nullable=false) private String role;
 @Column(nullable=false) private String status;
 private String mobile; private Integer age; private String specialization;
 @Column(nullable=true) private Integer consultationFee;
 @Column(length=20) private String aadhaarNumber;
 @Column(length=10) private String panNumber;
 @Column(length=150) private String degree;
 @Column(length=500) private String degreeCertificatePath;
 @Column(length=500) private String profilePhotoPath;
 private LocalDateTime createdAt=LocalDateTime.now();
 @Column(length=500) private String rejectionReason;
 public Account(){}
 public Long getId(){return id;} public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v==null?null:v.trim().toLowerCase();}
 public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
 public String getRole(){return role;} public void setRole(String v){role=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public String getMobile(){return mobile;} public void setMobile(String v){mobile=v;}
 public Integer getAge(){return age;} public void setAge(Integer v){age=v;}
 public String getSpecialization(){return specialization;} public void setSpecialization(String v){specialization=v;}
 public Integer getConsultationFee(){return consultationFee;} public void setConsultationFee(Integer v){consultationFee=v;}
 public String getAadhaarNumber(){return aadhaarNumber;} public void setAadhaarNumber(String v){aadhaarNumber=v;}
 public String getPanNumber(){return panNumber;} public void setPanNumber(String v){panNumber=v;}
 public String getDegree(){return degree;} public void setDegree(String v){degree=v;}
 public String getDegreeCertificatePath(){return degreeCertificatePath;} public void setDegreeCertificatePath(String v){degreeCertificatePath=v;}
 public String getProfilePhotoPath(){return profilePhotoPath;} public void setProfilePhotoPath(String v){profilePhotoPath=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public String getRejectionReason(){return rejectionReason;} public void setRejectionReason(String v){rejectionReason=v;}
}
