document
  .getElementById("buttonRegisterAccount")
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

    // Dados do novo usuário
    let newUser = {
      nome: userName,
      senha: password,
    };

    // Configuração da requisição fetch
    const optionsPOST = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newUser), // Convertendo os dados do usuário para JSON
    };

    // Enviando requisição para o servidor
    fetch("http://localhost:8080/usuarios", optionsPOST)
      .then((response) => response.json()) // Espera resposta em JSON
      .then((data) => {
        // Se o cadastro for bem-sucedido, exibe uma mensagem
        if (data.mensagem && data.mensagem === "Usuário criado com sucesso") {
          alert("Cadastro realizado com sucesso!");
          window.location.href = "login.html"; // Redireciona para a página de login
        } else {
          // Caso haja algum erro, exibe a mensagem
          alert(data.erro || "Erro ao cadastrar o usuário.");
        }
      })
      .catch((error) => {
        console.error("Erro na requisição:", error);
        alert("Erro ao conectar ao servidor. Tente novamente.");
      });
  });
