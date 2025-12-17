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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service responsável pela gestão de usuários, incluindo operações relacionadas
 * a alunos, professores e seus relacionamentos com disciplinas.
 *
 * Este service centraliza:
 * <ul>
 *   <li>Criação e atualização de usuários</li>
 *   <li>Listagem de usuários</li>
 *   <li>Gerenciamento de matrículas (aluno ↔ disciplina)</li>
 *   <li>Associação professor ↔ disciplina</li>
 *   <li>Consultas personalizadas</li>
 * </ul>
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;

    /**
     * Construtor para injeção de dependências do service.
     *
     * @param usuarioRepository repositório de usuários
     * @param mapper mapeador de entidades e DTOs
     * @param alunoRepository repositório de alunos
     * @param passwordEncoder codificador de senhas
     * @param disciplinaRepository repositório de disciplinas
     * @param professorRepository repositório de professores
     */
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper mapper,
                          AlunoRepository alunoRepository, PasswordEncoder passwordEncoder,
                          DisciplinaRepository disciplinaRepository, ProfessorRepository professorRepository) {

        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.professorRepository = professorRepository;
    }

    /**
     * Cria um novo aluno no sistema.
     *
     * @param dto dados de entrada do aluno
     * @return DTO contendo os dados do aluno criado
     * @throws DataIntegrityViolationException caso já exista um usuário com o e-mail informado
     */
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

    /**
     * Cria um novo professor no sistema.
     *
     * @param dto dados do professor
     * @return DTO com os dados do professor salvo
     * @throws DataIntegrityViolationException quando o email já está cadastrado
     */
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

    /**
     * Busca um aluno pelo ID.
     *
     * @param id identificador do aluno
     * @return DTO com dados do aluno encontrado
     * @throws ResourceNotFound caso o aluno não exista
     */
    public ReturnAlunoDTO getAluno(long id) throws ResourceNotFound {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Aluno não encontrado"));

        return mapper.map(aluno, ReturnAlunoDTO.class);
    }

    /**
     * Busca um professor pelo ID.
     *
     * @param id identificador do professor
     * @return DTO com dados do professor
     * @throws ResourceNotFound caso o professor não exista
     */
    public ReturnProfessorDTO getProfessor(Long id) throws ResourceNotFound {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Professor não encontrado"));

        return mapper.map(professor, ReturnProfessorDTO.class);
    }

    /**
     * Lista todos os alunos cadastrados.
     *
     * @return lista de alunos em formato DTO
     */
    public List<ReturnAlunoDTO> listarTodosAlunos() {
        List<Aluno> alunos = alunoRepository.findAll();
        return alunos.stream()
                .map(aluno -> mapper.map(aluno, ReturnAlunoDTO.class))
                .toList();
    }

    /**
     * Lista todos os professores cadastrados.
     *
     * @return lista de professores em formato DTO
     */
    public List<ReturnProfessorDTO> listarTodosProfessores() {
        List<Professor> professores = professorRepository.findAll();
        return professores.stream()
                .map(professor -> mapper.map(professor, ReturnProfessorDTO.class))
                .toList();
    }

    /**
     * Lista todos os usuários do sistema (alunos e professores).
     *
     * @return lista de usuários convertidos para DTOs específicos de seu tipo
     */
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

    /**
     * Atualiza os dados de um aluno existente.
     *
     * @param id identificador do aluno
     * @param dto dados atualizados
     * @return DTO do aluno atualizado
     * @throws ResourceNotFound quando o ID não pertence a um aluno
     */
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

    /**
     * Atualiza os dados de um professor.
     *
     * @param id identificador do professor
     * @param dto dados atualizados
     * @return DTO do professor salvo
     * @throws ResourceNotFound quando o ID não pertence a um professor
     */
    public ReturnProfessorDTO atualizarProfessor(Long id, InputProfessorDTO dto) throws ResourceNotFound {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Professor não encontrado para atualização"));

        if (!(usuarioExistente instanceof Professor)) {
            throw new ResourceNotFound("O ID fornecido não pertence a um Professor.");
        }

        Usuario usuarioComEmail = usuarioRepository.findByEmail(dto.getEmail());
        if (usuarioComEmail != null && !usuarioComEmail.getId().equals(id)) {
            throw new DataIntegrityViolationException("O e-mail informado já está em uso por outro usuário.");
        }

        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setEmail(dto.getEmail());

        Professor professorSalvo = (Professor) usuarioRepository.save(usuarioExistente);
        return mapper.map(professorSalvo, ReturnProfessorDTO.class);
    }

    /**
     * Remove um usuário do sistema.
     *
     * @param id identificador do usuário
     */
    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    /**
     * Obtém todas as disciplinas associadas a um usuário (aluno ou professor).
     *
     * @param userId ID do usuário
     * @return lista de disciplinas em formato DTO
     * @throws ResourceNotFound caso o usuário não exista ou não esteja associado a disciplinas
     */
    public List<DisciplinaDTOResponse> disciplinasByUser(long userId, String nome) throws ResourceNotFound {
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));

        List<Disciplina> disciplinas = new ArrayList<>();
        // TODO melhorar lógica e passar decisao para o Repository e unificar o metodo para coletar discciplina para o usuario, mandando o nome ou nao
        if (user instanceof Professor) {
            if(nome == null  ){
                disciplinas = disciplinaRepository.findDisciplinaByProfessor((Professor) user);
            }
            else{
            disciplinas = disciplinaRepository.searchDisciplinaByProfessorAndNome((Professor) user, nome);
            }
        } else if (user instanceof Aluno) {
            if(nome == null){
                disciplinas = disciplinaRepository.findDisciplinaByAluno((Aluno) user);
            }
            else{
                disciplinas = disciplinaRepository.searchDisciplinaByAlunoAndNome((Aluno) user, nome);
            }
        }

        return disciplinas.stream()
                .map(x -> mapper.map(x, DisciplinaDTOResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Matricula um aluno em uma disciplina.
     *
     * @param matriculaInputDTO dados contendo o ID da disciplina
     * @param alunoId ID do aluno a ser matriculado
     * @return DTO contendo aluno e disciplina resultante da matrícula
     * @throws ResourceNotFound quando o aluno ou a disciplina não existem
     * @throws BusinessLogicException quando um professor tenta se matricular
     */
    @Transactional
    public MatriculaResponseDTO enrollAluno(@Valid MatriculaInputDTO matriculaInputDTO, long alunoId)
            throws ResourceNotFound, BusinessLogicException {

        Usuario user = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));

        if (user instanceof Professor) {
            throw new BusinessLogicException("Professor não pode matricular-se em disciplinas");
        }

        Optional<Disciplina> disciplina = disciplinaRepository.findById(matriculaInputDTO.getDisciplinaId());
        if (!disciplina.isPresent()) {
            throw new ResourceNotFound("Usuário não encontrado");
        }

        disciplina.get().getAluno().add((Aluno) user);
        disciplinaRepository.save(disciplina.get());

        return new MatriculaResponseDTO(
                mapper.map(disciplina.get(), DisciplinaDTO.class),
                mapper.map(user, ReturnAlunoDTO.class)
        );
    }

    /**
     * Obtém todos os alunos matriculados em uma disciplina.
     *
     * @param id ID da disciplina
     * @return lista de alunos matriculados
     * @throws ResourceNotFound quando não há alunos matriculados
     */
    public List<ReturnAlunoDTO> getAlunosByDisciplina(long id) throws ResourceNotFound {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(id);

        List<Aluno> alunos = alunoRepository.findByDisciplinas(disciplina);

        if (alunos.isEmpty()) {
            throw new ResourceNotFound("Nenhum aluno matriculado na disciplina " + disciplina.getNome());
        }

        return alunos.stream()
                .map(x -> mapper.map(x, ReturnAlunoDTO.class))
                .toList();
    }

    /**
     * Cria uma disciplina associada a um professor específico.
     *
     * @param disciplinaDTO dados da disciplina
     * @param id ID do professor criador
     * @return DTO contendo os dados da disciplina criada
     * @throws BusinessLogicException caso o ID não pertença a um professor
     */
    public DisciplinaDTOResponse createDisciplina(DisciplinaDTO disciplinaDTO, long id) throws BusinessLogicException {
        Professor professor = professorRepository.findProfessorById(id);

        if (professor == null) {
            throw new BusinessLogicException("Apenas professores podem criar disciplinas");
        }

        Disciplina disciplina = new Disciplina();
        disciplina.setNome(disciplinaDTO.getNome());
        disciplina.setDescricao(disciplinaDTO.getDescricao());
        disciplina.setProfessor(professor);
        disciplinaRepository.save(disciplina);
        return mapper.map(disciplina, DisciplinaDTOResponse.class);
    }

    public ReturnAlunoDTO getAlunoByEmail(String email) throws ResourceNotFound {
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("Aluno não encontrado"));
        return mapper.map(aluno, ReturnAlunoDTO.class);
    }

    public ReturnProfessorDTO getProfessorByEmail(String email) throws ResourceNotFound {
        Professor professor = professorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("Professor não encontrado"));

        return mapper.map(professor, ReturnProfessorDTO.class);
    }
}
