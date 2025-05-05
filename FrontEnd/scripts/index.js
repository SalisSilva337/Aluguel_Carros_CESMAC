let iframe = document.querySelector(".iframe");
let homeButton = document.querySelector("#homeButton");
let rentCarButton = document.querySelector("#rentCarButton");
let aboutButton = document.querySelector("#aboutButton");
let contactButton = document.querySelector("#contactButton");
let profileButton = document.querySelector("#profileButton");
let closeButton = document.querySelector("#closeButton");
let sideBar = document.querySelector(".sideBar");
let navBarButtons = document.querySelectorAll(".navBarButtons");

window.onload = function () {
  const token = localStorage.getItem("authToken");

  if (token) {
    try {
      // Pega o payload do JWT (a parte do meio)
      const payloadBase64 = token.split(".")[1];
      const payloadDecoded = JSON.parse(atob(payloadBase64));

      const nomeUsuario = payloadDecoded.sub || payloadDecoded.nome;

      if (nomeUsuario) {
        document.getElementById("userSideBarName").textContent = nomeUsuario;
      } else {
        console.warn("Nome de usuário não encontrado no token.");
      }
    } catch (error) {
      console.error("Erro ao decodificar o token:", error);
    }
  } else {
    console.warn("Token não encontrado no localStorage.");
  }
};

//Change Iframe Source
homeButton.addEventListener("click", () => {
  iframe.src = "../FrontEnd/iframes/home.html";
});
rentCarButton.addEventListener("click", () => {
  iframe.src = "../FrontEnd/iframes/rental.html";
});
aboutButton.addEventListener("click", () => {
  iframe.src = "../FrontEnd/iframes/about.html";
});
contactButton.addEventListener("click", () => {
  iframe.src = "../FrontEnd/iframes/contact.html";
});

//Select bottomBorder in each button
navBarButtons.forEach((button) => {
  button.addEventListener("click", () => {
    navBarButtons.forEach((btn) => {
      btn.style.borderBottom = "none";
    });

    button.style.borderBottom = "2px solid white";
  });
});

//Show sidebar
profileButton.addEventListener("click", () => {
  if (sideBar.style.display == "flex" && sideBar.style.height == "100vh") {
    sideBar.style.display = "none";
    sideBar.style.height = "0vh";
  } else {
    sideBar.style.display = "flex";
    sideBar.style.height = "100vh";
  }
});
closeButton.addEventListener("click", () => {
  sideBar.style.display = "none";
  sideBar.style.height = "0vh";
});
