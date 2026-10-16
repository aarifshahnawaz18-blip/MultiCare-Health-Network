package com.cityhealthcare.repository;
import com.cityhealthcare.model.Prescription; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface PrescriptionRepository extends JpaRepository<Prescription,Long>{
 List<Prescription> findByPatient_IdOrderByCreatedAtDesc(Long id);
 List<Prescription> findByDoctor_IdOrderByCreatedAtDesc(Long id);
 void deleteByPatient_Id(Long patientId);
}
