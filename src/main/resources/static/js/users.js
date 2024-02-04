

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
tinymce.init({
  selector: 'textarea',
  init_instance_callback : function(editor) {
      var freeTiny = document.querySelector('.mce-notification');
      freeTiny.style.display = 'none';
  }
});


let password = document.getElementById("password");
let icon = document.getElementById("togglepass");

const toggle = () => {
    if (icon.classList.contains("fa-eye-slash")) {
        icon.classList.remove("fa-eye-slash");
        icon.classList.add("fa-eye");
        password.type = "text";
    } else {
        icon.classList.remove("fa-eye");
        icon.classList.add("fa-eye-slash");
        password.type = "password";
    }
};

icon.addEventListener("click", toggle);