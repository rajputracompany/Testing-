const preview=document.getElementById("preview");
const openBtn=document.getElementById("openBtn");
const closeBtn=document.getElementById("closeBtn");
const nextBtn=document.getElementById("nextBtn");
const toast=document.getElementById("toast");

function notify(text){
  toast.textContent=text; toast.classList.add("show");
  setTimeout(()=>toast.classList.remove("show"),1800);
}
openBtn.onclick=()=>{preview.classList.add("open");setTimeout(()=>preview.scrollIntoView({behavior:"smooth"}),80);notify("Preview opened");};
closeBtn.onclick=()=>window.scrollTo({top:0,behavior:"smooth"});
nextBtn.onclick=()=>notify("More chapters coming soon");
document.getElementById("menuBtn").onclick=()=>notify("Chapter menu coming soon");