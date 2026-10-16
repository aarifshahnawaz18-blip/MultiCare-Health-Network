document.addEventListener("DOMContentLoaded",()=>{
  const search=document.getElementById("globalSearch");
  if(search){
    search.addEventListener("keydown",e=>{
      if(e.key==="Enter"){
        localStorage.setItem("doctorSearch",search.value);
        location.href="/doctors";
      }
    });
  }
});
function doctorCard(d){
  const dept=getDept(d.dept);
  return `<div class="card doctor-card">
    <img class="doc-img" src="${d.image}" alt="${d.name}">
    <div class="doc-body">
      <span class="tag">${dept?.name||"Specialist"}</span>
      <h3>${d.name}</h3>
      <div class="muted">${d.specialization}</div>
      <div class="doc-meta"><span class="tag">${d.experience}</span><span class="tag green">${d.hospital}</span></div>
      <div class="muted">${d.degree}</div>
      <div style="margin-top:10px"><span class="price">₹${d.fee}</span> <span class="muted">consultation</span></div>
      <div class="actions">
        <a class="btn btn-outline btn-sm" href="/doctor-profile?id=${d.id}">View Profile</a>
        <a class="btn btn-primary btn-sm" href="/book-appointment?id=${d.id}">Book</a>
      </div>
    </div>
  </div>`;
}
