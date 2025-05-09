let iframe = document.querySelector(".iframe");
let homeButton = document.querySelector("#homeButton");
let rentCarButton = document.querySelector("#rentCarButton");
let aboutButton = document.querySelector("#aboutButton");
let contactButton = document.querySelector("#contactButton");
let profileButton = document.querySelector("#profileButton");
let closeButton = document.querySelector("#closeButton");
let sideBar = document.querySelector(".sideBar");
let navBarButtons = document.querySelectorAll(".navBarButtons");
let gridRentedCars = document.querySelector(".gridRentedCars");
let registerHyperlink = document.querySelector("#registerHyperlink");
window.onload = function () {
  const token = sessionStorage.getItem("authToken");

  if (token) {
    try {
      // Pega o payload do JWT (a parte do meio)
      const payloadBase64 = token.split(".")[1];
      const payloadDecoded = JSON.parse(atob(payloadBase64));

      const nomeUsuario = payloadDecoded.sub || payloadDecoded.nome;

      if (nomeUsuario) {
        registerHyperlink.textContent = "Trocar De Conta";
        document.getElementById("userSideBarName").textContent = nomeUsuario;

        const optionsGET = {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        };

        const optionsPUT = {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        };

        fetch("http://localhost:8080/alugueis/meus-alugueis", optionsGET)
          .then((response) => response.json())
          .then((data) => {
            const gridRentedCars = document.querySelector(".gridRentedCars");
            gridRentedCars.innerHTML = ""; // limpa antes de preencher

            for (let index = 0; index < data.length; index++) {
              const aluguel = data[index];
              console.log(aluguel);
              // Cria a div do carro
              const eachCarDiv = document.createElement("div");
              eachCarDiv.className = "eachCarDiv";

              // Cria a div com as informações do carro
              const eachCarInfoDiv = document.createElement("div");
              eachCarInfoDiv.className = "eachCarInfoDiv";

              const carImage = document.createElement("img");
              const carName = document.createElement("h3");

              carImage.src = "../../uploads/" + aluguel.carro.fotoCarro;
              carName.textContent = aluguel.carro.modelo;

              eachCarInfoDiv.appendChild(carImage);

              // Adiciona o nome do carro

              eachCarInfoDiv.appendChild(carName);

              // Cria a div com os detalhes
              const detailsDiv = document.createElement("div");

              const totalValue = document.createElement("h3");
              totalValue.textContent = `Valor Total: R$${aluguel.valorTotal}`;

              const returnDate = document.createElement("h3");
              returnDate.textContent = `Dia da Devolução: ${aluguel.dataFim}`;

              detailsDiv.appendChild(totalValue);
              detailsDiv.appendChild(returnDate);
              eachCarInfoDiv.appendChild(detailsDiv);

              // Botão de devolução
              const returnButton = document.createElement("button");
              returnButton.textContent = "Devolver";
              returnButton.addEventListener("click", () => {
                fetch(
                  "http://localhost:8080/alugueis/devolver/" + aluguel.id,
                  optionsPUT
                )
                  .then((response) => {
                    if (!response.ok) {
                      return response.text().then((text) => {
                        throw new Error("Erro: " + text);
                      });
                    }
                    return response.json(); // AluguelResumoDTO retornado
                  })
                  .then((data) => {
                    eachCarDiv.remove(); // Remove do DOM
                    alert("Aluguel devolvido com sucesso!");
                  })
                  .catch((error) => {
                    console.error("Erro ao devolver:", error);
                    alert("Erro ao devolver aluguel: " + error.message);
                  });
              });
              eachCarInfoDiv.appendChild(returnButton);

              // Monta toda a estrutura
              eachCarDiv.appendChild(eachCarInfoDiv);
              gridRentedCars.appendChild(eachCarDiv);
            }
          })
          .catch((error) => {
            console.error("Erro ao carregar os aluguéis:", error);
          });

        // Exemplo de função para devolver (você pode ajustar conforme seu backend)
        function devolverAluguel(id) {
          fetch(`http://localhost:8080/alugueis/${id}/devolver`, {
            method: "PUT", // ou DELETE/POST conforme o seu backend
            headers: {
              "Content-Type": "application/json",
              // Adicione Authorization se necessário
            },
          })
            .then((res) => {
              if (res.ok) {
                alert("Aluguel devolvido com sucesso!");
                location.reload(); // Recarrega a lista
              } else {
                throw new Error("Falha ao devolver aluguel");
              }
            })
            .catch((err) => {
              console.error(err);
              alert("Erro ao processar devolução.");
            });
        }
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
