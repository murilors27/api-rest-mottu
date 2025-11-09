<p align="center">
  <img src="https://upload.wikimedia.org/wikipedia/commons/4/4e/Logo_FIAP.png" width="200">
</p>

<h3 align="center">Global Solution — Java Advanced (Sprint Final)</h3>
<p align="center">Projeto desenvolvido como parte da disciplina de Java Advanced (FIAP 2025)</p>

---

# 🏍️ Mottu Tracker — Sistema de Rastreamento de Motos com UWB

O **Mottu Tracker** é uma solução completa de **gestão e rastreamento de motos** desenvolvida para a empresa **Mottu**, com foco em **eficiência operacional, precisão e escalabilidade**.  
A proposta surgiu a partir do desafio real de gerenciar a localização das motos em pátios de múltiplas filiais, utilizando sensores **UWB (Ultra Wideband)** para identificar a posição exata de cada veículo.

A aplicação é composta por três camadas integradas:
- 🧠 **API Java Spring Boot** — responsável por toda a lógica de negócio e persistência dos dados.  
- 🌐 **Aplicação Web (Thymeleaf)** — interface administrativa para cadastro e controle.  
- 📱 **Aplicativo Mobile (React Native)** — acesso rápido e visualização de motos, com modo escuro e integração em tempo real.

---

## 🎯 Objetivo do Projeto

A Mottu possui mais de 100 filiais distribuídas pelo Brasil e México.  
A dificuldade em localizar motos dentro dos pátios afeta a produtividade e o tempo de operação.  
O **Mottu Tracker** vem como solução para:

- Identificar com precisão a localização das motos dentro dos pátios;
- Fornecer visualização em tempo real da disposição das unidades;
- Centralizar os cadastros e operações de manutenção e alocação;
- Reduzir erros humanos e otimizar o controle da frota.

---

## ⚙️ Tecnologias Utilizadas

### 💻 **Back-end (API Java)**
- Java 17  
- Spring Boot 3.4.5  
- Spring Web  
- Spring Data JPA  
- Spring Security (autenticação Basic e roles ADMIN/USER)  
- Bean Validation  
- Flyway (versionamento do banco)  
- PostgreSQL  
- Maven  

### 🌐 **Front-end Web**
- Thymeleaf + Bootstrap  
- Fragments reutilizáveis (_head, _navbar, _footer_)  
- CSS customizado para identidade visual (modo escuro e neon verde Mottu)  

### 📱 **Mobile**
- React Native (Expo + TypeScript)  
- Axios (consumo da API Java com autenticação Basic)  
- Context API (tema global e autenticação)  
- AsyncStorage (armazenamento local)  
- React Navigation  
- Dark/Light Mode  

---

## 🧩 Arquitetura Geral

A solução segue a arquitetura **MVC + REST**, dividida em camadas:

```
Controller → Service → Repository → Entity → Database
```

- **Controllers:** recebem as requisições HTTP (GET, POST, PUT, DELETE).
- **Services:** contêm a lógica de negócio (validações e regras).
- **Repositories:** fazem a persistência com o banco via JPA.
- **Entities:** representam as tabelas (Moto, Sensor, Alocação, Manutenção).
- **Database:** versionado via Flyway com scripts SQL e seeds iniciais.

---

## 🗂️ Funcionalidades da Aplicação

### 🏍️ **Motos**
- CRUD completo (cadastro, listagem, edição, exclusão)
- Validação de campos obrigatórios
- Bloqueio de exclusão se a moto estiver alocada ou em manutenção
- Implementação de **soft delete** para preservar histórico

### 📡 **Sensores UWB**
- Cadastro e gerenciamento de sensores vinculados às motos
- Validação de IDs e vínculos ativos

### 🔄 **Alocações**
- Abertura e encerramento de alocações
- Moto só pode ser alocada se estiver DISPONÍVEL
- Histórico de alocações encerradas mantido no banco

### 🧰 **Manutenções**
- Controle de motos em manutenção
- Mudança automática de status da moto (MANUTENÇÃO / DISPONÍVEL)
- Impede alocação de motos em manutenção

### 🔐 **Segurança**
- Login via autenticação Basic (ADMIN e USER)
- Controle de acesso:
  - **ADMIN:** pode criar, editar e excluir
  - **USER:** apenas visualiza os registros

---

## 🌍 Integração com o Aplicativo Mobile

O aplicativo **Mottu Tracker Mobile** se conecta diretamente à API Java, consumindo os endpoints REST.  
Funcionalidades:

- Login com autenticação Basic  
- Listagem de motos em tempo real  
- Cadastro e exclusão de motos  
- Feedback visual com mensagens de erro detalhadas  
- **Modo claro e escuro** integrado ao sistema  
- **Soft delete** refletido na listagem do app  

**Tecnologias:** React Native, TypeScript, Axios, Expo, Context API.  

---

## 🌐 Aplicação Web (Thymeleaf)

A interface web foi desenvolvida para uso administrativo e segue a identidade visual da Mottu:  
- Tema escuro com detalhes em verde neon.  
- Validações visuais com alertas e feedbacks.  
- Controle de acesso de acordo com o tipo de usuário.  
- Fragments reutilizados para padrão visual e performance.

---

## 🚀 Deploy e Acesso

A aplicação está hospedada no **Render**, com acesso público.  
O app mobile consome diretamente o endpoint do serviço online.

**URL do Deploy:** [[deploy](https://api-rest-mottu.onrender.com)]  
**Swagger (documentação REST):** `/swagger-ui/index.html`

**Usuários de teste:**
| Tipo | Login | Senha |
|------|--------|--------|
| ADMIN | admin | admin123 |
| USER | user | user123 |

---

## 🧩 Endpoints Principais da API

### 📌 Motos
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| GET | `/api/motos` | Lista todas as motos |
| GET | `/api/motos/{id}` | Retorna uma moto específica |
| POST | `/api/motos` | Cria nova moto |
| PUT | `/api/motos/{id}` | Atualiza dados de uma moto |
| DELETE | `/api/motos/{id}` | Soft delete da moto |

### 📡 Sensores
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| GET | `/api/sensores` | Lista sensores |
| POST | `/api/sensores` | Cadastra sensor |
| PUT | `/api/sensores/{id}` | Atualiza sensor |
| DELETE | `/api/sensores/{id}` | Remove sensor |

### 🔄 Alocações
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| GET | `/api/alocacoes` | Lista alocações |
| POST | `/api/alocacoes` | Cria nova alocação |
| PUT | `/api/alocacoes/{id}` | Encerra alocação |

### 🧰 Manutenções
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| GET | `/api/manutencoes` | Lista manutenções |
| POST | `/api/manutencoes` | Abre manutenção |
| PUT | `/api/manutencoes/{id}` | Fecha manutenção |

---

## 🧪 Como Rodar Localmente

### 1️⃣ Clonar o repositório:
```
git clone https://github.com/murilors27/api-rest-mottu.git
cd api-rest-mottu
```

### 2️⃣ Configurar o banco no `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mottu
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  flyway:
    enabled: true
```

### 3️⃣ Executar o projeto:
```
./mvnw spring-boot:run
```

### 4️⃣ Acessar:
- Web: [http://localhost:8080/motos](http://localhost:8080/motos)
- Swagger: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 📱 Integração com o App Mobile

### Repositório:
🔗 [Mottu Tracker Mobile (GitHub)](https://github.com/murilors27/mottu-tracker-mobile)

### Instalação:
```
npm install
npx expo start
```

### Configuração:
No arquivo `api.ts`, atualize o IP local:
```ts
baseURL: "http://SEU_IP_LOCAL:8080/api/motos"
```

### Funcionalidades do app:
- Login com autenticação Basic  
- Listagem de motos com indicador de status (🟢 disponível, 🟡 alocada, 🔧 manutenção)  
- Cadastro, edição e exclusão de motos  
- Mensagens personalizadas de erro e sucesso  
- Modo escuro/claro integrado  
- Design inspirado na identidade visual da Mottu  

---

## 🧱 Estrutura do Banco (Flyway)

| Versão | Script | Descrição |
|---------|---------|-----------|
| V1 | `create_sensores.sql` | Criação da tabela de sensores |
| V2 | `create_motos.sql` | Criação da tabela de motos |
| V3 | `insert_sensores.sql` | Dados iniciais de sensores |
| V4 | `insert_motos.sql` | Dados iniciais de motos |
| V5–V9 | Roles, Users, Alocações e Manutenções | Controle de acesso e relacionamentos |
| V10 | `insert_sensores_padronizados.sql` | Inserção de novo modelo de sensor |
| V11 | `insert_motos_padronizadas.sql` | Inserção de novo modelo de moto |
| V12 | `enforce_sensor_format.sql` | Garante padronização no sensor |

---

## 🧠 Integração Multidisciplinar

| Disciplina | Aplicação na Solução |
|-------------|----------------------|
| **Java Advanced** | Desenvolvimento da API REST com CRUDs, validações e segurança |
| **.NET Development** | Criação de CRUD paralelo com EF Core e PostgreSQL |
| **Mobile Development** | App React Native com consumo da API Java |
| **Banco de Dados** | Modelagem relacional e versionamento com Flyway |
| **DevOps / Cloud** | Deploy da aplicação na nuvem (Render) |
| **Design e UX** | Interface moderna e responsiva, com tema escuro e feedback visual |

---

## 🎥 Apresentação e Demonstração Técnica

📽️ **Roteiro do vídeo:**
1. Introdução — contexto e problema da Mottu  
2. Demonstração da API no Swagger  
3. Demonstração do app web (Thymeleaf)  
4. Demonstração do app mobile (React Native)  
5. Encerramento — resultados e próximos passos  

🔗 *Link para o vídeo:* [em breve]

---

## 👥 Equipe de Desenvolvimento

| Nome                                | RM       | GitHub                                |
|-------------------------------------|----------|----------------------------------------|
| **Murilo Ribeiro Santos**           | RM555109 | [@murilors27](https://github.com/murilors27) |
| **Thiago Garcia Tonato**            | RM99404  | [@thiago-tonato](https://github.com/thiago-tonato) |
| **Ian Madeira Gonçalves da Silva**  | RM555502 | [@IanMadeira](https://github.com/IanMadeira) |

**Curso:** Análise e Desenvolvimento de Sistemas  
**Instituição:** FIAP — Faculdade de Informática e Administração Paulista  
**Ano:** 2025

---

<p align="center">💚 Projeto desenvolvido com dedicação e tecnologia, para otimizar a gestão de frotas da Mottu. 💚</p>
