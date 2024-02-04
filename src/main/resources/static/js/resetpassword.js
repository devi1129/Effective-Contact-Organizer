let confrimicon=document.getElementById("toggleconfirm");
let newicon=document.getElementById("togglenew");
let newpass=document.getElementById("newpassword");
let confirmpass=document.getElementById("confirmpassword");

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

confrimicon.addEventListener("click", () => toggle(confrimicon, confirmpass));
newicon.addEventListener("click", () => toggle(newicon, newpass));