package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlternativaDTO;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.repository.AlternativaRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuestaoRepository;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlternativaService {

    private final AlternativaRepository repository;
    private final QuestaoRepository questaoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AlternativaService(AlternativaRepository repository, QuestaoRepository questaoRepository, ModelMapper modelMapper) {
        this.repository = repository;
        this.questaoRepository = questaoRepository;
        this.modelMapper = modelMapper;
    }

    public List<AlternativaDTO> findAll() {
        return repository.findAll().stream()
                .map(entity -> modelMapper.map(entity, AlternativaDTO.class))
                .collect(Collectors.toList());
    }

    public AlternativaDTO findById(Long id) throws ResourceNotFound {
        Alternativa entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Alternativa não encontrada." ));

        return modelMapper.map(entity, AlternativaDTO.class);
    }

    
    public void delete(Long id) throws ResourceNotFound {
        Alternativa entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Alternativa não encontrada."));

        repository.delete(entity);
    }

}
