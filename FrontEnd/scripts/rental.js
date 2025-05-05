let modal = document.querySelector(".modal");
let registerCarModalButton = document.querySelector(".registerCarModalButton");
let registerCarButton = document.querySelector(".registerCarButton");
let inputCarName = document.querySelector("#inputCarName");
let inputCarYear = document.querySelector("#inputCarYear");
let inputCarPrice = document.querySelector("#inputCarPrice");

//open modal
registerCarButton.addEventListener("click", () => {
  modal.style.display = "flex";
});
//close modal
window.onclick = function (event) {
  if (event.target === modal) {
    modal.style.display = "none";
  }
};
registerCarModalButton.addEventListener("click", (event) => {
  event.preventDefault();

  if (
    inputCarName.value.trim() === "" ||
    inputCarYear.value.trim() === "" ||
    inputCarPrice.value.trim() === ""
  ) {
    alert("Por favor, preencha todos os campos antes de salvar.");
    return;
  }

  let addNewCarJSON = {
    modelo: inputCarName.value,
    ano: inputCarYear.value,
    precoPorDia: inputCarPrice.value,
    disponivel: true,
    fotoCarro: inputImgCar.value
  };

  console.log(JSON.stringify(addNewCarJSON));

  const optionsPOST = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      
    },
    body: JSON.stringify(addNewCarJSON),
  };

  fetch("http://localhost:8080/carros", optionsPOST)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
    });
});
