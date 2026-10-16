package com.cityhealthcare.repository;
import com.cityhealthcare.model.PatientReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PatientReportRepository extends JpaRepository<PatientReport, Long> {
    List<PatientReport> findByPatient_IdOrderByUploadedAtDesc(Long patientId);
    void deleteByPatient_Id(Long patientId);
}
