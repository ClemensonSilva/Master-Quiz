package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlternativaDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuestaoRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.DisciplinaRepository;
import br.com.edu.ufersa.projeto_quiz.exception.BusinessLogicException;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Serviço responsável pelo gerenciamento de questões dentro do sistema.
 * Realiza operações de criação, edição, remoção, listagem e consulta de questões,
 * incluindo validações de regras de negócio e associação com disciplinas e quizzes.
 */
@Service
public class QuestaoService {
    private final QuestaoRepository repository;
    private final DisciplinaRepository disciplinaRepository;
    private final ModelMapper mapper;
    private final ModelMapper modelMapper;

    @Autowired
    public QuestaoService(QuestaoRepository repository, DisciplinaRepository disciplinaRepository, ModelMapper mapper, ModelMapper modelMapper) {
        this.repository = repository;
        this.disciplinaRepository = disciplinaRepository;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
    }


    /**
     * Retorna todas as questões associadas a uma disciplina específica.
     *
     * @param disciplinaId ID da disciplina cujas questões devem ser recuperadas.
     * @return lista de {@link QuestaoDTOResponse}.
     * @throws ResourceNotFound caso não exista nenhuma questão cadastrada para a disciplina.
     */
    public List<QuestaoDTOResponse> findAll(long disciplinaId) throws ResourceNotFound {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(disciplinaId);

        List<Questao> questoes = repository.findAllByDisciplina(disciplina);

        if (questoes.isEmpty()) {
            throw new ResourceNotFound("Não há nenhuma questão nessa disciplina.");
        }

        return questoes
                .stream()
                .map(x -> mapper.map(x, QuestaoDTOResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca uma questão pelo seu ID.
     *
     * @param id ID da questão a ser pesquisada.
     * @return {@link QuestaoDTOResponse} encontrada ou {@code null} caso não exista.
     */
    public QuestaoDTOResponse findById(long id) {
        Optional<Questao> questao = repository.findById(id);

        return questao
                .map(q -> mapper.map(q, QuestaoDTOResponse.class))
                .orElse(null);
    }

    /**
     * Cria e persiste uma nova questão associada a uma disciplina.
     * A questão criada pode posteriormente ser utilizada em múltiplos quizzes.
     *
     * @param questaoDTO dados da questão a ser criada.
     * @param disciplinaId ID da disciplina à qual a questão será vinculada.
     * @return a questão criada como {@link QuestaoDTOResponse}.
     * @throws BusinessLogicException caso a disciplina não exista ou a alternativa correta não seja válida.
     */
    @Transactional
    public QuestaoDTOResponse save(QuestaoDTO questaoDTO, long disciplinaId) throws BusinessLogicException{

        // Verifica a existência da disciplina
        Disciplina disciplina = disciplinaRepository.findDisciplinaById(disciplinaId);
        if (disciplina == null) {
            throw new BusinessLogicException("Só posso adicionar questões em uma disciplina");
        }

        // Criação da instância de questão
        Questao novaQuestao = new Questao();
        novaQuestao.setDescricao(questaoDTO.getDescricao());
        novaQuestao.setDisciplina(disciplina);

        // Mapeamento da alternativa correta e verificação de sua existencia.
        AlternativaDTO alternativaCorreta = questaoDTO.getAlternativas().stream()
                .filter(x -> x.getCorreta() == Boolean.TRUE)
                .findFirst()
                .orElseThrow(() ->
                        new BusinessLogicException("A questão deve possuir uma alternativa correta"));

        // Criação das alternativas
        questaoDTO.getAlternativas().forEach(alternativaDTO -> {
            Alternativa novaAlternativa = new Alternativa();
            novaAlternativa.setDescricao(alternativaDTO.getDescricao());
            novaAlternativa.setCorreta(alternativaDTO.getCorreta());
            novaQuestao.addAlternativa(novaAlternativa);
        });

        // Persistência
        Questao questao = repository.save(novaQuestao);
        return mapper.map(questao, QuestaoDTOResponse.class);
    }

    /**
     * Remove uma questão com base em seu ID.
     *
     * @param id ID da questão a ser removida.
     * @throws ResourceNotFound caso a questão não exista.
     */
    public void delete(long id) throws ResourceNotFound {
        Optional<Questao> questao = repository.findById(id);

        if (questao.isEmpty()) {
            throw new ResourceNotFound("Não há questão para deletar");
        }

        repository.delete(questao.get());
    }


    /**
     * Atualiza os dados de uma questão existente.
     *
     * @param id ID da questão que será atualizada.
     * @param dto dados atualizados da questão.
     * @return {@link QuestaoDTOResponse} contendo os dados atualizados.
     * @throws ResourceNotFound caso a questão não seja encontrada.
     */
    @Transactional
    public QuestaoDTOResponse edit(long id, @Valid QuestaoDTO dto) throws ResourceNotFound {
        Questao questao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Questão não encontrada"));

        questao.setDescricao(dto.getDescricao());

        questao.getAlternativas().clear();

        for (AlternativaDTO altDto : dto.getAlternativas()) {
            Alternativa novaAlt = modelMapper.map(altDto, Alternativa.class);
            novaAlt.setQuestao(questao);
            questao.getAlternativas().add(novaAlt);
        }

        repository.save(questao);
        return mapper.map(questao, QuestaoDTOResponse.class);
    }


    /**
     * Retorna as questões associadas a um quiz específico.
     *
     * @param quiz entidade {@link Quiz} cujo conjunto de questões será consultado.
     * @return lista de {@link QuestaoDTO}.
     */
    public List<QuestaoDTO> findByQuiz(Quiz quiz) {
        List<Questao> questoes = repository.findQuestoesByQuiz(quiz);

        return questoes
                .stream()
                .map(x -> mapper.map(x, QuestaoDTO.class))
                .collect(Collectors.toList());
    }


}
