package br.com.edu.ufersa.projeto_quiz;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class     ProjetoQuizApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoQuizApplication.class, args);
	}
    @Bean
    public ModelMapper modelMapperBuild(){
        return new ModelMapper();
    }
}
