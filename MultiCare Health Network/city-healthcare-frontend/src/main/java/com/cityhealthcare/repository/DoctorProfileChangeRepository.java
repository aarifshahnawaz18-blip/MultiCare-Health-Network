package com.cityhealthcare.repository;
import com.cityhealthcare.model.DoctorProfileChange; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface DoctorProfileChangeRepository extends JpaRepository<DoctorProfileChange,Long>{ List<DoctorProfileChange> findAllByOrderBySubmittedAtAsc(); }
