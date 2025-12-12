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

    /**
     * Valida se a disciplina existe no banco.
     *
     * @param disciplinaId ID da disciplina.
     * @return a entidade Disciplina válida.
     * @throws ResourceNotFound caso a disciplina não exista.
     */
    private Disciplina validateDisciplina(long disciplinaId) throws ResourceNotFound {
        Disciplina disciplina = disciplinaRepository.findDisciplinaById(disciplinaId);
        if (disciplina == null) {
            throw new ResourceNotFound("Disciplina não encontrada.");
        }
        return disciplina;
    }

    /**
     * Valida se a questão existe e pertence à disciplina informada.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @return a entidade Questao válida.
     * @throws ResourceNotFound caso a questão não exista ou não pertença à disciplina.
     */
    private Questao validateQuestao(long disciplinaId, long questaoId) throws ResourceNotFound {
        Disciplina disciplina = validateDisciplina(disciplinaId);

        Questao questao = questaoRepository.findQuestaoByIdAndDisciplina(questaoId, disciplina);
        if (questao == null) {
            throw new ResourceNotFound("Questão não encontrada.");
        }
        return questao;
    }

    /**
     * Valida se a alternativa existe e pertence à questão informada.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @param alternativaId ID da alternativa.
     * @return a entidade Alternativa válida.
     * @throws ResourceNotFound caso a alternativa não exista.
     */
    private Alternativa validateAlternativa(long disciplinaId, long questaoId, long alternativaId)
            throws ResourceNotFound {

        Questao questao = validateQuestao(disciplinaId, questaoId);

        Alternativa alternativa = repository.findByIdAndQuestao(alternativaId, questao);
        if (alternativa == null) {
            throw new ResourceNotFound("Alternativa não encontrada.");
        }
        return alternativa;
    }

    /**
     * Busca todas as alternativas de uma questão.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @return lista de alternativas da questão.
     * @throws ResourceNotFound caso disciplina ou questão não existam.
     */
    public List<AlternativaDTOResponse> findAll(long disciplinaId, long questaoId) throws ResourceNotFound {
        Questao questao = validateQuestao(disciplinaId, questaoId);

        return repository.findByQuestao(questao)
                .stream()
                .map(x -> modelMapper.map(x, AlternativaDTOResponse.class))
                .toList();
    }

    /**
     * Busca uma alternativa pelo ID.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @param id ID da alternativa.
     * @return DTO da alternativa encontrada.
     * @throws ResourceNotFound caso a alternativa não exista.
     */
    public AlternativaDTOResponse findById(long disciplinaId, long questaoId, long id) throws ResourceNotFound {
        Alternativa entity = validateAlternativa(disciplinaId, questaoId, id);
        return modelMapper.map(entity, AlternativaDTOResponse.class);
    }

    /**
     * Deleta uma alternativa da questão.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @param id ID da alternativa.
     * @throws ResourceNotFound caso a alternativa não exista.
     */
    public void delete(long disciplinaId, long questaoId, long id) throws ResourceNotFound {
        Alternativa entity = validateAlternativa(disciplinaId, questaoId, id);
        repository.delete(entity);
    }

    /**
     * Atualiza uma alternativa existente.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @param id ID da alternativa.
     * @param dto dados para atualização.
     * @return DTO atualizado.
     * @throws ResourceNotFound caso a alternativa não exista.
     */
    public AlternativaDTOResponse update(long disciplinaId, long questaoId, long id, AlternativaDTO dto)
            throws ResourceNotFound {

        Alternativa entity = validateAlternativa(disciplinaId, questaoId, id);

        entity.setDescricao(dto.getDescricao());
        entity.setCorreta(dto.getCorreta());

        repository.save(entity);
        return modelMapper.map(entity, AlternativaDTOResponse.class);
    }

    /**
     * Cria uma nova alternativa para uma questão.
     *
     * Regras:
     * - A alternativa sempre pertence a uma questão.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @param dto dados da alternativa.
     * @return DTO da alternativa criada.
     * @throws BusinessLogicException caso alguma regra de negócio seja violada.
     * @throws ResourceNotFound caso disciplina ou questão não existam.
     */
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
