package com.cityhealthcare.repository;
import com.cityhealthcare.model.DoctorDayOff; import org.springframework.data.jpa.repository.JpaRepository; import java.time.LocalDate; import java.util.*;
public interface DoctorDayOffRepository extends JpaRepository<DoctorDayOff,Long>{
 boolean existsByDoctor_IdAndOffDate(Long doctorId, LocalDate date);
 Optional<DoctorDayOff> findByDoctor_IdAndOffDate(Long doctorId, LocalDate date);
 List<DoctorDayOff> findByDoctor_IdAndOffDateGreaterThanEqualOrderByOffDateAsc(Long doctorId, LocalDate date);
}
