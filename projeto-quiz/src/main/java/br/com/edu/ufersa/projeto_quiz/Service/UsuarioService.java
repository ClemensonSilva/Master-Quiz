package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Professor;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Usuario;
import br.com.edu.ufersa.projeto_quiz.Model.repository.AlunoRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.ProfessorRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
// import org.springframework.security.crypto.password.PasswordEncoder; // Removido por enquanto

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper mapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public AlunoRepository alunoRepository;
    @Autowired
    public ProfessorRepository professorRepository;

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
    public ReturnAlunoDTO atualizarAluno(Long id, InputAlunoDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado para atualização"));

        if (!(usuarioExistente instanceof Aluno)) {
            throw new RuntimeException("O ID fornecido não pertence a um Aluno.");
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

    public ReturnProfessorDTO atualizarProfessor(Long id, InputProfessorDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado para atualização"));

        if (!(usuarioExistente instanceof Professor)) {
            throw new RuntimeException("O ID fornecido não pertence a um Professor.");
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
}