let buttonLogin = document.querySelector("#buttonLogin");
let inputPassword = document.querySelector("#inputPassword");

buttonLogin.disabled = true;

inputPassword.addEventListener("input", () => {
  if (inputPassword.value.length < 6) {
    buttonLogin.disabled = true;
  } else {
    buttonLogin.disabled = false;
  }
});
