package com.cityhealthcare.model;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="doctor_days_off", uniqueConstraints=@UniqueConstraint(columnNames={"doctor_id","off_date"}))
public class DoctorDayOff {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="doctor_id",nullable=false) private Account doctor;
 @Column(name="off_date",nullable=false) private LocalDate offDate;
 public DoctorDayOff(){}
 public DoctorDayOff(Account doctor, LocalDate date){this.doctor=doctor;this.offDate=date;}
 public Long getId(){return id;} public Account getDoctor(){return doctor;} public LocalDate getOffDate(){return offDate;}
}
