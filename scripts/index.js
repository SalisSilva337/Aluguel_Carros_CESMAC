let iframe = document.querySelector(".iframe");
let homeButton = document.querySelector("#homeButton");
let rentCarButton = document.querySelector("#rentCarButton");
let aboutButton = document.querySelector("#aboutButton");
let contactButton = document.querySelector("#contactButton");

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
