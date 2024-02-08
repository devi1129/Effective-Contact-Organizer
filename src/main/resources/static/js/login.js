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

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('forgotpassword').addEventListener('click', function () {
        var username = document.getElementById('username').value;
        this.href = '/forgot?username=' + encodeURIComponent(username);
    });
});

icon.addEventListener("click",toggle);
// retainValues.js


