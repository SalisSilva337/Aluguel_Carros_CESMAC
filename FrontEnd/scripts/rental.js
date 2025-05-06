let modal = document.querySelector(".modal");
let registerCarModalButton = document.querySelector(".registerCarModalButton");
let registerCarButton = document.querySelector(".registerCarButton");
let inputCarName = document.querySelector("#inputCarName");
let inputCarYear = document.querySelector("#inputCarYear");
let inputCarPrice = document.querySelector("#inputCarPrice");
let imgCar = document.querySelector("#imgCar");
let inputImgCar = document.querySelector("#inputImgCar");
let gridSection = document.querySelector(".gridSection");

const token = localStorage.getItem("authToken");

//open modal
registerCarButton.addEventListener("click", () => {
  modal.style.display = "flex";
});
inputImgCar.addEventListener("input", () => {
  imgCar.src = URL.createObjectURL(inputImgCar.files[0]);
});
//close modal
window.onclick = function (event) {
  if (event.target === modal) {
    modal.style.display = "none";
  }
};

//register car funct
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

  console.log(inputCarName.value);
  const formData = new FormData();
  formData.append("modelo", inputCarName.value);
  formData.append("ano", inputCarYear.value);
  formData.append("precoPorDia", inputCarPrice.value);
  formData.append("disponivel", true);
  formData.append("fotoCarro", inputImgCar.files[0]);

  console.log(formData);

  const optionsPOST = {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    body: formData,
  };

  fetch("http://localhost:8080/carros", optionsPOST)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
    });
});

window.onload = function loadCars() {
  const optionsGET = {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };

  fetch("http://localhost:8080/carros/disponiveis", optionsGET)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);

      for (let index = 0; index < data.length; index++) {
        const grid = document.createElement("div");
        grid.classList.add("grid");

        // Cria a imagem do carro
        const img = document.createElement("img");
        img.classList.add("imgGrid");
        img.src = "../../uploads/" + data[index].fotoCarro;

        // Cria o nome do carro
        const carName = document.createElement("h3");
        carName.classList.add("carNameGrid");
        carName.textContent = data[index].modelo;

        // Cria o ano do carro
        const yearCar = document.createElement("span");
        yearCar.classList.add("yearCarGrid");
        yearCar.textContent = `Ano: ${data[index].ano}`;

        // Cria o preço do carro
        const priceCar = document.createElement("span");
        priceCar.classList.add("priceCarGrid");
        priceCar.textContent = `Valor da Diária: R$${data[index].precoPorDia}`;

        // Cria a label para Data de Início
        const dateStartLabel = document.createElement("span");
        dateStartLabel.textContent = "Data Inicio:";

        // Cria o input para Data de Início
        const inputDateStart = document.createElement("input");
        inputDateStart.classList.add("inputDateGrid");
        inputDateStart.type = "date";
        inputDateStart.min = "2025-01-01";

        // Cria a label para Data Final
        const dateEndLabel = document.createElement("span");
        dateEndLabel.textContent = "Data Final:";

        // Cria o input para Data Final
        const inputDateEnd = document.createElement("input");
        inputDateEnd.classList.add("inputDateGrid");
        inputDateEnd.type = "date";
        inputDateEnd.min = "2025-01-01";

        // Cria o botão de alugar
        const rentCarButton = document.createElement("button");
        rentCarButton.classList.add("rentCarButton");
        rentCarButton.textContent = "ALUGAR";

        // Adiciona os elementos criados à div (grid)
        grid.appendChild(img);
        grid.appendChild(carName);
        grid.appendChild(yearCar);
        grid.appendChild(priceCar);
        grid.appendChild(dateStartLabel);
        grid.appendChild(inputDateStart);
        grid.appendChild(dateEndLabel);
        grid.appendChild(inputDateEnd);
        grid.appendChild(rentCarButton);
        gridSection.appendChild(grid);
      }
    });
};
