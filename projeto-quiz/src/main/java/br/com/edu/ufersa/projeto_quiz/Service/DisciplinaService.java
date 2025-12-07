package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Model.entity.*;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.repository.AlunoRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.DisciplinaRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.ProfessorRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuestaoRepository;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {
    // TODO apenas disciplina deve acessar o proprio repository, os demais acessam os services de suas classes
    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;
    private final UsuarioService usuarioService;
    private final QuestaoRepository questaoRepository;
    private final ModelMapper mapper;
    private final QuizService quizService;
    private final ModelMapper modelMapper;

    @Autowired
    public DisciplinaService(DisciplinaRepository disciplinaRepository, ProfessorRepository professorRepository, UsuarioService usuarioService, ModelMapper mapper, QuizService quizService, QuestaoRepository questaoRepository, ModelMapper modelMapper) {
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
        this.mapper = mapper;
        this.quizService = quizService;
        this.usuarioService = usuarioService;
        this.questaoRepository = questaoRepository;
        this.modelMapper = modelMapper;
    }

    public List<DisciplinaDTOResponse> findAll(){
        List<Disciplina> disciplinas = disciplinaRepository.findAll();

        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTOResponse.class))
                .collect(Collectors.toList());
    }

    public DisciplinaDTOResponse findById(long id){
        Optional<Disciplina> disciplina = disciplinaRepository.findById(id);
        if(disciplina.isPresent())
            return mapper.map(disciplina.get(), DisciplinaDTOResponse.class);
        return null;
    }

    /**
     * Método para associar um quiz a uma disciplina. Usada o método save do service de Quiz intermante.
     * Regra:
     * @param quizDTO
     * @param disciplinaId
     * @return QuizDTO
     * @throws ResourceNotFound
     */
    public QuizDTO addQuiz(QuizDTO quizDTO, long disciplinaId) throws ResourceNotFound {
        Disciplina  disciplina = disciplinaRepository.findById(disciplinaId).orElseThrow(()-> new ResourceNotFound("Disciplina não encontrada"));
        return quizService.save(quizDTO, disciplina);
    }

    public DisciplinaDTOResponse save(DisciplinaDTO disciplinaDTO){
        Professor professor = professorRepository.findById(disciplinaDTO.getProfessorId())
                .orElseThrow(() -> new DataIntegrityViolationException("Professor nao cadastrado"));

        Disciplina disciplina = Disciplina.convert(disciplinaDTO);
        disciplina.setProfessor(professor);
        disciplinaRepository.save(disciplina);
        return modelMapper.map(disciplina, DisciplinaDTOResponse.class);
    }

    public DisciplinaDTO delete(long id){
        Optional<Disciplina> disciplina = disciplinaRepository.findById(id);
        if(disciplina.isPresent())
            disciplinaRepository.delete(disciplina.get());
        return null;
    }

    public List<DisciplinaDTO> findByAluno(Aluno aluno){
        List<Disciplina> disciplinas = disciplinaRepository.findDisciplinaByAluno(aluno);
        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTO.class))
                .collect(Collectors.toList());
    }

    public List<DisciplinaDTO> findByProfessor(Professor professor){
        List<Disciplina> disciplinas = disciplinaRepository.findDisciplinaByProfessor(professor);
        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTO.class))
                .collect(Collectors.toList());
    }


    public List<QuizDTO> getQuizesByDisciplina(long id) throws ResourceNotFound {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(id);

        List<QuizDTO> quizzes = quizService.findByDisciplina(disciplina);
        return quizzes;
    }

    public DisciplinaDTOResponse getDisciplinaByQuiz(long id) throws  ResourceNotFound{
        Quiz quiz = new Quiz();
        quiz.setId(id);

        Disciplina disciplina = disciplinaRepository.findDisciplinaByQuizes(quiz);
        if(disciplina == null){
            throw new ResourceNotFound("Disciplina não encontrada");
        }

        return mapper.map(disciplina, DisciplinaDTOResponse.class);

    }

    public List<ReturnAlunoDTO> getAlunosByDisciplina(long id) throws ResourceNotFound {
        return usuarioService.getAlunosByDisciplina(id);
    }

    public ReturnProfessorDTO getProfessorByDisciplina(long id) throws ResourceNotFound {
        return usuarioService.getProfessor(id);
    }
}
