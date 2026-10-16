package com.cityhealthcare.repository;
import com.cityhealthcare.model.PatientProfile; import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional;
public interface PatientProfileRepository extends JpaRepository<PatientProfile,Long>{ Optional<PatientProfile> findByAccount_Id(Long accountId);
 void deleteByAccount_Id(Long accountId); }
