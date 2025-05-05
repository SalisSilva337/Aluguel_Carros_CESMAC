let modal = document.querySelector(".modal");
let registerCarModalButton = document.querySelector(".registerCarModalButton");
let registerCarButton = document.querySelector(".registerCarButton");
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
registerCarModalButton.addEventListener("click", () => {
  event.preventDefault();

  if (
    inputNomeJogo.value.trim() === "" ||
    inputDescricaoJogo.value.trim() === "" ||
    inputCapaJogo.value.trim() === "" ||
    inputPosterJogo.value.trim() === "" ||
    inputPrecoJogo.value.trim() === "" ||
    inputPlataformaJogo.value.trim() === "" ||
    inputScreenshot1Jogo.value.trim() === "" ||
    inputScreenshot2Jogo.value.trim() === ""
  ) {
    alert("Por favor, preencha todos os campos antes de salvar.");
    return;
  }

  let addNewCarJSON = {
    modelo: inputNomeJogo.value,
    ano: inputDescricaoJogo.value,
    precoPorDia: inputCapaJogo.value,
    disponivel: true,
    fotoCarro: inputPrecoJogo.value,
  };

  console.log(JSON.stringify(addNewCarJSON));

  const optionsPOST = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(addNewCarJSON),
  };

  fetch("http://localhost:8080/giggagames/v1", optionsPOST)
    .then((response) => response)
    .then((data) => {
      console.log(data);
    });
});
