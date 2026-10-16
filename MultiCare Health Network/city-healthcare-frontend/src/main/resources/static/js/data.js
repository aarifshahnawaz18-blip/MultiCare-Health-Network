const departments = [
  {id:"cardio", name:"Cardiology", icon:"❤️", desc:"Heart and cardiovascular care"},
  {id:"ortho", name:"Orthopedics", icon:"🦴", desc:"Bones, joints and muscles"},
  {id:"neuro", name:"Neurology", icon:"🧠", desc:"Brain and nervous system"},
  {id:"derma", name:"Dermatology", icon:"🧴", desc:"Skin, hair and nails"},
  {id:"pedia", name:"Pediatrics", icon:"👶", desc:"Healthcare for children"},
  {id:"general", name:"General Medicine", icon:"🩺", desc:"Primary and preventive care"},
  {id:"ent", name:"ENT", icon:"👂", desc:"Ear, nose and throat"},
  {id:"gyn", name:"Gynecology", icon:"🌸", desc:"Women's health"}
];

const doctors = [
  {id:1,name:"Dr. Rahul Sharma",dept:"cardio",specialization:"Senior Cardiologist",degree:"MBBS, MD (Cardiology)",experience:"12 Years",fee:700,hospital:"CityCare Hospital",area:"Central City",phone:"+91 98765 12001",image:"https://i.pravatar.cc/500?img=12",about:"Experienced cardiologist specializing in preventive cardiology, hypertension and cardiac rehabilitation.",slots:["10:00 AM","10:30 AM","11:00 AM","11:30 AM","04:00 PM","04:30 PM"],leaves:[]},
  {id:2,name:"Dr. Sneha Patil",dept:"cardio",specialization:"Interventional Cardiologist",degree:"MBBS, DM (Cardiology)",experience:"9 Years",fee:900,hospital:"Metro Heart Centre",area:"West City",phone:"+91 98765 12002",image:"https://i.pravatar.cc/500?img=47",about:"Specialist in cardiac diagnostics and interventional procedures.",slots:["09:00 AM","09:30 AM","02:00 PM","02:30 PM","05:00 PM"],leaves:[]},
  {id:3,name:"Dr. Amit Verma",dept:"ortho",specialization:"Orthopedic Surgeon",degree:"MBBS, MS (Orthopedics)",experience:"15 Years",fee:800,hospital:"Sunrise Multispeciality",area:"North City",phone:"+91 98765 12003",image:"https://i.pravatar.cc/500?img=11",about:"Orthopedic surgeon focused on sports injuries, joint pain and fracture care.",slots:["10:00 AM","10:30 AM","12:00 PM","03:00 PM","03:30 PM"],leaves:[]},
  {id:4,name:"Dr. Neha Kulkarni",dept:"neuro",specialization:"Neurologist",degree:"MBBS, MD, DM (Neurology)",experience:"11 Years",fee:1000,hospital:"NeuroLife Hospital",area:"East City",phone:"+91 98765 12004",image:"https://i.pravatar.cc/500?img=44",about:"Neurologist providing care for stroke, migraine, epilepsy and movement disorders.",slots:["09:30 AM","10:00 AM","01:00 PM","01:30 PM"],leaves:[]},
  {id:5,name:"Dr. Arjun Mehta",dept:"derma",specialization:"Dermatologist",degree:"MBBS, MD (Dermatology)",experience:"8 Years",fee:600,hospital:"DermaPlus Clinic",area:"South City",phone:"+91 98765 12005",image:"https://i.pravatar.cc/500?img=13",about:"Dermatologist specializing in acne, pigmentation, hair and cosmetic dermatology.",slots:["11:00 AM","11:30 AM","12:00 PM","05:00 PM","05:30 PM"],leaves:[]},
  {id:6,name:"Dr. Pooja Joshi",dept:"pedia",specialization:"Pediatrician",degree:"MBBS, MD (Pediatrics)",experience:"10 Years",fee:500,hospital:"Little Stars Hospital",area:"Central City",phone:"+91 98765 12006",image:"https://i.pravatar.cc/500?img=49",about:"Pediatrician providing preventive and general healthcare for infants and children.",slots:["09:00 AM","09:30 AM","04:00 PM","04:30 PM"],leaves:[]},
  {id:7,name:"Dr. Vikram Rao",dept:"general",specialization:"General Physician",degree:"MBBS, MD (Medicine)",experience:"14 Years",fee:400,hospital:"CityCare Hospital",area:"Central City",phone:"+91 98765 12007",image:"https://i.pravatar.cc/500?img=68",about:"General physician for fever, infections, diabetes screening and preventive care.",slots:["09:00 AM","09:30 AM","10:00 AM","06:00 PM","06:30 PM"],leaves:[]},
  {id:8,name:"Dr. Kavya Nair",dept:"ent",specialization:"ENT Specialist",degree:"MBBS, MS (ENT)",experience:"7 Years",fee:650,hospital:"HealthFirst Hospital",area:"West City",phone:"+91 98765 12008",image:"https://i.pravatar.cc/500?img=32",about:"ENT specialist for sinus, throat, ear infections and hearing-related conditions.",slots:["10:30 AM","11:00 AM","02:30 PM","03:00 PM"],leaves:[]},
  {id:9,name:"Dr. Riya Deshmukh",dept:"gyn",specialization:"Gynecologist",degree:"MBBS, MS (OBGYN)",experience:"13 Years",fee:850,hospital:"WomenCare Hospital",area:"North City",phone:"+91 98765 12009",image:"https://i.pravatar.cc/500?img=45",about:"Gynecologist focused on women's wellness, pregnancy care and reproductive health.",slots:["09:00 AM","09:30 AM","01:00 PM","04:00 PM"],leaves:[]}
];

const patients = [
  {id:"P1001",name:"Amit Kumar",age:32,gender:"Male",phone:"9876543210",blood:"B+",email:"amit@example.com"},
  {id:"P1002",name:"Priya Singh",age:28,gender:"Female",phone:"9876543211",blood:"O+",email:"priya@example.com"},
  {id:"P1003",name:"Rahul Patil",age:45,gender:"Male",phone:"9876543212",blood:"A+",email:"rahul@example.com"},
  {id:"P1004",name:"Snehal Joshi",age:36,gender:"Female",phone:"9876543213",blood:"AB+",email:"snehal@example.com"}
];

const history = [
  {patientId:"P1001",date:"2026-09-12",doctor:"Dr. Rahul Sharma",hospital:"CityCare Hospital",diagnosis:"Hypertension follow-up",notes:"BP monitored; lifestyle changes advised."},
  {patientId:"P1001",date:"2026-07-18",doctor:"Dr. Vikram Rao",hospital:"CityCare Hospital",diagnosis:"Fever and viral infection",notes:"Recovered after prescribed medication."},
  {patientId:"P1002",date:"2026-08-21",doctor:"Dr. Neha Kulkarni",hospital:"NeuroLife Hospital",diagnosis:"Migraine",notes:"Trigger management discussed."}
];

const prescriptions = [
  {patientId:"P1001",date:"2026-09-12",doctor:"Dr. Rahul Sharma",diagnosis:"Hypertension follow-up",medicines:["Amlodipine 5mg - Once daily","Telmisartan 40mg - Once daily"],instructions:"Take medicines after breakfast. Monitor BP regularly."},
  {patientId:"P1001",date:"2026-07-18",doctor:"Dr. Vikram Rao",diagnosis:"Viral fever",medicines:["Paracetamol 500mg - As needed","ORS - 2 to 3 times daily"],instructions:"Rest and maintain hydration."}
];

function save(key,val){localStorage.setItem(key,JSON.stringify(val))}
function load(key,fallback){try{return JSON.parse(localStorage.getItem(key)) ?? fallback}catch(e){return fallback}}
function getDoctor(id){return doctors.find(d=>d.id==id)}
function getDept(id){return departments.find(d=>d.id===id)}
function fmtDate(d){
  return new Date(d+"T00:00:00").toLocaleDateString("en-IN",{day:"2-digit",month:"short",year:"numeric"});
}
function next15(){
  const out=[];
  const base=new Date();
  for(let i=0;i<15;i++){
    const d=new Date(base); d.setHours(0,0,0,0); d.setDate(base.getDate()+i);
    const iso=d.toISOString().slice(0,10);
    out.push({iso,label:d.toLocaleDateString("en-IN",{weekday:"short",day:"2-digit",month:"short"})});
  }
  return out;
}
function getAppointments(){
  return load("appointments",[
    {id:"A1001",patientId:"P1001",patient:"Amit Kumar",doctorId:1,doctor:"Dr. Rahul Sharma",date:next15()[0].iso,time:"10:00 AM",status:"Confirmed",reason:"Regular check-up"},
    {id:"A1002",patientId:"P1002",patient:"Priya Singh",doctorId:4,doctor:"Dr. Neha Kulkarni",date:next15()[1].iso,time:"09:30 AM",status:"Confirmed",reason:"Migraine consultation"}
  ]);
}
function toast(msg){
  const el=document.getElementById("toast"); if(!el)return;
  el.textContent=msg; el.classList.add("show"); setTimeout(()=>el.classList.remove("show"),2600);
}
