package br.com.edu.ufersa.projeto_quiz.Model.repository;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlternativaDTO;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AlternativaRepository extends JpaRepository<Alternativa, Long> {
}
