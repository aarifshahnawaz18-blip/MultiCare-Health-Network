package com.cityhealthcare;

import com.cityhealthcare.model.*;
import com.cityhealthcare.repository.*;
import com.cityhealthcare.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Controller
public class WebController {
 private static final Logger log = LoggerFactory.getLogger(WebController.class);
 private final AccountService accounts;
 private final AccountRepository accountRepo;
 private final DoctorProfileRepository doctorRepo;
 private final PatientProfileRepository patientRepo;
 private final AppointmentRepository appointmentRepo;
 private final PrescriptionRepository prescriptionRepo;
 private final DoctorDayOffRepository dayOffRepo;
 private final DoctorProfileChangeRepository profileChangeRepo;
 private final PatientReportRepository reportRepo;
 private final ContactMessageRepository messageRepo;
 private final DoctorReviewRepository reviewRepo;

 public WebController(AccountService a, AccountRepository ar, DoctorProfileRepository dr, PatientProfileRepository pr,
                      AppointmentRepository ap, PrescriptionRepository rx, DoctorDayOffRepository dayOffRepo, DoctorProfileChangeRepository profileChangeRepo, PatientReportRepository reportRepo, ContactMessageRepository messageRepo, DoctorReviewRepository reviewRepo) {
  accounts=a; accountRepo=ar; doctorRepo=dr; patientRepo=pr; appointmentRepo=ap; prescriptionRepo=rx; this.dayOffRepo=dayOffRepo; this.profileChangeRepo=profileChangeRepo; this.reportRepo=reportRepo; this.messageRepo=messageRepo; this.reviewRepo=reviewRepo;
 }
 @GetMapping({"/","/home"}) public String home(){return "index";}
 @PostMapping("/contact-message") public String contactMessage(@RequestParam String name,@RequestParam String number,@RequestParam String message,RedirectAttributes ra){try{if(name.isBlank()||number.isBlank()||message.isBlank())throw new IllegalArgumentException("Please fill all contact fields.");if(number.length()>30||message.length()>2000)throw new IllegalArgumentException("Please check the message/number length.");messageRepo.save(new ContactMessage(name.trim(),number.trim(),message.trim()));ra.addFlashAttribute("contactSuccess","Your message has been sent successfully.");}catch(Exception e){ra.addFlashAttribute("contactError",e.getMessage());}return "redirect:/#contact";}
 @GetMapping("/patient-reviews") public String patientReviews(HttpSession s,Model m){Account p=logged(s,"PATIENT");if(p==null)return "redirect:/patient-login";List<Account> doctors=appointmentRepo.findByPatient_IdOrderByAppointmentDateDesc(p.getId()).stream().map(Appointment::getDoctor).distinct().toList();m.addAttribute("doctors",doctors);m.addAttribute("reviews",reviewRepo.findByPatient_IdOrderByCreatedAtDesc(p.getId()));return "patient-reviews";}
 @PostMapping("/patient-reviews") public String submitReview(@RequestParam Long doctorId,@RequestParam int rating,@RequestParam String review,HttpSession s,RedirectAttributes ra){Account p=logged(s,"PATIENT");if(p==null)return "redirect:/patient-login";try{Account d=accountRepo.findById(doctorId).orElseThrow(()->new IllegalArgumentException("Doctor not found."));if(!"DOCTOR".equals(d.getRole())||!appointmentRepo.existsByDoctor_IdAndPatient_Id(d.getId(),p.getId()))throw new IllegalArgumentException("You can review only a doctor you have an appointment with.");if(rating<1||rating>5||review.isBlank())throw new IllegalArgumentException("Choose 1-5 stars and enter your review.");if(reviewRepo.existsByPatient_IdAndDoctor_Id(p.getId(),d.getId()))throw new IllegalArgumentException("You have already reviewed this doctor.");reviewRepo.save(new DoctorReview(p,d,rating,review.trim()));ra.addFlashAttribute("success","Review submitted successfully.");}catch(Exception e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/patient-reviews";}
 @GetMapping("/admin/messages-reviews") public String adminMessagesReviews(HttpSession s,Model m){if(!"ADMIN".equals(s.getAttribute("role")))return "redirect:/admin/login";m.addAttribute("messages",messageRepo.findAllByOrderByCreatedAtDesc());m.addAttribute("reviews",reviewRepo.findAllByOrderByCreatedAtDesc());return "admin-messages-reviews";}
 @GetMapping("/patient-login") public String patientLogin(){return "patient-login";}
 @GetMapping("/doctor-login") public String doctorLogin(){return "doctor-login";}
 @GetMapping("/patient-register") public String patientRegister(){return "patient-register";}
 @GetMapping("/doctor-register") public String doctorRegister(){return "doctor-register";}

 @PostMapping("/register")
 public String register(@RequestParam String fullName,@RequestParam String email,@RequestParam String password,
  @RequestParam String role,@RequestParam(required=false) String mobile,@RequestParam(required=false) Integer age,
  @RequestParam(required=false) String specialization,@RequestParam(required=false) String aadhaarNumber,
  @RequestParam(required=false) String panNumber,@RequestParam(required=false) String degree,@RequestParam(required=false) Integer consultationFee,
  @RequestParam(required=false) MultipartFile degreeCertificate,@RequestParam(required=false) MultipartFile profilePhoto, RedirectAttributes ra) {
  String certPath=null; String photoPath=null;
  try {
   if("DOCTOR".equals(role)) {
    if(consultationFee==null||consultationFee<0||consultationFee>1000000) throw new IllegalArgumentException("Enter a valid consultation fee (₹0 to ₹10,00,000). ");
    if(aadhaarNumber==null||!aadhaarNumber.matches("\\d{12}")) throw new IllegalArgumentException("Enter a valid 12-digit Aadhaar number.");
    if(panNumber==null||!panNumber.matches("[A-Za-z]{5}\\d{4}[A-Za-z]")) throw new IllegalArgumentException("Enter a valid PAN number.");
    if(degree==null||degree.isBlank()) throw new IllegalArgumentException("Degree is required for verification.");
    if(degreeCertificate==null||degreeCertificate.isEmpty()) throw new IllegalArgumentException("Please upload your degree certificate.");
    String contentType=degreeCertificate.getContentType();
    if(contentType==null||(!contentType.equals("application/pdf")&&!contentType.startsWith("image/"))) throw new IllegalArgumentException("Degree certificate must be a PDF or image.");
    if(degreeCertificate.getSize()>5*1024*1024) throw new IllegalArgumentException("Certificate must be 5 MB or smaller.");
    Path dir=Paths.get("uploads","degree-certificates").toAbsolutePath().normalize(); Files.createDirectories(dir);
    String ext="application/pdf".equals(contentType)?".pdf":
      ("image/png".equals(contentType)?".png":("image/jpeg".equals(contentType)?".jpg":
      ("image/webp".equals(contentType)?".webp":("image/gif".equals(contentType)?".gif":".img"))));
    String stored=UUID.randomUUID()+ext; Files.copy(degreeCertificate.getInputStream(),dir.resolve(stored),StandardCopyOption.REPLACE_EXISTING);
    certPath=stored;
   }
   if("DOCTOR".equals(role)) {
    if(profilePhoto==null||profilePhoto.isEmpty()) throw new IllegalArgumentException("Please upload your profile photo.");
    if(profilePhoto.getContentType()==null||!profilePhoto.getContentType().startsWith("image/")) throw new IllegalArgumentException("Profile photo must be an image.");
    if(profilePhoto.getSize()>3*1024*1024) throw new IllegalArgumentException("Profile photo must be 3 MB or smaller.");
    Path photoDir=Paths.get("uploads","doctor-photos").toAbsolutePath().normalize(); Files.createDirectories(photoDir);
    String ext=profilePhoto.getOriginalFilename()!=null&&profilePhoto.getOriginalFilename().toLowerCase().endsWith(".png")?".png":".jpg";
    photoPath=UUID.randomUUID()+ext; Files.copy(profilePhoto.getInputStream(),photoDir.resolve(photoPath),StandardCopyOption.REPLACE_EXISTING);
   }
   Account registered=accounts.register(fullName,email,password,role,mobile,age,specialization,aadhaarNumber,panNumber,degree,certPath,consultationFee);
   if(photoPath!=null){registered.setProfilePhotoPath(photoPath);accountRepo.save(registered);}
   ra.addFlashAttribute("success","Registration submitted. Admin approval is required before login.");
   return "redirect:/"+("DOCTOR".equals(role)?"doctor-login":"patient-login");
  } catch(Exception e) {
   ra.addFlashAttribute("error",e.getMessage()==null?"Registration failed.":e.getMessage());
   return "redirect:/"+("DOCTOR".equals(role)?"doctor-register":"patient-register");
  }
 }

 @PostMapping("/login")
 public String login(@RequestParam String email,@RequestParam String password,@RequestParam String role,HttpSession session,RedirectAttributes ra) {
  try {
   Account a=accounts.authenticate(email,password,role);
   session.setAttribute("accountId",a.getId()); session.setAttribute("accountName",a.getFullName()); session.setAttribute("role",a.getRole());
   session.setMaxInactiveInterval(8*60*60);
   log.info("Login success: role={}, accountId={}, sessionId={}, isNew={}", a.getRole(), a.getId(), session.getId(), session.isNew());
   return "redirect:/"+("DOCTOR".equals(role)?"doctor-dashboard":"patient-dashboard");
  } catch(IllegalArgumentException e) {
   ra.addFlashAttribute("error",e.getMessage());
   return "redirect:/"+("DOCTOR".equals(role)?"doctor-login":"patient-login");
  }
 }

 @GetMapping("/admin/login") public String adminLogin(){return "admin-login";}
 @PostMapping("/admin/login") public String adminLoginPost(@RequestParam String username,@RequestParam String password,HttpSession session,RedirectAttributes ra){
  String u=System.getenv().getOrDefault("CITYCARE_ADMIN_USER","admin"), p=System.getenv().getOrDefault("CITYCARE_ADMIN_PASSWORD","ChangeThisAdmin123!");
  if(u.equals(username)&&p.equals(password)){session.setAttribute("role","ADMIN");session.setMaxInactiveInterval(8*60*60);log.info("Admin login success: sessionId={}, isNew={}",session.getId(),session.isNew());return "redirect:/admin/dashboard";}
  ra.addFlashAttribute("error","Invalid admin credentials.");return "redirect:/admin/login";
 }
 @GetMapping("/admin/dashboard") public String adminDashboard(HttpSession s,Model m){
  if(!"ADMIN".equals(s.getAttribute("role")))return "redirect:/admin/login";
  m.addAttribute("pending",accounts.pending());m.addAttribute("allAccounts",accounts.all());m.addAttribute("profileChanges",profileChangeRepo.findAllByOrderBySubmittedAtAsc());
  m.addAttribute("pendingCount",accounts.count("PENDING"));m.addAttribute("approvedCount",accounts.count("APPROVED"));m.addAttribute("rejectedCount",accounts.count("REJECTED"));return "admin-dashboard";
 }
 @PostMapping("/admin/accounts/{id}/approve") public String approve(@PathVariable Long id,HttpSession s,RedirectAttributes ra){
  if(!"ADMIN".equals(s.getAttribute("role")))return "redirect:/admin/login";
  try{accounts.decide(id,"APPROVED",null);ra.addFlashAttribute("success","Account approved; profile record created.");}catch(Exception e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/admin/dashboard";
 }
 @PostMapping("/admin/accounts/{id}/reject") public String reject(@PathVariable Long id,@RequestParam(required=false) String reason,HttpSession s,RedirectAttributes ra){
  if(!"ADMIN".equals(s.getAttribute("role")))return "redirect:/admin/login";
  try{accounts.decide(id,"REJECTED",reason);ra.addFlashAttribute("success","Registration rejected.");}catch(Exception e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/admin/dashboard";
 }


 @GetMapping("/admin/appointments") public String adminAppointments(HttpSession s,Model m){
  if(!"ADMIN".equals(s.getAttribute("role")))return "redirect:/admin/login";
  m.addAttribute("appointments",appointmentRepo.findAllByOrderByAppointmentDateAscAppointmentTimeAsc());
  return "admin-appointments";
 }
 @GetMapping("/admin/doctors") public String adminDoctors(HttpSession s,Model m){
  if(!"ADMIN".equals(s.getAttribute("role")))return "redirect:/admin/login";
  List<Account> doctors=accountRepo.findByRoleAndStatus("DOCTOR","APPROVED");
  m.addAttribute("doctors",doctors);
  m.addAttribute("doctorProfiles",doctorRepo.findAll());
  m.addAttribute("appointments",appointmentRepo.findAllByOrderByAppointmentDateAscAppointmentTimeAsc());
  return "admin-doctors";
 }

 @GetMapping("/patient-dashboard") public String patientDash(HttpSession s,Model m){Account a=logged(s,"PATIENT");if(a==null)return "redirect:/patient-login";m.addAttribute("account",a);m.addAttribute("appointments",appointmentRepo.findByPatient_IdOrderByAppointmentDateDesc(a.getId()));m.addAttribute("prescriptions",prescriptionRepo.findByPatient_IdOrderByCreatedAtDesc(a.getId()));return "patient-dashboard";}
 @GetMapping("/doctor-dashboard") public String doctorDash(HttpSession s,Model m){Account a=logged(s,"DOCTOR");if(a==null)return "redirect:/doctor-login";List<Appointment> all=appointmentRepo.findByDoctor_IdOrderByAppointmentDateDesc(a.getId());m.addAttribute("account",a);m.addAttribute("todayAppointments",all.stream().filter(x->LocalDate.now().equals(x.getAppointmentDate())).toList());m.addAttribute("prescriptions",prescriptionRepo.findByDoctor_IdOrderByCreatedAtDesc(a.getId()));return "doctor-dashboard";}

 @GetMapping("/patient-profile") public String patientProfile(HttpSession s,Model m){Account a=logged(s,"PATIENT");if(a==null)return "redirect:/patient-login";m.addAttribute("account",a);m.addAttribute("profile",patientRepo.findByAccount_Id(a.getId()).orElse(null));return "patient-profile";}
 @GetMapping("/doctor-profile") public String doctorProfile(HttpSession s,Model m){Account a=logged(s,"DOCTOR");if(a==null)return "redirect:/doctor-login";m.addAttribute("account",a);m.addAttribute("profile",doctorRepo.findByAccount_Id(a.getId()).orElse(null));return "doctor-profile";}

 @GetMapping("/patient-appointments") public String patientAppointments(HttpSession s,Model m){Account a=logged(s,"PATIENT");if(a==null)return "redirect:/patient-login";m.addAttribute("appointments",appointmentRepo.findByPatient_IdOrderByAppointmentDateDesc(a.getId()));return "patient-appointments";}
 @GetMapping("/doctor-appointments") public String doctorAppointments(HttpSession s,Model m){Account a=logged(s,"DOCTOR");if(a==null)return "redirect:/doctor-login";m.addAttribute("appointments",appointmentRepo.findByDoctor_IdOrderByAppointmentDateDesc(a.getId()));return "doctor-appointments";}
 @GetMapping("/patient-prescriptions") public String patientPrescriptions(HttpSession s,Model m){Account a=logged(s,"PATIENT");if(a==null)return "redirect:/patient-login";m.addAttribute("prescriptions",prescriptionRepo.findByPatient_IdOrderByCreatedAtDesc(a.getId()));return "patient-prescriptions";}
 @GetMapping("/doctor-prescription") public String doctorPrescriptionPage(@RequestParam(required=false) Long appointmentId,HttpSession s,Model m){Account a=logged(s,"DOCTOR");if(a==null)return "redirect:/doctor-login";m.addAttribute("appointments",appointmentRepo.findByDoctor_IdOrderByAppointmentDateDesc(a.getId()));m.addAttribute("selectedAppointmentId",appointmentId);return "doctor-prescription";}
 @PostMapping("/doctor-prescription") public String createPrescription(@RequestParam Long appointmentId,@RequestParam String diagnosis,@RequestParam String medicines,@RequestParam(required=false) String instructions,HttpSession s,RedirectAttributes ra){
  Account doctor=logged(s,"DOCTOR");if(doctor==null)return "redirect:/doctor-login";
  Appointment ap=appointmentRepo.findById(appointmentId).orElseThrow(()->new IllegalArgumentException("Appointment not found."));
  if(!ap.getDoctor().getId().equals(doctor.getId()))return "redirect:/doctor-dashboard";
  if(diagnosis.isBlank()||medicines.isBlank()){ra.addFlashAttribute("error","Diagnosis and medicines are required.");return "redirect:/doctor-prescription";}
  prescriptionRepo.save(new Prescription(ap.getPatient(),doctor,ap,diagnosis.trim(),medicines.trim(),instructions));
  ra.addFlashAttribute("success","Prescription saved to the patient's record.");return "redirect:/doctor-appointments";
 }

 @GetMapping("/book-appointment") public String bookAppointment(@RequestParam(required=false) String department,HttpSession s,Model m){Account p=logged(s,"PATIENT");if(p==null)return "redirect:/patient-login";m.addAttribute("doctors",department==null||department.isBlank()?accountRepo.findByRoleAndStatus("DOCTOR","APPROVED"):accountRepo.findByRoleAndStatus("DOCTOR","APPROVED").stream().filter(d->department.equalsIgnoreCase(d.getSpecialization())).toList());m.addAttribute("department",department);m.addAttribute("departments",accountRepo.findByRoleAndStatus("DOCTOR","APPROVED").stream().map(Account::getSpecialization).filter(Objects::nonNull).distinct().sorted().toList());m.addAttribute("slots",timeSlots());return "book-appointment";}
 @PostMapping("/book-appointment") public String bookAppointmentPost(@RequestParam Long doctorId,@RequestParam String appointmentDate,@RequestParam String appointmentTime,@RequestParam(required=false) String reason,HttpSession s,RedirectAttributes ra){
  Account p=logged(s,"PATIENT");if(p==null)return "redirect:/patient-login";
  Account d=accountRepo.findById(doctorId).orElse(null);
  if(d==null||!"DOCTOR".equals(d.getRole())||!"APPROVED".equals(d.getStatus())){ra.addFlashAttribute("error","Please select an approved doctor.");return "redirect:/book-appointment";}
  LocalDate date;try{date=LocalDate.parse(appointmentDate);}catch(Exception e){ra.addFlashAttribute("error","Invalid appointment date.");return "redirect:/book-appointment";}
  if(date.isBefore(LocalDate.now())){ra.addFlashAttribute("error","Appointment date cannot be in the past.");return "redirect:/book-appointment";}
  if(!timeSlots().contains(appointmentTime)){ra.addFlashAttribute("error","Choose a valid 15-minute slot between 09:00 and 17:00.");return "redirect:/book-appointment";}
  if(dayOffRepo.existsByDoctor_IdAndOffDate(doctorId,date)){ra.addFlashAttribute("error","Doctor is unavailable on this date.");return "redirect:/book-appointment";}
  if(appointmentRepo.existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndStatus(doctorId,date,appointmentTime,"BOOKED")){ra.addFlashAttribute("error","This slot is already booked. Please choose another slot.");return "redirect:/book-appointment";}
  try {appointmentRepo.saveAndFlush(new Appointment(p,d,date,appointmentTime,reason==null?"Consultation":reason.trim()));}
  catch(org.springframework.dao.DataIntegrityViolationException ex){ra.addFlashAttribute("error","This slot was just booked by another patient. Please choose another slot.");return "redirect:/book-appointment";}
  ra.addFlashAttribute("success","Appointment booked successfully.");return "redirect:/patient-appointments";
 }
 private List<String> timeSlots(){List<String> slots=new ArrayList<>();for(LocalTime t=LocalTime.of(9,0);t.isBefore(LocalTime.of(17,0));t=t.plusMinutes(15))slots.add(t.format(DateTimeFormatter.ofPattern("HH:mm")));return slots;}
 @GetMapping({"/departments","/doctors"}) public String browseDoctors(@RequestParam(required=false) String department,HttpSession s,Model m){List<Account> approved=accountRepo.findByRoleAndStatus("DOCTOR","APPROVED");m.addAttribute("doctors",department==null||department.isBlank()?approved:approved.stream().filter(d->department.equalsIgnoreCase(d.getSpecialization())).toList());m.addAttribute("department",department);m.addAttribute("departments",approved.stream().map(Account::getSpecialization).filter(Objects::nonNull).distinct().sorted().toList());return "doctors";}
 @GetMapping("/patient-history") public String patientHistory(HttpSession s,Model m){Account a=logged(s,"PATIENT");if(a==null)return "redirect:/patient-login";m.addAttribute("appointments",appointmentRepo.findByPatient_IdOrderByAppointmentDateDesc(a.getId()));m.addAttribute("account",a);m.addAttribute("prescriptions",prescriptionRepo.findByPatient_IdOrderByCreatedAtDesc(a.getId()));m.addAttribute("reports",reportRepo.findByPatient_IdOrderByUploadedAtDesc(a.getId()));return "patient-history";}
 @GetMapping("/doctor-patients") public String doctorPatients(@RequestParam(required=false) String q,HttpSession s,Model m){Account a=logged(s,"DOCTOR");if(a==null)return "redirect:/doctor-login";List<Account> patients=appointmentRepo.findByDoctor_IdOrderByAppointmentDateDesc(a.getId()).stream().map(Appointment::getPatient).distinct().filter(p->q==null||q.isBlank()||p.getFullName().toLowerCase().contains(q.toLowerCase())||(p.getEmail()!=null&&p.getEmail().toLowerCase().contains(q.toLowerCase()))||(p.getMobile()!=null&&p.getMobile().contains(q))).toList();m.addAttribute("patients",patients);m.addAttribute("q",q);return "doctor-patients";}
 @GetMapping("/patient-details") public String patientDetails(@RequestParam(required=false) Long id,HttpSession s,Model m){Account d=logged(s,"DOCTOR");if(d==null)return "redirect:/doctor-login";if(id==null||!appointmentRepo.existsByDoctor_IdAndPatient_Id(d.getId(),id))return "redirect:/doctor-patients";Account p=accountRepo.findById(id).orElse(null);if(p==null||!"PATIENT".equals(p.getRole()))return "redirect:/doctor-patients";m.addAttribute("patient",p);m.addAttribute("prescriptions",prescriptionRepo.findByPatient_IdOrderByCreatedAtDesc(id));m.addAttribute("appointments",appointmentRepo.findByPatient_IdOrderByAppointmentDateDesc(id).stream().filter(x->x.getDoctor().getId().equals(d.getId())).toList());m.addAttribute("reports",reportRepo.findByPatient_IdOrderByUploadedAtDesc(id));return "patient-details";}
 @GetMapping("/doctor-availability") public String availability(HttpSession s,Model m){Account d=logged(s,"DOCTOR");if(d==null)return "redirect:/doctor-login";m.addAttribute("daysOff",dayOffRepo.findByDoctor_IdAndOffDateGreaterThanEqualOrderByOffDateAsc(d.getId(),LocalDate.now()));return "doctor-availability";}
 @GetMapping("/admin/certificates/{id}")
 public ResponseEntity<?> viewCertificate(@PathVariable Long id,HttpSession session) {
  if(!"ADMIN".equals(session.getAttribute("role"))) return ResponseEntity.status(403).body("Forbidden");
  Account a=accountRepo.findById(id).orElse(null);
  if(a==null||a.getDegreeCertificatePath()==null) return ResponseEntity.notFound().build();
  try {
   Path file=Paths.get("uploads","degree-certificates",a.getDegreeCertificatePath()).toAbsolutePath().normalize();
   if(!Files.exists(file)) return ResponseEntity.notFound().build();
   String ct=Files.probeContentType(file);
   MediaType media=ct!=null&&ct.equals("application/pdf")?MediaType.APPLICATION_PDF:MediaType.APPLICATION_OCTET_STREAM;
   return ResponseEntity.ok().contentType(media).header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=degree-certificate").body(new InputStreamResource(Files.newInputStream(file)));
  } catch(Exception ex) { return ResponseEntity.internalServerError().body("Could not read certificate."); }
 }
 @GetMapping("/doctor-photo/{id}") public ResponseEntity<?> doctorPhoto(@PathVariable Long id){Account a=accountRepo.findById(id).orElse(null);if(a==null||a.getProfilePhotoPath()==null)return ResponseEntity.notFound().build();try{Path f=Paths.get("uploads","doctor-photos",a.getProfilePhotoPath()).toAbsolutePath().normalize();if(!Files.exists(f))return ResponseEntity.notFound().build();String ct=Files.probeContentType(f);return ResponseEntity.ok().contentType(MediaType.parseMediaType(ct==null?"image/jpeg":ct)).body(new InputStreamResource(Files.newInputStream(f)));}catch(Exception e){return ResponseEntity.notFound().build();}}
 @PostMapping("/doctor-profile/edit") public String editDoctorProfile(@RequestParam String fullName,@RequestParam String mobile,@RequestParam String specialization,@RequestParam String degree,@RequestParam Integer consultationFee,@RequestParam(required=false) MultipartFile profilePhoto,HttpSession s,RedirectAttributes ra){Account d=logged(s,"DOCTOR");if(d==null)return "redirect:/doctor-login";try{if(consultationFee==null||consultationFee<0||consultationFee>1000000)throw new IllegalArgumentException("Enter a valid consultation fee (₹0 to ₹10,00,000).");String photo=d.getProfilePhotoPath();if(profilePhoto!=null&&!profilePhoto.isEmpty()){if(profilePhoto.getContentType()==null||!profilePhoto.getContentType().startsWith("image/")||profilePhoto.getSize()>3*1024*1024)throw new IllegalArgumentException("Photo must be an image up to 3 MB.");Path dir=Paths.get("uploads","doctor-photos").toAbsolutePath().normalize();Files.createDirectories(dir);String ext=profilePhoto.getOriginalFilename()!=null&&profilePhoto.getOriginalFilename().toLowerCase().endsWith(".png")?".png":".jpg";photo=UUID.randomUUID()+ext;Files.copy(profilePhoto.getInputStream(),dir.resolve(photo),StandardCopyOption.REPLACE_EXISTING);}profileChangeRepo.save(new DoctorProfileChange(d,fullName.trim(),mobile.trim(),specialization.trim(),degree.trim(),photo,consultationFee));ra.addFlashAttribute("success","Profile edit sent to Admin for approval. Your current profile remains unchanged until approval.");}catch(Exception e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/doctor-profile";}
 @PostMapping("/admin/profile-changes/{id}/approve") public String approveProfileChange(@PathVariable Long id,HttpSession s,RedirectAttributes ra){if(!"ADMIN".equals(s.getAttribute("role")))return "redirect:/admin/login";DoctorProfileChange c=profileChangeRepo.findById(id).orElse(null);if(c!=null){Account d=c.getDoctor();d.setFullName(c.getFullName());d.setMobile(c.getMobile());d.setSpecialization(c.getSpecialization());d.setDegree(c.getDegree());d.setProfilePhotoPath(c.getPhotoPath());d.setConsultationFee(c.getConsultationFee());accountRepo.save(d);doctorRepo.findByAccount_Id(d.getId()).ifPresent(dp->{dp.setDegree(c.getDegree());dp.setSpecialization(c.getSpecialization());dp.setMobile(c.getMobile());doctorRepo.save(dp);});profileChangeRepo.delete(c);ra.addFlashAttribute("success","Doctor profile change approved.");}return "redirect:/admin/dashboard";}
 @PostMapping("/admin/profile-changes/{id}/reject") public String rejectProfileChange(@PathVariable Long id,HttpSession s,RedirectAttributes ra){if(!"ADMIN".equals(s.getAttribute("role")))return "redirect:/admin/login";profileChangeRepo.deleteById(id);ra.addFlashAttribute("success","Profile change rejected.");return "redirect:/admin/dashboard";}
 @PostMapping("/doctor-availability/toggle") public String toggleDayOff(@RequestParam String date,HttpSession s,RedirectAttributes ra){Account d=logged(s,"DOCTOR");if(d==null)return "redirect:/doctor-login";try{LocalDate day=LocalDate.parse(date);if(day.isBefore(LocalDate.now()))throw new IllegalArgumentException("Past dates cannot be changed.");Optional<DoctorDayOff> existing=dayOffRepo.findByDoctor_IdAndOffDate(d.getId(),day);if(existing.isPresent())dayOffRepo.delete(existing.get());else{if(appointmentRepo.findByDoctor_IdOrderByAppointmentDateDesc(d.getId()).stream().anyMatch(a->a.getAppointmentDate().equals(day)&&"BOOKED".equals(a.getStatus())))throw new IllegalArgumentException("This date has existing bookings; cancel or reschedule them first.");dayOffRepo.save(new DoctorDayOff(d,day));}ra.addFlashAttribute("success","Availability updated.");}catch(Exception e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/doctor-availability";}
 @PostMapping("/patient-history/reports") public String uploadPatientReport(@RequestParam String title,@RequestParam(required=false) String notes,@RequestParam MultipartFile reportImage,HttpSession s,RedirectAttributes ra){
  Account p=logged(s,"PATIENT");if(p==null)return "redirect:/patient-login";
  try{if(title==null||title.isBlank())throw new IllegalArgumentException("Report title is required.");if(reportImage==null||reportImage.isEmpty())throw new IllegalArgumentException("Please select a report image.");String ct=reportImage.getContentType();if(ct==null||!ct.startsWith("image/"))throw new IllegalArgumentException("Only report images are allowed.");Path dir=Paths.get("uploads","patient-reports").toAbsolutePath().normalize();Files.createDirectories(dir);String ext=reportImage.getOriginalFilename()!=null&&reportImage.getOriginalFilename().toLowerCase().endsWith(".png")?".png":".jpg";String stored=UUID.randomUUID()+ext;Files.copy(reportImage.getInputStream(),dir.resolve(stored),StandardCopyOption.REPLACE_EXISTING);reportRepo.save(new PatientReport(p,title.trim(),notes==null?null:notes.trim(),stored,ct));ra.addFlashAttribute("success","Report uploaded to your medical history.");}catch(Exception e){ra.addFlashAttribute("error",e.getMessage()==null?"Could not upload report.":e.getMessage());}return "redirect:/patient-history";
 }
 @GetMapping("/patient-report/{id}/image") public ResponseEntity<?> patientReportImage(@PathVariable Long id,HttpSession s){Account viewer=logged(s,"PATIENT");boolean doctor="DOCTOR".equals(s.getAttribute("role"))&&s.getAttribute("accountId") instanceof Number;PatientReport report=reportRepo.findById(id).orElse(null);if(report==null)return ResponseEntity.notFound().build();if(viewer!=null&&!viewer.getId().equals(report.getPatient().getId()))return ResponseEntity.status(403).build();if(viewer==null&&doctor){Long did=((Number)s.getAttribute("accountId")).longValue();if(!appointmentRepo.existsByDoctor_IdAndPatient_Id(did,report.getPatient().getId()))return ResponseEntity.status(403).build();}else if(viewer==null&&!doctor)return ResponseEntity.status(403).build();try{Path f=Paths.get("uploads","patient-reports",report.getImagePath()).toAbsolutePath().normalize();if(!Files.exists(f))return ResponseEntity.notFound().build();return ResponseEntity.ok().contentType(MediaType.parseMediaType(report.getContentType())).header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=patient-report").body(new InputStreamResource(Files.newInputStream(f)));}catch(Exception e){return ResponseEntity.notFound().build();}}
 @GetMapping("/patient-profile/delete-confirm") public String deletePatientConfirm(HttpSession s){return logged(s,"PATIENT")==null?"redirect:/patient-login":"patient-profile";}
 @PostMapping("/patient-profile/delete") @Transactional public String deletePatient(HttpSession s,RedirectAttributes ra){Account p=logged(s,"PATIENT");if(p==null)return "redirect:/patient-login";prescriptionRepo.deleteByPatient_Id(p.getId());reportRepo.deleteByPatient_Id(p.getId());appointmentRepo.deleteByPatient_Id(p.getId());patientRepo.deleteByAccount_Id(p.getId());accountRepo.delete(p);s.invalidate();return "redirect:/?deleted=true";}
 @GetMapping("/logout") public String logout(HttpSession s){s.invalidate();return "redirect:/";}
 private Account logged(HttpSession s,String role){
  Object id=s.getAttribute("accountId");
  Object actualRole=s.getAttribute("role");
  if(!role.equals(actualRole) || !(id instanceof Number)) {
   log.warn("Session rejected for requiredRole={}, actualRole={}, accountIdType={}, sessionId={}, isNew={}", role, actualRole, id==null?"null":id.getClass().getName(), s.getId(), s.isNew());
   return null;
  }
  Account account=accountRepo.findById(((Number)id).longValue()).orElse(null);
  if(account==null) log.warn("Session accountId {} was not found in database; sessionId={}", id, s.getId());
  return account;
}
}
