package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlternativaDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.AlternativaDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.repository.AlternativaRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.DisciplinaRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuestaoRepository;
import br.com.edu.ufersa.projeto_quiz.exception.BusinessLogicException;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AlternativaService {

    private final AlternativaRepository repository;
    private final QuestaoRepository questaoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AlternativaService(AlternativaRepository repository,
                              QuestaoRepository questaoRepository,
                              ModelMapper modelMapper,
                              DisciplinaRepository disciplinaRepository) {

        this.repository = repository;
        this.questaoRepository = questaoRepository;
        this.modelMapper = modelMapper;
        this.disciplinaRepository = disciplinaRepository;
    }



    private Disciplina validateDisciplina(long disciplinaId) throws ResourceNotFound {
        Disciplina disciplina =  disciplinaRepository.findDisciplinaById(disciplinaId);
        if(disciplina == null) {
            throw new ResourceNotFound("Disciplina não encontrada.");
        }
        return disciplina;
    }

    private Questao validateQuestao(long disciplinaId, long questaoId) throws ResourceNotFound {
        Disciplina disciplina = validateDisciplina(disciplinaId);

        Questao questao =  questaoRepository.findQuestaoByIdAndDisciplina(questaoId, disciplina);
        if(questao == null) {
            throw new ResourceNotFound("Questão não encontrada.");
        }
        return  questao;
    }

    private Alternativa validateAlternativa(long disciplinaId, long questaoId, long alternativaId)
            throws ResourceNotFound {

        Questao questao = validateQuestao(disciplinaId, questaoId);

        Alternativa alternativa =  repository.findByIdAndQuestao(alternativaId, questao);
        if(alternativa == null) {
            throw new ResourceNotFound("Alternativa não encontrada.");
        }
        return  alternativa;
    }



    public List<AlternativaDTOResponse> findAll(long disciplinaId, long questaoId) throws ResourceNotFound {
        Questao questao = validateQuestao(disciplinaId, questaoId);

        return repository.findByQuestao(questao)
                .stream()
                .map(x -> modelMapper.map(x, AlternativaDTOResponse.class))
                .toList();
    }


    public AlternativaDTOResponse findById(long disciplinaId, long questaoId, long id) throws ResourceNotFound {
        Alternativa entity = validateAlternativa(disciplinaId, questaoId, id);
        return modelMapper.map(entity, AlternativaDTOResponse.class);
    }


    public void delete(long disciplinaId, long questaoId, long id) throws ResourceNotFound {
        Alternativa entity = validateAlternativa(disciplinaId, questaoId, id);
        repository.delete(entity);
    }


    public AlternativaDTOResponse update(long disciplinaId, long questaoId, long id, AlternativaDTO dto)
            throws ResourceNotFound {

        Alternativa entity = validateAlternativa(disciplinaId, questaoId, id);

        entity.setDescricao(dto.getDescricao());
        entity.setCorreta(dto.getCorreta());

        repository.save(entity);
        return modelMapper.map(entity, AlternativaDTOResponse.class);
    }


    public AlternativaDTOResponse save(long disciplinaId, long questaoId, AlternativaDTO dto)
            throws BusinessLogicException, ResourceNotFound {

        Questao questao = validateQuestao(disciplinaId, questaoId);

        Alternativa alternativa = new Alternativa();
        alternativa.setDescricao(dto.getDescricao());
        alternativa.setCorreta(dto.getCorreta());
        alternativa.setQuestao(questao);

        Alternativa saved = repository.save(alternativa);

        return modelMapper.map(saved, AlternativaDTOResponse.class);
    }
}
