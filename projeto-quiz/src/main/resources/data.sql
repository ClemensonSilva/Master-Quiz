-- ==================================================================================
-- 1. USUÁRIOS (PROFESSORES E ALUNOS)
-- ==================================================================================
INSERT INTO tb_usuarios (tipo_usuario, nome, email, senha)
VALUES ('Professor', 'Prof. Pardal', 'professor@ufersa.edu.br', 'Padal123');

INSERT INTO tb_usuarios (tipo_usuario, nome, email, senha)
VALUES ('Aluno', 'Joãozinho Estudioso', 'joao@aluno.ufersa.edu.br', 'Joao123');

-- ==================================================================================
-- 2. DISCIPLINAS
-- ==================================================================================
INSERT INTO tb_disciplina (nome, professor_id)
VALUES ('Engenharia de Software', 1);

INSERT INTO tb_disciplina (nome, professor_id)
VALUES ('Banco de Dados', 1);

-- ==================================================================================
-- 3. VINCULAR ALUNO À DISCIPLINA
-- ==================================================================================
INSERT INTO tb_disciplina_aluno (disciplina_id, aluno_id) VALUES (1, 2);

-- ==================================================================================
-- 4. QUIZ
-- ==================================================================================
INSERT INTO tb_quiz (titulo, disciplina_id)
VALUES ('Quiz 01 - Modelagem de Dados', 1);

-- ==================================================================================
-- 5. QUESTÕES
-- MUDANÇA: Removemos 'alternativa_correta_id'. A Questão agora só tem descrição e disciplina.
-- ==================================================================================

-- Questão 1
INSERT INTO tb_questao (descricao, disciplina_id)
VALUES ('O que significa DER?', 1);

-- Questão 2
INSERT INTO tb_questao (descricao, disciplina_id)
VALUES ('Qual comando SQL remove uma tabela?', 1);

-- ==================================================================================
-- 6. ALTERNATIVAS
-- MUDANÇA: Adicionamos a coluna 'correta' (BOOLEAN) para indicar a resposta certa.
-- ==================================================================================

-- Alternativas para a Questão 1 (DER)
-- A primeira é a VERDADEIRA (true/1), as outras são FALSAS (false/0)
INSERT INTO tb_alternativa (descricao, questao_id, correta) VALUES ('Diagrama de Entidade Relacionamento', 1, 1);
INSERT INTO tb_alternativa (descricao, questao_id, correta) VALUES ('Dados em Rede', 1, 0);
INSERT INTO tb_alternativa (descricao, questao_id, correta) VALUES ('Domínio de Estrutura Rápida', 1, 0);

-- Alternativas para a Questão 2 (SQL Remove)
INSERT INTO tb_alternativa (descricao, questao_id, correta) VALUES ('DELETE TABLE', 2, 0);
INSERT INTO tb_alternativa (descricao, questao_id, correta) VALUES ('DROP TABLE', 2, 1); -- Esta é a correta
INSERT INTO tb_alternativa (descricao, questao_id, correta) VALUES ('REMOVE TABLE', 2, 0);

-- ==================================================================================
-- 7. VINCULAR QUESTÕES AO QUIZ
-- ==================================================================================
INSERT INTO tb_questao_quiz (questoes_id, quiz_id) VALUES (1, 1);
INSERT INTO tb_questao_quiz (questoes_id, quiz_id) VALUES (2, 1);

-- ==================================================================================
-- 8. SIMULAÇÃO DE RESPOSTA
-- ==================================================================================
INSERT INTO tb_quiz_respondido (id, aluno_id, quiz_id, pontuacao_final, data_tentativa)
VALUES (1, 2, 1, 10.0, '2023-10-27');
