package com.cityhealthcare.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="prescriptions")
public class Prescription {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="patient_id") private Account patient;
 @ManyToOne(optional=false) @JoinColumn(name="doctor_id") private Account doctor;
 @ManyToOne @JoinColumn(name="appointment_id") private Appointment appointment;
 @Column(nullable=false,length=500) private String diagnosis;
 @Column(nullable=false,length=5000) private String medicines;
 @Column(length=3000) private String instructions;
 private LocalDateTime createdAt=LocalDateTime.now();
 public Prescription(){} public Prescription(Account p,Account d,Appointment a,String diagnosis,String medicines,String instructions){patient=p;doctor=d;appointment=a;this.diagnosis=diagnosis;this.medicines=medicines;this.instructions=instructions;}
 public Long getId(){return id;} public Account getPatient(){return patient;} public Account getDoctor(){return doctor;} public Appointment getAppointment(){return appointment;} public String getDiagnosis(){return diagnosis;} public String getMedicines(){return medicines;} public String getInstructions(){return instructions;} public LocalDateTime getCreatedAt(){return createdAt;}
}
