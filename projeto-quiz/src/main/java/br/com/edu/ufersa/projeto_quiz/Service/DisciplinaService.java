package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Professor;
import br.com.edu.ufersa.projeto_quiz.Model.repository.DisciplinaRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.ProfessorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {
    @Autowired
    private DisciplinaRepository repository;
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ModelMapper mapper;

    public List<DisciplinaDTOResponse> findAll(){
        List<Disciplina> disciplinas = repository.findAll();

        return disciplinas
                .stream()
                .map(DisciplinaDTOResponse::convert)
                .collect(Collectors.toList());
    }

    public DisciplinaDTOResponse findById(long id){
        Optional<Disciplina> disciplina = repository.findById(id);
        if(disciplina.isPresent())
            return DisciplinaDTOResponse.convert(disciplina.get());
        return null;
    }

    public DisciplinaDTO save(DisciplinaDTO disciplinaDTO){
        Professor professor = professorRepository.findById(disciplinaDTO.getProfessorId())
                .orElseThrow(() -> new DataIntegrityViolationException("Professor nao cadastrado"));

        Disciplina disciplina = Disciplina.convert(disciplinaDTO);
        disciplina.setProfessor(professor);
        disciplina = repository.save(disciplina);
        return disciplinaDTO;
    }

    public DisciplinaDTO delete(long id){
        Optional<Disciplina> disciplina = repository.findById(id);
        if(disciplina.isPresent())
            repository.delete(disciplina.get());
        return null;
    }

    public List<DisciplinaDTO> findByAluno(Aluno aluno){
        List<Disciplina> disciplinas = repository.findDisciplinaByAluno(aluno);
        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTO.class))
                .collect(Collectors.toList());
    }

    public List<DisciplinaDTO> findByProfessor(Professor professor){
        List<Disciplina> disciplinas = repository.findDisciplinaByProfessor(professor);
        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTO.class))
                .collect(Collectors.toList());
    }
}
