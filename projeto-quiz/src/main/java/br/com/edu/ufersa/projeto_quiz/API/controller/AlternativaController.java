package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlternativaDTO;
import br.com.edu.ufersa.projeto_quiz.Service.AlternativaService;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/alternativas")
public class AlternativaController {

    private final AlternativaService service;

    @Autowired
    public AlternativaController(AlternativaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AlternativaDTO>> findAll() throws ResourceNotFound {
        List<AlternativaDTO> lista = service.findAll();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlternativaDTO> findById(@PathVariable Long id) throws ResourceNotFound {
        AlternativaDTO dto = service.findById(id);


        // TODO dto.add(linkTo(methodOn(AlternativaController.class).update(id, null)).withRel("atualizar"));

        return ResponseEntity.ok(dto);
    }

    /*
    TODO implementar metodos de update e save
    /*
    @PostMapping
    public ResponseEntity<AlternativaDTO> create(@RequestBody AlternativaDTO dto) {
        AlternativaDTO saved = service.save(dto);
        // Adicionar links no saved...
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    */

    /*

    /*
    @PutMapping("/{id}")
    public ResponseEntity<AlternativaDTO> update(@PathVariable Long id, @RequestBody AlternativaDTO dto) {
        AlternativaDTO updated = service.update(id, dto);
        return findById(updated.getId()); // Retorna com os links atualizados
    }
    */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws ResourceNotFound {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}