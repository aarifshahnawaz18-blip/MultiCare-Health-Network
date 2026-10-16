package com.cityhealthcare.model;
import jakarta.persistence.*; import java.time.LocalDate; import java.time.LocalDateTime;
@Entity @Table(name="appointments", uniqueConstraints=@UniqueConstraint(name="uk_doctor_slot", columnNames={"doctor_id","appointment_date","appointment_time"}))
public class Appointment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="patient_id") private Account patient;
 @ManyToOne(optional=false) @JoinColumn(name="doctor_id") private Account doctor;
 @Column(nullable=false) private LocalDate appointmentDate;
 @Column(nullable=false,length=30) private String appointmentTime;
 @Column(nullable=false,length=30) private String status="BOOKED";
 @Column(length=1000) private String reason;
 @Column(name="patient_name",nullable=false) private String patientName;
 private LocalDateTime createdAt=LocalDateTime.now();
 public Appointment(){} public Appointment(Account p,Account d,LocalDate date,String time,String reason){patient=p;doctor=d;appointmentDate=date;appointmentTime=time;this.reason=reason;this.patientName=p.getFullName();status="BOOKED";}
 public Long getId(){return id;} public Account getPatient(){return patient;} public Account getDoctor(){return doctor;} public LocalDate getAppointmentDate(){return appointmentDate;} public String getAppointmentTime(){return appointmentTime;} public String getStatus(){return status;} public void setStatus(String s){status=s;} public String getReason(){return reason;} public String getPatientName(){return patientName;} public LocalDateTime getCreatedAt(){return createdAt;}
}
