package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Service.QuestaoService;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/disciplinas/{disciplinaId}/questoes")
public class QuestaoController {
    @Autowired
    private QuestaoService service;

    @GetMapping()
    public ResponseEntity<List<QuestaoDTOResponse>> findAll(@PathVariable long disciplinaId) throws ResourceNotFound {
        List<QuestaoDTOResponse> questoes = service.findAll(disciplinaId);


        // TODO adicionar hateoas para acao de responder quiz
        return ResponseEntity.ok(questoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestaoDTOResponse> findById(@PathVariable long disciplinaId, @PathVariable Long id) throws ResourceNotFound {
        QuestaoDTOResponse questao = service.findById(id);
        return ResponseEntity.ok(questao);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long disciplinaId,@PathVariable Long id){
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestaoDTO> edit(@PathVariable long disciplinaId, @PathVariable Long id, @Valid @RequestBody QuestaoDTO dto) throws ResourceNotFound {
        QuestaoDTO  questaoDTO = service.edit(id,dto);
        addSelfLink(questaoDTO, id, disciplinaId);
        return ResponseEntity.ok(questaoDTO);
    }


    public void addSelfLink(QuestaoDTO questao, long id, long disciplinaId) throws ResourceNotFound {
        questao.add(linkTo(methodOn(QuestaoController.class).findById(id, disciplinaId)).withSelfRel());
    }
}
