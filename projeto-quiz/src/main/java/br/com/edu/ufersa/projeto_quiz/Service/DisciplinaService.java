package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Model.entity.*;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.repository.DisciplinaRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.ProfessorRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuestaoRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.UsuarioRepository;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service responsável por gerenciar regras de negócio relacionadas às entidades de {@link Disciplina}.
 * Esse service centraliza operações de CRUD, relacionamento com professores, alunos e quizzes.
 */
@Service
public class DisciplinaService {

    // TODO apenas disciplina deve acessar o proprio repository, os demais acessam os services de suas classes
    private final DisciplinaRepository disciplinaRepository;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper mapper;
    private final QuizService quizService;
    private final ModelMapper modelMapper;

    /**
     * Construtor para injeção de dependências do service.
     *
     * @param disciplinaRepository repositório de disciplinas
     * @param usuarioService service responsável pelos usuários (alunos e professores)
     * @param mapper mapper genérico para conversão de entidades e DTOs
     * @param quizService service responsável por operações relacionadas a quizzes
     * @param modelMapper outro mapper utilizado internamente
     */
    @Autowired
    public DisciplinaService(DisciplinaRepository disciplinaRepository,
                             UsuarioService usuarioService, UsuarioRepository usuarioRepository,
                             ModelMapper mapper, QuizService quizService, ModelMapper modelMapper) {

        this.disciplinaRepository = disciplinaRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.quizService = quizService;
        this.usuarioService = usuarioService;
        this.modelMapper = modelMapper;
    }

    /**
     * Busca todas as disciplinas cadastradas no sistema.
     *
     * @return lista de {@link DisciplinaDTOResponse} representando todas as disciplinas
     */

//  Esta causando inconsistencias e nao esta sendo utilizada.
//    public List<DisciplinaDTOResponse> findAll() {
//        List<Disciplina> disciplinas = disciplinaRepository.findAll();
//
//        return disciplinas
//                .stream()
//                .map((x) -> mapper.map(x, DisciplinaDTOResponse.class))
//                .collect(Collectors.toList());
//    }

    /**
     * Busca uma disciplina pelo seu ID.
     *
     * @param id identificador da disciplina
     * @return DTO representando a disciplina encontrada ou {@code null} caso não exista
     */
    public DisciplinaDTOResponse findById(long id) {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(id);
        if (disciplina.isPresent())
            return mapper.map(disciplina.get(), DisciplinaDTOResponse.class);
        return null;
    }

    /**
     * Associa um novo quiz a uma disciplina previamente cadastrada.
     *
     * @param quizDTO dados do quiz a ser criado
     * @param disciplinaId identificador da disciplina alvo
     * @return QuizDTO representando o quiz criado
     * @throws ResourceNotFound caso a disciplina não exista
     */
    public QuizDTO addQuiz(QuizDTO quizDTO, long disciplinaId) throws ResourceNotFound {
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new ResourceNotFound("Disciplina não encontrada"));

        return quizService.save(quizDTO, disciplina);
    }

    /**
     * Remove uma disciplina do sistema com base no ID informado.
     *
     * @param id identificador da disciplina a ser excluída
     * @return {@code null} sempre, apenas para compatibilidade
     */
    public DisciplinaDTO delete(long id) {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(id);
        if (disciplina.isPresent())
            disciplinaRepository.delete(disciplina.get());
        return null;
    }

    /**
     * Busca disciplinas associadas a um aluno específico.
     *
     * @param aluno entidade do aluno
     * @return lista de {@link DisciplinaDTO}
     */
    public List<DisciplinaDTO> findByAluno(Aluno aluno) {
        List<Disciplina> disciplinas = disciplinaRepository.findDisciplinaByAluno(aluno);
        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca disciplinas ministradas por um determinado professor.
     *
     * @param professor entidade do professor
     * @return lista de {@link DisciplinaDTO} associadas ao professor
     */
    public List<DisciplinaDTO> findByProfessor(Professor professor) {
        List<Disciplina> disciplinas = disciplinaRepository.findDisciplinaByProfessor(professor);
        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Obtém todos os quizzes associados a uma disciplina.
     *
     * @param id identificador da disciplina
     * @return lista de quizzes convertidos para DTO
     * @throws ResourceNotFound caso a disciplina não exista
     */
    public List<QuizDTO> getQuizesByDisciplina(long id) throws ResourceNotFound {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(id);

        return quizService.findByDisciplina(disciplina);
    }

    /**
     * Obtém a disciplina associada a um quiz específico.
     *
     * @param id identificador do quiz
     * @return DTO da disciplina encontrada
     * @throws ResourceNotFound caso nenhum relacionamento seja encontrado
     */
    public DisciplinaDTOResponse getDisciplinaByQuiz(long id) throws ResourceNotFound {
        Quiz quiz = new Quiz();
        quiz.setId(id);

        Disciplina disciplina = disciplinaRepository.findDisciplinaByQuizes(quiz);
        if (disciplina == null) {
            throw new ResourceNotFound("Disciplina não encontrada");
        }

        return mapper.map(disciplina, DisciplinaDTOResponse.class);

    }

    /**
     * Obtém todos os alunos que estão matriculados em uma disciplina.
     *
     * @param id identificador da disciplina
     * @return lista de {@link ReturnAlunoDTO}
     * @throws ResourceNotFound caso a disciplina não exista
     */
    public List<ReturnAlunoDTO> getAlunosByDisciplina(long id) throws ResourceNotFound {
        return usuarioService.getAlunosByDisciplina(id);
    }

    /**
     * Obtém o professor responsável por uma disciplina.
     *
     * @param id identificador da disciplina
     * @return DTO do professor responsável
     * @throws ResourceNotFound caso a disciplina não exista
     */
    public ReturnProfessorDTO getProfessorByDisciplina(long id) throws ResourceNotFound {
        return usuarioService.getProfessor(id);
    }

    /**
     * Edita os dados básicos de uma disciplina, como nome.
     *
     * @param disciplinaDTO dados atualizados
     * @param id identificador da disciplina
     * @return DTO atualizado da disciplina
     * @throws ResourceNotFound caso a disciplina não exista
     */
    public DisciplinaDTOResponse edit(DisciplinaDTO disciplinaDTO, long id) throws ResourceNotFound {
        Disciplina disciplina = disciplinaRepository.findDisciplinaById(id);
        if (disciplina == null) {
            throw new ResourceNotFound("Disciplina não encontrada");
        }

        disciplina.setNome(disciplinaDTO.getNome());
        disciplinaRepository.save(disciplina);
        return mapper.map(disciplina, DisciplinaDTOResponse.class);
    }

    /**
     * Retorna lista com disciplinas baseado na variavel nome e no usuario
     *
     * @param nome da disciplina
     * @return Lista de DTOS das disciplinas encontradas
     * @throws ResourceNotFound
     */
    public List<DisciplinaDTOResponse> findByNome(String nome,  long usuarioId) throws ResourceNotFound {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));

        List<Disciplina> disciplinas = new ArrayList<>();

        if (usuario instanceof Professor) {
            disciplinas = disciplinaRepository.searchDisciplinaByProfessorAndNome((Professor) usuario, nome);
        } else if (usuario instanceof Aluno) {
            disciplinas = disciplinaRepository.searchDisciplinaByAlunoAndNome((Aluno) usuario, nome);
        }

        if (disciplinas.isEmpty()) {
            throw new ResourceNotFound("Disciplina não encontrada para " + nome);
        }
        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTOResponse.class))
                .collect(Collectors.toList());
    }
    /**
     * Retorna lista com disciplinas em que o aluno ainda não está matriculado
     *
     * @param alunoId do aluno
     * @return Lista de DTOS das disciplinas encontradas
     * @throws ResourceNotFound
     */
    public List<DisciplinaDTOResponse> findDisciplinasDisponiveis(long alunoId) throws ResourceNotFound {
        Optional<Usuario> aluno = usuarioRepository.findById(alunoId);
        if(aluno.isEmpty()){
            throw new ResourceNotFound("Aluno não encontrado.");
        }

        List<Disciplina> disciplinasDisponiveis = disciplinaRepository.findDisciplinasNaoPertencentesAoAluno(alunoId);

        return disciplinasDisponiveis
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTOResponse.class))
                .collect(Collectors.toList());
    }

    }

