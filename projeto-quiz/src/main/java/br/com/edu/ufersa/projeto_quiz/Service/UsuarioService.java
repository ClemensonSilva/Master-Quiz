package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Professor;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Usuario;
import br.com.edu.ufersa.projeto_quiz.Model.repository.AlunoRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.DisciplinaRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.ProfessorRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.UsuarioRepository;
import br.com.edu.ufersa.projeto_quiz.exception.BusinessLogicException;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
// import org.springframework.security.crypto.password.PasswordEncoder; // Removido por enquanto

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper mapper, AlunoRepository alunoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.alunoRepository = alunoRepository;
    }

    @Autowired
    public ProfessorRepository professorRepository;
    @Autowired
    public DisciplinaRepository disciplinaRepository;

    public ReturnAlunoDTO criarAluno(@Valid InputAlunoDTO dto) throws DataIntegrityViolationException {
        Usuario usuarioExistente = usuarioRepository.findByEmail(dto.getEmail());
        if (usuarioExistente != null) {
            throw new DataIntegrityViolationException("Já existe um usuário cadastrado com o mesmo email");
        }
        Aluno novoAluno = mapper.map(dto, Aluno.class);
        novoAluno.setSenha(passwordEncoder.encode(dto.getSenha()));
        Aluno alunoSalvo = usuarioRepository.save(novoAluno);
        return mapper.map(alunoSalvo, ReturnAlunoDTO.class);
    }

    public ReturnProfessorDTO criarProfessor(InputProfessorDTO dto) throws DataIntegrityViolationException {
        Usuario usuarioExistente = usuarioRepository.findByEmail(dto.getEmail());
        if (usuarioExistente != null) {
            throw new DataIntegrityViolationException("Já existe um usuário cadastrado com o mesmo email");
        }

        Professor novoProfessor = mapper.map(dto, Professor.class);
        novoProfessor.setSenha(passwordEncoder.encode(dto.getSenha()));
        Professor professorSalvo = usuarioRepository.save(novoProfessor);
        return mapper.map(professorSalvo, ReturnProfessorDTO.class);
    }

    public ReturnAlunoDTO getAluno(long id) throws ResourceNotFound {
        Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Aluno não encontrado"));
        return mapper.map(aluno, ReturnAlunoDTO.class);
    }
    public ReturnProfessorDTO getProfessor(Long id) throws ResourceNotFound {
        Professor professor = professorRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Professor não encontrado"));
        return mapper.map(professor, ReturnProfessorDTO.class);
    }


    public List<ReturnAlunoDTO> listarTodosAlunos() {
        List<Aluno> alunos = alunoRepository.findAll();
       return  alunos.stream().map(aluno -> mapper.map(aluno, ReturnAlunoDTO.class)).toList();
    }
    public List<ReturnProfessorDTO> listarTodosProfessores() {
        List<Professor> professores = professorRepository.findAll();
        return  professores.stream().map(professor -> mapper.map(professor, ReturnProfessorDTO.class)).toList();
    }

    public List<ReturnUsuarioDTO> listarTodosUsuarios() {
        List<Usuario> todosUsuarios = usuarioRepository.findAll();

        return todosUsuarios.stream().map(usuario -> {
            if (usuario instanceof Professor) {
                return mapper.map(usuario, ReturnProfessorDTO.class);
            } else if (usuario instanceof Aluno) {
                return mapper.map(usuario, ReturnAlunoDTO.class);
            }
            return null;
        }).collect(Collectors.toList());
    }
    public ReturnAlunoDTO atualizarAluno(Long id, InputAlunoDTO dto) throws ResourceNotFound {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Aluno não encontrado para atualização"));

        if (!(usuarioExistente instanceof Aluno)) {
            throw new ResourceNotFound("O ID fornecido não pertence a um Aluno.");
        }

        Usuario usuarioComEmail = usuarioRepository.findByEmail(dto.getEmail());
        if (usuarioComEmail != null && !usuarioComEmail.getId().equals(id)) {
            throw new DataIntegrityViolationException("O e-mail informado já está em uso por outro usuário.");
        }

        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setEmail(dto.getEmail());

        Aluno alunoSalvo = (Aluno) usuarioRepository.save(usuarioExistente);
        return mapper.map(alunoSalvo, ReturnAlunoDTO.class);
    }

    public ReturnProfessorDTO atualizarProfessor(Long id, InputProfessorDTO dto) throws ResourceNotFound {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Professor não encontrado para atualização"));

        if (!(usuarioExistente instanceof Professor)) {
            throw new ResourceNotFound("O ID fornecido não pertence a um Professor.");
        }

        Usuario usuarioComEmail = usuarioRepository.findByEmail(dto.getEmail());
        if (usuarioComEmail!= null && !usuarioComEmail.getId().equals(id)) {
            throw new DataIntegrityViolationException("O e-mail informado já está em uso por outro usuário.");
        }

        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setEmail(dto.getEmail());

        Professor professorSalvo = (Professor) usuarioRepository.save(usuarioExistente);
        return mapper.map(professorSalvo, ReturnProfessorDTO.class);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    /**
     * Metodo que será usada para buscar as diciplinas associadas ao aluno ou ao professor
     * passado no parâmetro como o id do usuário. Após isso, verifica-se se o user é Aluno ou
     * Professor e, baseando-se nisso, busca as disciplinas correspondentes ao tipo de usuário.
     * @param userId
     * @return List<DisciplinaDTO>
     */
    public List<DisciplinaDTOResponse> disciplinasByUser(long userId) throws ResourceNotFound {
        Usuario user = usuarioRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));

        List<Disciplina> disciplinas = new ArrayList<>();

        if (user instanceof Professor) {
             disciplinas = disciplinaRepository.findDisciplinaByProfessor((Professor) user);
        } else if (user instanceof Aluno) {
             disciplinas = disciplinaRepository.findDisciplinaByAluno((Aluno) user);
        }

        if (disciplinas == null || disciplinas.isEmpty()) {
            throw new ResourceNotFound(
                    "Nenhuma disciplina encontrada para o usuário: " + user.getId()
            );
        }
        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTOResponse.class))
                .collect(Collectors.toList());

}

    /**
     * Método usado para matricular um aluno em uma disciplina
     * @param matriculaInputDTO
     * @param alunoId
     * @return MatriculaResponseDTO
     * @throws ResourceNotFound
     */
    @Transactional
    public MatriculaResponseDTO enrollAluno(@Valid MatriculaInputDTO matriculaInputDTO, long alunoId) throws ResourceNotFound {
        Usuario user = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));

        if (user instanceof Professor) {
            throw  new BusinessLogicException("Professor não pode matricular-se em disciplinas");
        }

        Optional<Disciplina> disciplina = disciplinaRepository.findById(matriculaInputDTO.getDisciplinaId());

        if (!disciplina.isPresent()) throw  new ResourceNotFound("Usuário não encontrado");

        disciplina.get().getAluno().add((Aluno) user);

        disciplinaRepository.save(disciplina.get());

        return new MatriculaResponseDTO(mapper.map(disciplina.get(), DisciplinaDTO.class), mapper.map(user, ReturnAlunoDTO.class));

    }

    public List<ReturnAlunoDTO> getAlunosByDisciplina(long id) throws ResourceNotFound {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(id);
        List<Aluno> alunos = alunoRepository.findByDisciplinas(disciplina);
        if(alunos.isEmpty()) throw new ResourceNotFound("Nenhuma aluno matriculado na disciplina "+ disciplina.getNome());

        return alunos.stream().map(x -> mapper.map(x, ReturnAlunoDTO.class)).toList();
    }
}