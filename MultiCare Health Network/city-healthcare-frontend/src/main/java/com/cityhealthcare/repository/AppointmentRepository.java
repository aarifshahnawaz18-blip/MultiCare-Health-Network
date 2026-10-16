package com.cityhealthcare.repository;
import com.cityhealthcare.model.Appointment; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface AppointmentRepository extends JpaRepository<Appointment,Long>{
 List<Appointment> findByPatient_IdOrderByAppointmentDateDesc(Long id);
 List<Appointment> findAllByOrderByAppointmentDateAscAppointmentTimeAsc();
 List<Appointment> findByDoctor_IdOrderByAppointmentDateDesc(Long id);
 boolean existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndStatus(Long doctorId, java.time.LocalDate date, String time, String status);
 void deleteByPatient_Id(Long patientId);
 boolean existsByDoctor_IdAndPatient_Id(Long doctorId, Long patientId);
}
