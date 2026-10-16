package com.cityhealthcare.repository;
import com.cityhealthcare.model.DoctorProfile; import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional;
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile,Long>{ Optional<DoctorProfile> findByAccount_Id(Long accountId); }
