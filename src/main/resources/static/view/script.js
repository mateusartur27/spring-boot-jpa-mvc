const API = "http://localhost:8080/tasks";

/* ===============================
   CARREGAR TAREFAS (GET)
================================ */
async function carregarTasks() {

    const response = await fetch(API);
    const tasks = await response.json();

    renderizarLista(tasks);
}


function renderizarLista(tasks) {

    const lista = document.getElementById("listaTasks");

    lista.innerHTML = "";

    if (!tasks.length) {
        lista.innerHTML = "<li>Nenhum compromisso encontrado.</li>";
        return;
    }

    tasks.forEach(task => {

        const li = document.createElement("li");

        li.innerHTML = `
            <span>
                <strong>${task.data}</strong> as <strong>${task.hora}</strong> - ${task.descricao}
            </span>

            <button onclick="deletarTask(${task.id})">
                Deletar
            </button>

        `;

        lista.appendChild(li);

    });

}


/* ===============================
   CRIAR TAREFA (POST)
================================ */
async function criarTask() {

    const data = document.getElementById("data").value;
    const hora = document.getElementById("hora").value;
    const descricao = document.getElementById("descricao").value;

    if (!data || !hora || !descricao) {
        alert("Data, hora e descricao sao obrigatorias");
        return;
    }

    const response = await fetch(API, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            data: data,
            hora: hora,
            descricao: descricao
        })
    });

    if (!response.ok) {
        const error = await response.json();
        alert(error.erro || JSON.stringify(error));
        return;
    }

    document.getElementById("data").value = "";
    document.getElementById("hora").value = "";
    document.getElementById("descricao").value = "";

    carregarTasks();
}


/* ===============================
   FILTRAR POR DIA
================================ */
async function filtrarPorDia() {
    const dia = document.getElementById("filtroDia").value;
    if (!dia) {
        alert("Informe uma data para filtrar por dia");
        return;
    }

    const response = await fetch(`${API}?data=${dia}`);
    const tasks = await response.json();
    renderizarLista(tasks);
}


/* ===============================
   FILTRAR POR SEMANA
================================ */
async function filtrarPorSemana() {
    const diaReferencia = document.getElementById("filtroSemana").value;
    const url = diaReferencia ? `${API}/semana?data=${diaReferencia}` : `${API}/semana`;

    const response = await fetch(url);
    const tasks = await response.json();
    renderizarLista(tasks);
}


/* ===============================
   FILTRAR POR PERIODO
================================ */
async function filtrarPorPeriodo() {
    const inicio = document.getElementById("filtroInicio").value;
    const fim = document.getElementById("filtroFim").value;

    if (!inicio || !fim) {
        alert("Informe inicio e fim para filtrar por periodo");
        return;
    }

    const response = await fetch(`${API}?inicio=${inicio}&fim=${fim}`);

    if (!response.ok) {
        const error = await response.json();
        alert(error.erro || JSON.stringify(error));
        return;
    }

    const tasks = await response.json();
    renderizarLista(tasks);
}


/* ===============================
   DELETAR TAREFA (DELETE)
================================ */
async function deletarTask(id) {

    if (!confirm("Deseja realmente deletar esta tarefa?")) {
        return;
    }

    await fetch(`${API}/${id}`, {
        method: "DELETE"
    });

    carregarTasks();
}


/* ===============================
   CARREGAR AO ABRIR A PÁGINA
================================ */
window.onload = carregarTasks;