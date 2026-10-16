package com.cityhealthcare.service;
import com.cityhealthcare.model.Account; import com.cityhealthcare.repository.AccountRepository; import org.springframework.stereotype.Service; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import java.util.*; import org.springframework.transaction.annotation.Transactional; import com.cityhealthcare.repository.DoctorProfileRepository; import com.cityhealthcare.repository.PatientProfileRepository; import com.cityhealthcare.model.DoctorProfile; import com.cityhealthcare.model.PatientProfile;
@Service public class AccountService {
 private final AccountRepository repo; private final DoctorProfileRepository doctors; private final PatientProfileRepository patients; private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
 public AccountService(AccountRepository r, DoctorProfileRepository d, PatientProfileRepository p){repo=r;doctors=d;patients=p;}
 public Account register(String name,String email,String password,String role,String mobile,Integer age,String specialization,String aadhaar,String pan,String degree,String certificatePath,Integer consultationFee){
  if(name==null||name.isBlank()||email==null||email.isBlank()||password==null||password.length()<8) throw new IllegalArgumentException("Name/email required; password must be at least 8 characters.");
  if(!role.equals("PATIENT")&&!role.equals("DOCTOR")) throw new IllegalArgumentException("Invalid account type.");
  if(repo.findByEmail(email.trim().toLowerCase()).isPresent()) throw new IllegalArgumentException("This email is already registered.");
  Account a=new Account(); a.setFullName(name.trim());a.setEmail(email);a.setPasswordHash(encoder.encode(password));a.setRole(role);a.setStatus("PENDING");a.setMobile(mobile);a.setAge(age);a.setSpecialization(specialization);a.setConsultationFee(consultationFee);a.setAadhaarNumber(aadhaar);a.setPanNumber(pan);a.setDegree(degree);a.setDegreeCertificatePath(certificatePath);return repo.save(a);
 }
 public Account authenticate(String email,String password,String expectedRole){Account a=repo.findByEmail(email.trim().toLowerCase()).orElseThrow(()->new IllegalArgumentException("Invalid email or password."));if(!encoder.matches(password,a.getPasswordHash()))throw new IllegalArgumentException("Invalid email or password.");if(!a.getRole().equals(expectedRole))throw new IllegalArgumentException("Please use the correct login page.");if(!a.getStatus().equals("APPROVED"))throw new IllegalArgumentException(a.getStatus().equals("PENDING")?"Your account is awaiting admin approval.":"Your account was rejected. Contact the administrator.");return a;}
 public List<Account> pending(){return repo.findByStatusOrderByCreatedAtAsc("PENDING");} public List<Account> all(){return repo.findAll();}
 @Transactional public Account decide(Long id,String status,String reason){Account a=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Account not found."));if(!a.getStatus().equals("PENDING"))throw new IllegalArgumentException("This request has already been processed.");a.setStatus(status);a.setRejectionReason(reason);Account saved=repo.save(a);
  if("APPROVED".equals(status)){
   if("DOCTOR".equals(saved.getRole())&&!doctors.findByAccount_Id(saved.getId()).isPresent()) doctors.save(new DoctorProfile(saved));
   if("PATIENT".equals(saved.getRole())&&!patients.findByAccount_Id(saved.getId()).isPresent()) patients.save(new PatientProfile(saved));
  }
  return saved;}
 public long count(String status){return repo.countByStatus(status);}
}
