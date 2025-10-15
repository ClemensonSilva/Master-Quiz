package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Professor;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Usuario;
import br.com.edu.ufersa.projeto_quiz.Model.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
// import org.springframework.security.crypto.password.PasswordEncoder; // Removido por enquanto

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper mapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    public ReturnAlunoDTO criarAluno(@Valid InputAlunoDTO dto) throws DataIntegrityViolationException {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(dto.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new DataIntegrityViolationException("Já existe um usuário cadastrado com o mesmo email");
        }


        Aluno novoAluno = mapper.map(dto, Aluno.class);
        Aluno alunoSalvo = usuarioRepository.save(novoAluno);
        return mapper.map(alunoSalvo, ReturnAlunoDTO.class);
    }

    public ReturnProfessorDTO criarProfessor(InputProfessorDTO dto) throws DataIntegrityViolationException {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(dto.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new DataIntegrityViolationException("Já existe um usuário cadastrado com o mesmo email");
        }

        Professor novoProfessor = mapper.map(dto, Professor.class);
        Professor professorSalvo = usuarioRepository.save(novoProfessor);
        return mapper.map(professorSalvo, ReturnProfessorDTO.class);
    }

    public List<ReturnUsuarioDTO> listarTodos() {
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

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}