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

document
  .getElementById("buttonLogin")
  .addEventListener("click", function (event) {
    event.preventDefault(); // Evita o comportamento padrão do botão de redirecionamento

    // Pegando os valores dos inputs
    let userName = document.getElementById("inputUserName").value.trim();
    let password = document.getElementById("inputPassword").value.trim();

    // Validação simples dos campos
    if (userName === "" || password === "") {
      alert("Por favor, preencha todos os campos.");
      return;
    }

    // Dados do usuário para login
    let loginUser = {
      nome: userName,
      senha: password,
    };

    // Configuração da requisição fetch
    const optionsPOST = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginUser), // Convertendo os dados do usuário para JSON
    };

    // Enviando requisição para o servidor
    fetch("http://localhost:8080/usuarios/login", optionsPOST)
      .then((response) => response.json()) // Espera resposta em JSON
      .then((data) => {
        // Se o login for bem-sucedido, armazena o token e redireciona
        if (data.token) {
          // Armazenando o token JWT (você pode usar localStorage, sessionStorage ou cookies)
          localStorage.setItem("authToken", data.token);
          alert("Login bem-sucedido!");
          window.location.href = "index.html"; // Redireciona para a página inicial
        } else {
          // Caso haja erro, exibe a mensagem
          alert(data.erro || "Erro ao fazer login.");
        }
      })
      .catch((error) => {
        console.error("Erro na requisição:", error);
        alert("Erro ao conectar ao servidor. Tente novamente.");
      });
  });
