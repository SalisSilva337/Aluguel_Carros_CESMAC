let iframe = document.querySelector(".iframe");
let homeButton = document.querySelector("#homeButton");
let rentCarButton = document.querySelector("#rentCarButton");
let aboutButton = document.querySelector("#aboutButton");
let contactButton = document.querySelector("#contactButton");
let profileButton = document.querySelector("#profileButton");
let closeButton = document.querySelector("#closeButton");
let sideBar = document.querySelector(".sideBar");
let navBarButtons = document.querySelectorAll(".navBarButtons");

//Change Iframe Source
homeButton.addEventListener("click", () => {
  iframe.src = "../iframes/home.html";
});
rentCarButton.addEventListener("click", () => {
  iframe.src = "../iframes/rental.html";
});
aboutButton.addEventListener("click", () => {
  iframe.src = "../iframes/about.html";
});
contactButton.addEventListener("click", () => {
  iframe.src = "../iframes/contact.html";
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

let options = [(method = "GET")];
fetch("carros/todoscarros", options)
  .then((response) => response.json())
  .then((response) => {
    for (let index = 0; index < response.length; index++) {
      response.nome;
      response.dataInicial;
    }
  });
