package com.cityhealthcare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_reports")
public class PatientReport {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "patient_id", nullable = false)
    private Account patient;
    @Column(nullable = false, length = 180) private String title;
    @Column(length = 1000) private String notes;
    @Column(nullable = false, length = 255) private String imagePath;
    @Column(nullable = false, length = 100) private String contentType;
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public PatientReport() {}
    public PatientReport(Account patient, String title, String notes, String imagePath, String contentType) {
        this.patient = patient; this.title = title; this.notes = notes; this.imagePath = imagePath; this.contentType = contentType;
    }
    public Long getId(){return id;} public Account getPatient(){return patient;} public String getTitle(){return title;}
    public String getNotes(){return notes;} public String getImagePath(){return imagePath;} public String getContentType(){return contentType;}
    public LocalDateTime getUploadedAt(){return uploadedAt;}
}
