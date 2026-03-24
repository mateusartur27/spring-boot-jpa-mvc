# Agenda de Compromissos

Aplicação Spring Boot para gerenciar compromissos com datas, horários e filtros por período (dia, semana, período customizado).

## 🎯 Visão Geral

Sistema desenvolvido seguindo os padrões MVC com separação clara entre camadas:
- **Model**: Entidades JPA com validações
- **Repository**: Acesso aos dados com Spring Data JPA
- **Service**: Regras de negócio e cálculos
- **Controller**: API REST
- **View**: Frontend separado consumindo a API

## 🛠️ Stack Tecnológico

- **Java 25** (Zulu)
- **Spring Boot 4.0.3**
- **Spring Data JPA**
- **H2 Database** (em memória)
- **Gradle**
- **HTML5 + CSS3 + JavaScript (Vanilla)**

## 📋 Requisitos

- JDK 25+
- Gradle 9.3.1+

## 🚀 Como Executar

### 1. Clone o repositório
```bash
git clone https://github.com/[seu-usuario]/agenda-compromissos.git
cd agenda-compromissos
```

### 2. Execute a aplicação
```bash
./gradlew bootRun
```

A aplicação será iniciada em `http://localhost:8080`

### 3. Acesse a interface web
Abra no navegador:
```
http://localhost:8080/view/index.html
```

## 📚 Funcionalidades Principais

### 1. Criar Compromisso
- Adicione um compromisso com data, hora e descrição
- Validação automática (não permite datas passadas)

### 2. Listar Compromissos
- Visualize todos os compromissos
- Ordenados por data e hora

### 3. Filtros Avançados
- **Por Dia**: Veja compromissos de um dia específico
- **Por Semana**: Compromissos de domingo a sábado (semana completa)
- **Por Período**: Intervalo customizado (início e fim)

### 4. Atualizar e Deletar
- Edite compromissos existentes
- Remova quando não for mais necessário

## 🔍 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/todolist/
│   │   ├── TodolistApplication.java       (entry point)
│   │   ├── model/Task.java                (entidade)
│   │   ├── repository/TaskRepository.java (acesso dados)
│   │   ├── service/TaskService.java       (regras negócio)
│   │   ├── controller/TaskController.java (rotas API)
│   │   └── exception/GlobalExceptionHandler.java (tratamento erros)
│   └── resources/
│       ├── application.properties
│       └── static/view/
│           ├── index.html
│           ├── script.js
│           └── style.css
└── test/
    └── java/com/todolist/...
```

## 🔐 Validações

### Na Entidade (Model)
Arquivo: `src/main/java/com/todolist/model/Task.java`

- `@NotNull`: Data obrigatória
- `@NotNull`: Hora obrigatória
- `@NotBlank`: Descrição obrigatória
- `@FutureOrPresent`: Data não pode ser no passado

### Na Camada de Negócio (Service)
Arquivo: `src/main/java/com/todolist/service/TaskService.java`

Método `validateDateTime(Task task)`:
- Valida combinação data + hora não pode estar no passado
- Lança `IllegalArgumentException` com mensagem clara

## 📡 API REST - Endpoints

### Base URL
```
http://localhost:8080
```

### 1. Criar Compromisso
```http
POST /tasks
Content-Type: application/json

{
  "data": "2026-03-26",
  "hora": "14:30:00",
  "descricao": "Reuniao de projeto"
}
```
**Resposta**: `201 Created`

### 2. Listar Todos
```http
GET /tasks
```
**Resposta**: `200 OK` com array de compromissos

### 3. Buscar por ID
```http
GET /tasks/1
```
**Resposta**: `200 OK` ou `404 Not Found`

### 4. Atualizar Compromisso
```http
PUT /tasks/1
Content-Type: application/json

{
  "data": "2026-03-27",
  "hora": "09:00:00",
  "descricao": "Consulta médica"
}
```
**Resposta**: `200 OK`

### 5. Deletar Compromisso
```http
DELETE /tasks/1
```
**Resposta**: `204 No Content`

### 6. Filtrar por Dia
```http
GET /tasks?data=2026-03-26
```
**Resposta**: `200 OK` com compromissos do dia

### 7. Filtrar por Semana
```http
GET /tasks/semana?data=2026-03-26
```
Retorna compromissos de domingo a sábado (semana contendo a data informada)
**Resposta**: `200 OK`

### 8. Filtrar por Período
```http
GET /tasks?inicio=2026-03-24&fim=2026-03-31
```
**Resposta**: `200 OK` com compromissos no intervalo

## 🧪 Testando com Postman

### Exemplo 1: Criar um Compromisso
1. Método: `POST`
2. URL: `http://localhost:8080/tasks`
3. Headers: `Content-Type: application/json`
4. Body (raw):
```json
{
  "data": "2026-03-26",
  "hora": "14:30:00",
  "descricao": "Reuniao de projeto"
}
```

### Exemplo 2: Filtrar por Dia
1. Método: `GET`
2. URL: `http://localhost:8080/tasks?data=2026-03-26`

### Exemplo 3: Filtrar por Período
1. Método: `GET`
2. URL: `http://localhost:8080/tasks?inicio=2026-03-24&fim=2026-03-31`

## Formatos de Data e Hora

- **Data**: `AAAA-MM-DD` (ISO 8601)
- **Hora**: `HH:MM:SS` (formato 24h)
- **Exemplo**: `2026-03-26` às `14:30:00`

## 🚨 Tratamento de Erros

| Código | Cenário |
|--------|---------|
| `400 Bad Request` | Data obrigatória, hora faltando, data no passado, período inválido |
| `404 Not Found` | Compromisso com ID inexistente |
| `201 Created` | Compromisso criado com sucesso |
| `200 OK` | Operação bem-sucedida (GET, PUT) |
| `204 No Content` | Deletado com sucesso |

## 📊 Exemplos de Resposta

### GET /tasks (sucesso)
```json
[
  {
    "id": 1,
    "data": "2026-03-26",
    "hora": "14:30:00",
    "descricao": "Reuniao de projeto"
  },
  {
    "id": 2,
    "data": "2026-03-27",
    "hora": "09:00:00",
    "descricao": "Consulta médica"
  }
]
```

### POST /tasks (erro - data no passado)
```json
{
  "erro": "Nao e permitido cadastrar compromisso no passado"
}
```

## 🔧 Configuração

### application.properties
```properties
spring.application.name=todolist

# Database
spring.datasource.url=jdbc:h2:mem:todolistdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# H2 Console (acesso visual ao banco)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Acesse o H2 Console em: `http://localhost:8080/h2-console`

## 🎓 Conhecimentos Aplicados

### Manipulação de Datas (LocalDate, LocalDateTime)
- `LocalDate`: Armazena apenas a data (sem hora)
- `LocalTime`: Armazena apenas a hora (sem data)
- `LocalDateTime`: Combinação de data + hora para validações
- `TemporalAdjusters`: Cálculos de semana (SUNDAY a SATURDAY)

### Filtros por Período
- Queries customizadas no Repository com `@Query` ou derivadas
- `findByDataBetweenOrderByDataAscHoraAsc`: retorna compromissos em intervalo
- Lógica de semana: domingo a sábado (ISO-8601)

### Validação de Datas
- **Anotações**: `@NotNull`, `@FutureOrPresent`
- **Regras de Negócio**: validação em `Service` antes de salvar
- **Resposta HTTP 400**: para erros de validação

## 👨‍💻 Autor

Desenvolvido como projeto prático de Spring Boot com foco em boas práticas de arquitetura em camadas.

## 📝 Licença

MIT - Livre para usar, modificar e distribuir.

---

**Última atualização**: Março de 2026
