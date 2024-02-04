
let oldicon=document.getElementById("toggleold");
let newicon=document.getElementById("togglenew");
let oldpass=document.getElementById("oldpassword");
let newpass=document.getElementById("newpassword");

const toggle = (icon, passwordInput) => {
    if (icon.classList.contains("fa-eye-slash")) {
        icon.classList.remove("fa-eye-slash");
        icon.classList.add("fa-eye");
        passwordInput.type = "text";
    } else {
        icon.classList.remove("fa-eye");
        icon.classList.add("fa-eye-slash");
        passwordInput.type = "password";
    }
}

oldicon.addEventListener("click", () => toggle(oldicon, oldpass));
newicon.addEventListener("click", () => toggle(newicon, newpass));


function confirmDeleteAccount() {
        Swal.fire({
            title: "Are you sure?",
            text: "You will loose your contacts and Account..",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Yes, delete it!",
        }).then((result) => {
            if (result.isConfirmed) {
                window.location = "/setting/delete";
                
                 Swal.fire("Confirmed", "Your Account is deleted", "info");
            } else {
                Swal.fire("Cancelled", "Your Account is safe!", "info");
            }
        });
    }
    
    
    const togglebar = () => {
    const sidebar = document.querySelector(".sidebar");
    const main = document.getElementById("main");
 
    const openbtn = document.querySelector("#main i");

    if (getComputedStyle(sidebar).display === "block") {
        sidebar.style.display = "none";
        main.style.marginLeft = "0%";
          openbtn.style.display = "inline"; 
          
          
     
    } else {
        sidebar.style.display = "block";
        main.style.marginLeft = "20%";
         openbtn.style.display = "none";
    }
};
document.addEventListener("DOMContentLoaded", () => {
    // Optionally, you can also hide the open button here in case CSS is disabled
  const mediaQuery600 = window.matchMedia("(max-width: 700px)");
const sidebar = document.querySelector(".sidebar");
const main = document.getElementById("main");
const openBtn = document.querySelector("#main i");

const handleMediaQuery = (mq) => {
    if (mq.matches) {
        // Screen width is 600px or less
        sidebar.classList.add("hidden");
        main.style.marginLeft = "0%";
        openBtn.style.display = "inline";
    } else {
        // Screen width is greater than 600px
        
        sidebar.classList.remove("hidden");
        main.style.marginLeft = "20%";
        openBtn.style.display = "none";
    }
};

// Initial check on page load
handleMediaQuery(mediaQuery600);

// Add a listener to handle changes in the media query
mediaQuery600.addListener(handleMediaQuery);

});
