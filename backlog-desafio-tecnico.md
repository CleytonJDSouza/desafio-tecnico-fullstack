# Backlog — Desafio Técnico Fullstack (CRUD Usuário + Endereço)

**Prazo:** 9 dias corridos (09/07 a 17/07)
**Squad:** Cleyton José de Souza
**Prioridade:** 🔴 Crítico | 🟡 Importante | 🟢 Desejável

## SPRINT 1 — Setup e Backend Base
📅 Dia 1 (Qui 09/07 - feriado)

- [X] **TASK-01** 🔴 Criar projeto Spring Boot (Spring Initializr) com dependências: Web, JPA, H2, PostgreSQL Driver, Validation, Lombok
- [X] **TASK-02** 🔴 Configurar `application.yml`/`application.properties` com dois profiles: `dev` (H2) e `prod` (PostgreSQL)
- [X] **TASK-03** 🔴 Criar estrutura de pacotes: `controller`, `service`, `repository`, `model/entity`, `dto`, `exception`
- [X] **TASK-04** 🔴 Criar entidade `User` (id, nome, email, telefone) com validações via Bean Validation (`@NotBlank`, `@Email`)
- [X] **TASK-05** 🔴 Criar entidade `Address` (id, cep, rua, número, estado, cidade, bairro) com relacionamento `@ManyToOne` para `User`
- [X] **TASK-06** 🔴 Mapear relacionamento `User` (1) → `Address` (N) com `@OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)`
- [X] **TASK-07** 🟡 Subir projeto e validar se tabelas são criadas corretamente no H2 (console habilitado)
- [X] **TASK-08** 🔴 Criar repositório GitHub + primeiro commit ("setup inicial do projeto")

**Critério de aceite da Sprint 1:** projeto sobe sem erro, entidades mapeadas, tabelas criadas no H2.

---

## SPRINT 2 — CRUD Usuário (Backend)
📅 Dia 2 (Sex 10/07 - noite)

- [X] **TASK-09** 🔴 Criar `UserRepository extends JpaRepository<User, Long>`
- [X] **TASK-10** 🔴 Criar `UserDTO` (request) e `UserResponseDTO` (response, incluindo lista de endereços)
- [X] **TASK-11** 🔴 Implementar `UserService`: métodos `listAll()`, `findById()`, `create()`, `update()`, `delete()`
- [X] **TASK-12** 🔴 Implementar `UserController` com endpoints:
  - `GET /api/users` — listar todos
  - `GET /api/users/{id}` — buscar por ID (com endereços)
  - `POST /api/users` — criar
  - `PUT /api/users/{id}` — atualizar
  - `DELETE /api/users/{id}` — remover
- [X] **TASK-13** 🔴 Criar tratamento de exceção global (`@RestControllerAdvice`) para erros 404 (usuário não encontrado) e 400 (validação)
- [X] **TASK-14** 🟡 Testar endpoints manualmente via Postman/Insomnia
- [X] **TASK-15** 🔴 Commit: "CRUD de usuário implementado"

**Critério de aceite da Sprint 2:** todos os endpoints de usuário funcionando via Postman, sem endereço ainda.

---

## SPRINT 3 — CRUD Endereço + Integração ViaCEP
📅 Dia 3 e 4 (Sáb 11/07 noite + Dom 12/07)

- [X] **TASK-16** 🔴 Criar `AddressRepository extends JpaRepository<Address, Long>`
- [X] **TASK-17** 🔴 Criar `AddressDTO` (request/response)
- [X] **TASK-18** 🔴 Criar `ViaCepClient` (RestTemplate ou WebClient) para consumir `https://viacep.com.br/ws/{cep}/json`
- [X] **TASK-19** 🔴 Criar `CepValidationService`: valida formato do CEP + consulta ViaCEP + trata retorno `"erro": true` (CEP inexistente)
- [X] **TASK-20** 🔴 Integrar validação de CEP no fluxo de criação/atualização de endereço — bloquear com erro 400 se CEP inválido
- [X] **TASK-21** 🔴 Ao validar CEP com sucesso, preencher automaticamente rua/bairro/cidade/estado retornados pela ViaCEP (regra de negócio a confirmar, mas recomendado)
- [X] **TASK-22** 🔴 Implementar endpoints de endereço vinculados ao usuário (criar/atualizar endereço dentro do fluxo de usuário, conforme endpoints já especificados)
- [X] **TASK-23** 🟡 Criar `CepValidationServiceTest` — testes unitários mockando resposta da ViaCEP (sucesso, CEP inválido, erro de conexão)
- [X] **TASK-24** 🟡 Criar testes de repositório (`@DataJpaTest`) para `UserRepository` e `AddressRepository`
- [X] **TASK-25** 🔴 Commit: "integração ViaCEP + CRUD endereço + testes unitários"

**Critério de aceite da Sprint 3:** CEP inválido bloqueia a operação; testes unitários passando; endereço vinculado corretamente ao usuário.

---

## SPRINT 4 — Frontend: Base e Listagem
📅 Dia 5 (Seg 13/07 - noite)

- [X] **TASK-26** 🔴 Criar projeto Angular (`ng new`), configurar roteamento e HttpClientModule
- [X] **TASK-27** 🔴 Criar `UserService` (Angular) com métodos consumindo a API (`getAll`, `getById`, `create`, `update`, `delete`)
- [X] **TASK-28** 🔴 Criar model/interface `User` e `Address` no frontend (TypeScript)
- [X] **TASK-29** 🔴 Criar componente `user-list` — tabela com nome, e-mail, telefone e ações (ver/editar/excluir)
- [ ] **TASK-30** 🔴 Configurar rotas principais (`/users`, `/users/:id`, `/users/new`, `/users/:id/edit`)
- [ ] **TASK-31** 🟡 Estilizar listagem (algo simples e limpo, sem precisar de framework CSS pesado)
- [ ] **TASK-32** 🔴 Commit: "estrutura base do frontend + listagem de usuários"

**Critério de aceite da Sprint 4:** tela lista usuários reais vindos da API.

---

## SPRINT 5 — Frontend: Detalhe e CRUD Usuário
📅 Dia 6 (Ter 14/07 - noite)

- [ ] **TASK-33** 🔴 Criar componente `user-detail` — exibir dados do usuário + lista de endereços vinculados
- [ ] **TASK-34** 🔴 Criar componente `user-form` (reativo) — cadastro e edição de usuário (reaproveitar componente para os dois casos)
- [ ] **TASK-35** 🔴 Implementar validações no formulário (nome obrigatório, e-mail válido, telefone obrigatório)
- [ ] **TASK-36** 🔴 Implementar ação de exclusão de usuário (com confirmação)
- [ ] **TASK-37** 🟡 Tratar estados vazios (usuário sem endereço cadastrado ainda)
- [ ] **TASK-38** 🔴 Commit: "tela de detalhe + CRUD completo de usuário no frontend"

**Critério de aceite da Sprint 5:** fluxo completo de criar, ver, editar e excluir usuário funcionando de ponta a ponta.

---

## SPRINT 6 — Frontend: CRUD Endereço
📅 Dia 7 (Qua 15/07 - noite)

- [ ] **TASK-39** 🔴 Criar componente `address-form` — cadastro e edição de endereço
- [ ] **TASK-40** 🔴 Implementar busca automática ao digitar/sair do campo CEP (chamando o backend, que valida via ViaCEP)
- [ ] **TASK-41** 🔴 Preencher automaticamente rua/bairro/cidade/estado ao validar CEP
- [ ] **TASK-42** 🔴 Implementar ação de adicionar múltiplos endereços a um mesmo usuário
- [ ] **TASK-43** 🟡 Implementar edição e exclusão de endereço específico
- [ ] **TASK-44** 🔴 Commit: "CRUD completo de endereço no frontend"

**Critério de aceite da Sprint 6:** usuário consegue ter múltiplos endereços, todos validados via CEP.

---

## SPRINT 7 — UX, Testes Finais e Entrega
📅 Dia 8 (Qui 16/07)

- [ ] **TASK-45** 🟡 Implementar spinner global (interceptor HTTP no Angular) durante chamadas à API
- [ ] **TASK-46** 🟡 Implementar toasts (ex: `ngx-toastr`) para sucesso, erro e CEP inválido
- [ ] **TASK-47** 🟡 Revisar mensagens de erro do backend para serem amigáveis no frontend
- [ ] **TASK-48** 🔴 Rodar todos os testes unitários do backend e garantir que passam (`mvn test`)
- [ ] **TASK-49** 🔴 Testar fluxo completo manualmente (criar usuário → adicionar endereço → editar → excluir)
- [ ] **TASK-50** 🔴 Escrever `README.md`:
  - Descrição do projeto
  - Tecnologias utilizadas
  - Como rodar backend (profiles dev/prod)
  - Como rodar frontend
  - Como rodar os testes
  - Prints ou GIF da aplicação (opcional, mas soma pontos)
- [ ] **TASK-51** 🟢 Revisar código (remover comentários de debug, `console.log`, imports não usados)
- [ ] **TASK-52** 🔴 Push final no GitHub + conferir se o repositório está público/acessível
- [ ] **TASK-53** 🔴 Enviar entrega com o link do repositório

**Critério de aceite final:** aplicação rodando localmente seguindo apenas o README, sem passos "escondidos".

---

## Regra de corte, se o prazo apertar

Se algum dia atrasar, corte primeiro nesta ordem:
1. TASK-31 (estilização extra)
2. TASK-37 (estados vazios)
3. TASK-51 (limpeza de código — fazer o mínimo)
4. TASK-45/46 (spinner/toast — implementar versão básica, sem refinamento)

**Nunca corte:** CRUD completo, validação de CEP, testes unitários (TASK-23) e README.
