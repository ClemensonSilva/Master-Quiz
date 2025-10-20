package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Usuario;
import br.com.edu.ufersa.projeto_quiz.Model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private   UsuarioRepository usuarioRepository;
    // vai puxa do Db o user baseado na sua senha'
    @Override
    public UserDetails loadUserByUsername(String email){
        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario != null){
                UserDetails user = new org.springframework.security.core
                        .userdetails.User(email, usuario.getSenha(),
                        true, true, true,
                        true, AuthorityUtils.createAuthorityList("USER"));
                return user;
            } else throw new UsernameNotFoundException("Usuário não encontrado!");
    }

}
