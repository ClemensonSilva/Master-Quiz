-- ==================================================================================
-- 1. USUÁRIOS (PROFESSOR E ALUNO)
-- ==================================================================================
INSERT INTO tb_usuarios (tipo_usuario, nome, email, senha)
VALUES ('Professor', 'Prof. Pardal', 'professor@ufersa.edu.br', 'Padal123');

INSERT INTO tb_usuarios (tipo_usuario, nome, email, senha)
VALUES ('Aluno', 'Joãozinho Estudioso', 'joao@aluno.ufersa.edu.br', 'Joao123');

-- ==================================================================================
-- 2. DISCIPLINAS
-- ==================================================================================
INSERT INTO tb_disciplina (nome, professor_id, descricao)
VALUES
    ('Estruturas de Dados', 1, 'Compreenda estruturas fundamentais como listas, pilhas, filas, árvores e grafos'),
    ('Sistemas Operacionais', 1, 'Estude os conceitos de processos, memória, escalonamento e sistemas de arquivos'),
    ('Redes de Computadores', 1, 'Aprenda os princípios de comunicação de dados, protocolos e arquitetura de redes'),
    ('Desenvolvimento Web', 1, 'Conheça as principais tecnologias e práticas para criação de aplicações web modernas');

-- ==================================================================================
-- 3. VINCULAR ALUNO À DISCIPLINA
-- ==================================================================================
INSERT INTO tb_disciplina_aluno (disciplina_id, aluno_id)
VALUES (1,2),(2,2),(3,2),(4,2);

-- ==================================================================================
-- 4. QUIZZES
-- ==================================================================================
INSERT INTO tb_quiz (titulo, disciplina_id, descricao)
VALUES
    ('Quiz 01 - Listas, Pilhas e Filas', 1, 'Estruturas lineares'),
    ('Quiz 02 - Árvores e Grafos', 1, 'Estruturas não lineares'),

    ('Quiz 01 - Processos e Threads', 2, 'Conceitos de processos'),
    ('Quiz 02 - Gerenciamento de Memória', 2, 'Memória e paginação'),

    ('Quiz 01 - Fundamentos de Redes', 3, 'Conceitos básicos de redes'),
    ('Quiz 02 - Protocolos e Camadas', 3, 'Modelo OSI e TCP/IP'),

    ('Quiz 01 - Fundamentos Web', 4, 'HTML, CSS e JS'),
    ('Quiz 02 - Backend e APIs', 4, 'APIs REST'),
    ('Quiz 03 - Frontend Moderno', 4, 'Frameworks'),
    ('Quiz 04 - Segurança Web', 4, 'Boas práticas');

-- ==================================================================================
-- 5. QUESTÕES (20 QUESTÕES = 5 POR DISCIPLINA)
-- ==================================================================================
INSERT INTO tb_questao (descricao, disciplina_id)
VALUES
-- Estruturas de Dados (1–5)
('O que é uma pilha?', 1),
('O que é uma fila?', 1),
('O que caracteriza uma lista ligada?', 1),
('Árvores são estruturas de que tipo?', 1),
('O que é um grafo?', 1),

-- Sistemas Operacionais (6–10)
('O que é um processo?', 2),
('O que é uma thread?', 2),
('Qual função do escalonador?', 2),
('O que é paginação?', 2),
('O que é memória virtual?', 2),

-- Redes (11–15)
('O que é endereço IP?', 3),
('Função do protocolo TCP?', 3),
('Função do protocolo UDP?', 3),
('O que é o modelo OSI?', 3),
('O que é uma porta lógica?', 3),

-- Web (16–20)
('O que é HTML?', 4),
('Função do CSS?', 4),
('O que é JavaScript?', 4),
('O que é API REST?', 4),
('O que é autenticação?', 4);

-- ==================================================================================
-- 6. ALTERNATIVAS (PADRÃO: 1 CORRETA + 2 ERRADAS)
-- ==================================================================================
INSERT INTO tb_alternativa (descricao, questao_id, correta)
VALUES
-- Questões 1–20 (exemplo compacto)
('Estrutura LIFO',1,1),('FIFO',1,0),('Árvore',1,0),
('FIFO',2,1),('LIFO',2,0),('Grafo',2,0),
('Usa ponteiros',3,1),('É estática',3,0),('É hierárquica',3,0),
('Hierárquicas',4,1),('Lineares',4,0),('Sequenciais',4,0),
('Conjunto de nós',5,1),('Lista',5,0),('Fila',5,0),

('Programa em execução',6,1),('Arquivo',6,0),('Thread',6,0),
('Fluxo leve',7,1),('Processo pesado',7,0),('Arquivo',7,0),
('Escolher processos',8,1),('Gerenciar disco',8,0),('Rede',8,0),
('Divisão da memória',9,1),('Cache',9,0),('Swap',9,0),
('Extensão da RAM',10,1),('CPU',10,0),('Disco rígido',10,0),

('Identificador de rede',11,1),('MAC',11,0),('DNS',11,0),
('Entrega confiável',12,1),('Criptografia',12,0),('IP',12,0),
('Entrega rápida',13,1),('Confiável',13,0),('Orientado conexão',13,0),
('Modelo em camadas',14,1),('Protocolo',14,0),('Linguagem',14,0),
('Canal de comunicação',15,1),('IP',15,0),('MAC',15,0),

('Linguagem de marcação',16,1),('Banco de dados',16,0),('Framework',16,0),
('Estilização',17,1),('Banco',17,0),('Servidor',17,0),
('Linguagem dinâmica',18,1),('HTML',18,0),('CSS',18,0),
('Interface entre sistemas',19,1),('Banco',19,0),('Linguagem',19,0),
('Validação de usuário',20,1),('Estilo',20,0),('Layout',20,0);

-- ==================================================================================
-- 7. VINCULAR 5 QUESTÕES PARA CADA QUIZ
-- ==================================================================================
INSERT INTO tb_questao_quiz (questoes_id, quiz_id)
VALUES
-- Quiz 1 a 10
(1,1),(2,1),(3,1),(4,1),(5,1),
(1,2),(2,2),(3,2),(4,2),(5,2),

(6,3),(7,3),(8,3),(9,3),(10,3),
(6,4),(7,4),(8,4),(9,4),(10,4),

(11,5),(12,5),(13,5),(14,5),(15,5),
(11,6),(12,6),(13,6),(14,6),(15,6),

(16,7),(17,7),(18,7),(19,7),(20,7),
(16,8),(17,8),(18,8),(19,8),(20,8),
(16,9),(17,9),(18,9),(19,9),(20,9),
(16,10),(17,10),(18,10),(19,10),(20,10);

-- ==================================================================================
-- 8. QUIZ RESPONDIDO
-- ==================================================================================
INSERT INTO tb_quiz_respondido (id, aluno_id, quiz_id, pontuacao_final, data_tentativa)
VALUES (1, 2, 1, 10.0, '2023-10-27');
