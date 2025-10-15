package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTO;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Professor;
import br.com.edu.ufersa.projeto_quiz.Model.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {
    @Autowired
    private DisciplinaRepository repository;

    public List<DisciplinaDTO> findAll(){
        List<Disciplina> disciplinas = repository.findAll();
        return disciplinas
                .stream()
                .map(DisciplinaDTO::convert)
                .collect(Collectors.toList());
    }

    public DisciplinaDTO findById(long id){
        Optional<Disciplina> disciplina = repository.findById(id);
        if(disciplina.isPresent())
            return DisciplinaDTO.convert(disciplina.get());
        return null;
    }

    public DisciplinaDTO save(DisciplinaDTO disciplinaDTO){
        Disciplina disciplina = repository.save(Disciplina.convert(disciplinaDTO));
        return DisciplinaDTO.convert(disciplina);
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
                .map(DisciplinaDTO::convert)
                .collect(Collectors.toList());
    }

    public List<DisciplinaDTO> findByProfessor(Professor professor){
        List<Disciplina> disciplinas = repository.findDisciplinaByProfessor(professor);
        return disciplinas
                .stream()
                .map(DisciplinaDTO::convert)
                .collect(Collectors.toList());
    }
}
