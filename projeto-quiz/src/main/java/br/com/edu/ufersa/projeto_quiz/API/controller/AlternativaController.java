package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlternativaDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.AlternativaDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Service.AlternativaService;
import br.com.edu.ufersa.projeto_quiz.exception.BusinessLogicException;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controlador responsável por gerenciar as alternativas pertencentes a uma questão,
 * que por sua vez pertence a uma disciplina.
 *
 * <p>Este controller segue o padrão REST para operações CRUD e utiliza HATEOAS
 * para adicionar links de navegação aos recursos retornados.</p>
 *
 * <p>Endpoint base:
 * /api/v1/disciplinas/{disciplinaId}/questoes/{questaoId}/alternativas</p>
 */
@RestController
@RequestMapping("/api/v1/disciplinas/{disciplinaId}/questoes/{questaoId}/alternativas")
public class AlternativaController {

    private final AlternativaService service;
    private final ModelMapper modelMapper;

    @Autowired
    public AlternativaController(AlternativaService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    /**
     * Recupera todas as alternativas pertencentes a uma questão específica dentro de uma disciplina.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @return Lista de {@link AlternativaDTOResponse} com links HATEOAS.
     * @throws ResourceNotFound Caso a disciplina ou questão não existam.
     * @throws BusinessLogicException Caso exista algum erro de integridade ou regra de negócio.
     */
    @GetMapping
    public ResponseEntity<List<AlternativaDTOResponse>> findAll(
            @PathVariable long disciplinaId,
            @PathVariable long questaoId) throws ResourceNotFound, BusinessLogicException {

        List<AlternativaDTOResponse> lista = service.findAll(disciplinaId, questaoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        for (AlternativaDTOResponse alternativa : lista) {
            addSelfLink(alternativa, disciplinaId, questaoId);
            addLinks(alternativa, alternativa.getId(), disciplinaId, questaoId);
        }

        return ResponseEntity.ok(lista);
    }
    /**
     * Recupera uma alternativa específica pelo seu ID.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @param id ID da alternativa.
     * @return A alternativa encontrada com links HATEOAS.
     * @throws ResourceNotFound Caso a alternativa, questão ou disciplina não existam.
     */

    @GetMapping("/{id}")
    public ResponseEntity<AlternativaDTOResponse> findById(
            @PathVariable long disciplinaId,
            @PathVariable long questaoId,
            @PathVariable long id) throws ResourceNotFound {

        AlternativaDTOResponse dto = service.findById(disciplinaId, questaoId, id);

        addSelfLink(dto, disciplinaId, questaoId);
        addLinks(dto, id, disciplinaId, questaoId);

        return ResponseEntity.ok(dto);
    }

    /**
     * Cria uma nova alternativa dentro de uma questão.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @param dto Dados da alternativa a ser criada.
     * @return A alternativa criada com links HATEOAS e status HTTP 201.
     * @throws BusinessLogicException Caso viole regras de negócio (ex.: mais de uma correta).
     * @throws ResourceNotFound Caso disciplina ou questão não existam.
     */
    @PostMapping
    public ResponseEntity<AlternativaDTOResponse> create(
            @PathVariable long disciplinaId,
            @PathVariable long questaoId,
            @RequestBody AlternativaDTO dto) throws BusinessLogicException, ResourceNotFound {

        AlternativaDTOResponse saved = service.save(disciplinaId, questaoId, dto);

        addSelfLink(saved, disciplinaId, questaoId);
        addLinks(saved, saved.getId(), disciplinaId, questaoId);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Atualiza os dados de uma alternativa existente.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @param id ID da alternativa.
     * @param dto Dados atualizados.
     * @return A alternativa atualizada com links HATEOAS.
     * @throws ResourceNotFound Caso a alternativa, questão ou disciplina não existam.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlternativaDTOResponse> update(
            @PathVariable long disciplinaId,
            @PathVariable long questaoId,
            @PathVariable long id,
            @RequestBody AlternativaDTO dto) throws ResourceNotFound {

        AlternativaDTOResponse updated = service.update(disciplinaId, questaoId, id, dto);

        addSelfLink(updated, disciplinaId, questaoId);
        addLinks(updated, id, disciplinaId, questaoId);

        return ResponseEntity.ok(updated);
    }

    /**
     * Remove uma alternativa de uma questão específica.
     *
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @param id ID da alternativa.
     * @return Resposta HTTP 204 caso a exclusão seja bem-sucedida.
     * @throws ResourceNotFound Caso a alternativa, questão ou disciplina não existam.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable long disciplinaId,
            @PathVariable long questaoId,
            @PathVariable long id) throws ResourceNotFound {

        service.delete(disciplinaId, questaoId, id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Adiciona o link "self" ao DTO da alternativa.
     *
     * @param dto Alternativa DTO.
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @throws ResourceNotFound Necessário para compatibilidade com chamadas HATEOAS.
     */

    private void addSelfLink(AlternativaDTOResponse dto, long disciplinaId, long questaoId) throws ResourceNotFound {
        dto.add(linkTo(methodOn(AlternativaController.class)
                .findById(disciplinaId, questaoId, dto.getId())).withSelfRel());
    }
    /**
     * Adiciona links complementares (atualizar e deletar) ao DTO.
     *
     * @param dto Alternativa DTO.
     * @param id ID da alternativa.
     * @param disciplinaId ID da disciplina.
     * @param questaoId ID da questão.
     * @throws ResourceNotFound Necessário para compatibilidade com chamadas HATEOAS.
     */
    private void addLinks(AlternativaDTOResponse dto, long id, long disciplinaId, long questaoId) throws ResourceNotFound {
        dto.add(linkTo(methodOn(AlternativaController.class)
                .update(disciplinaId, questaoId, id, modelMapper.map(dto, AlternativaDTO.class)))
                .withRel("atualizar"));

        dto.add(linkTo(methodOn(AlternativaController.class)
                .delete(disciplinaId, questaoId, id)).withRel("deletar"));
    }
}
