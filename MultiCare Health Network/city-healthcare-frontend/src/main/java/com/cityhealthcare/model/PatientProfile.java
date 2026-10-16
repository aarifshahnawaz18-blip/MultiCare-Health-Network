package com.cityhealthcare.model;
import jakarta.persistence.*;
@Entity @Table(name="patients")
public class PatientProfile {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(optional=false) @JoinColumn(name="account_id",nullable=false,unique=true) private Account account;
 private Integer age; private String mobile;
 public PatientProfile(){} public PatientProfile(Account a){account=a;age=a.getAge();mobile=a.getMobile();}
 public Long getId(){return id;} public Account getAccount(){return account;} public Integer getAge(){return age;} public String getMobile(){return mobile;}
}
