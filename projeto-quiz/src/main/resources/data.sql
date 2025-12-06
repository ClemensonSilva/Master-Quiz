-- ==================================================================================
-- 1. USUÁRIOS (PROFESSORES E ALUNOS)
-- Tabela única devido à estratégia SINGLE_TABLE
-- Senha para todos abaixo é: 123456 (Hash BCrypt)
-- ==================================================================================

-- Criando um Professor
INSERT INTO tb_usuarios (tipo_usuario, nome, email, senha)
VALUES ('Professor', 'Prof. Pardal', 'professor@ufersa.edu.br', 'Padal123');

-- Criando um Aluno
INSERT INTO tb_usuarios (tipo_usuario, nome, email, senha)
VALUES ('Aluno', 'Joãozinho Estudioso', 'joao@aluno.ufersa.edu.br', 'Joao123');

-- ==================================================================================
-- 2. DISCIPLINAS
-- O professor de ID 1 será o dono desta disciplina
-- ==================================================================================

INSERT INTO tb_disciplina (nome, professor_id)
VALUES ('Engenharia de Software', 1);

INSERT INTO tb_disciplina (nome, professor_id)
VALUES ('Banco de Dados', 1);

-- ==================================================================================
-- 3. VINCULAR ALUNO À DISCIPLINA (ManyToMany)
-- O aluno (ID 2) está matriculado na disciplina (ID 1)
-- ==================================================================================

INSERT INTO tb_disciplina_aluno (disciplina_id, aluno_id) VALUES (1, 2);

-- ==================================================================================
-- 4. QUIZ
-- Criando um Quiz para a disciplina 1
-- ==================================================================================

INSERT INTO tb_quiz (titulo, disciplina_id)
VALUES ('Quiz 01 - Modelagem de Dados', 1);

-- ==================================================================================
-- 5. QUESTÕES (Inserção Inicial)
-- IMPORTANTE: "alternativa_correta_id" começa como NULL para evitar erro de FK,
-- pois as alternativas ainda não existem.
-- ==================================================================================

-- Questão 1
INSERT INTO tb_questao (descricao, disciplina_id, alternativa_correta_id)
VALUES ('O que significa DER?', 1, NULL);

-- Questão 2
INSERT INTO tb_questao (descricao, disciplina_id, alternativa_correta_id)
VALUES ('Qual comando SQL remove uma tabela?', 1, NULL);

-- ==================================================================================
-- 6. ALTERNATIVAS
-- Agora criamos as alternativas vinculadas às questões (IDs 1 e 2 gerados acima)
-- ==================================================================================

-- Alternativas para a Questão 1 (DER)
INSERT INTO tb_alternativa (descricao, questao_id) VALUES ('Diagrama de Entidade Relacionamento', 1); -- ID 1 (Correta)
INSERT INTO tb_alternativa (descricao, questao_id) VALUES ('Dados em Rede', 1);                        -- ID 2
INSERT INTO tb_alternativa (descricao, questao_id) VALUES ('Domínio de Estrutura Rápida', 1);          -- ID 3

-- Alternativas para a Questão 2 (SQL Remove)
INSERT INTO tb_alternativa (descricao, questao_id) VALUES ('DELETE TABLE', 2);                         -- ID 4
INSERT INTO tb_alternativa (descricao, questao_id) VALUES ('DROP TABLE', 2);                           -- ID 5 (Correta)
INSERT INTO tb_alternativa (descricao, questao_id) VALUES ('REMOVE TABLE', 2);                         -- ID 6

-- ==================================================================================
-- 7. ATUALIZAR QUESTÕES COM A RESPOSTA CORRETA
-- Agora que as alternativas existem, vinculamos a correta na questão
-- ==================================================================================

-- Questão 1 correta é ID 1
UPDATE tb_questao SET alternativa_correta_id = 1 WHERE id = 1;

-- Questão 2 correta é ID 5
UPDATE tb_questao SET alternativa_correta_id = 5 WHERE id = 2;

-- ==================================================================================
-- 8. VINCULAR QUESTÕES AO QUIZ (ManyToMany)
-- Tabela de junção padrão gerada pelo Hibernate (tb_questao_quiz)
-- Colunas prováveis: questoes_id (Questao) e quiz_id (Quiz)
-- ==================================================================================

INSERT INTO tb_questao_quiz (questoes_id, quiz_id) VALUES (1, 1);
INSERT INTO tb_questao_quiz (questoes_id, quiz_id) VALUES (2, 1);

-- ==================================================================================
-- 9. SIMULAÇÃO DE UM QUIZ RESPONDIDO
-- O aluno (ID 2) respondeu o Quiz (ID 1)
-- ==================================================================================

INSERT INTO tb_quiz_respondido (id, aluno_id, quiz_id, pontuacao_final, data_tentativa)
VALUES (1, 2, 1, 10.0, '2023-10-27');

-- ==================================================================================
-- 10. RESPOSTAS DADAS PELO ALUNO
-- Registrando o que o aluno marcou
-- ==================================================================================
