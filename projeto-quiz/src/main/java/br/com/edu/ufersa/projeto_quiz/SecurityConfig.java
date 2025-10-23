package br.com.edu.ufersa.projeto_quiz;
import br.com.edu.ufersa.projeto_quiz.Service.UserDetailsServiceImpl;
import br.com.edu.ufersa.projeto_quiz.filters.AuthorizationFilter;
import br.com.edu.ufersa.projeto_quiz.filters.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*"); // Permite todas as origens (ajuste conforme necessário)
        corsConfig.addAllowedMethod("*"); // Permite todos os métodos HTTP (ajuste conforme necessário)
        corsConfig.addAllowedHeader("*"); // Permite todos os cabeçalhos (ajuste conforme necessário)
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder( passwordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .addFilterBefore(new LoginFilter("/api/v1/login",authenticationManager),
                        UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(new AuthorizationFilter(), BasicAuthenticationFilter.class)

                .authorizeHttpRequests(authorizeRequests ->

                        authorizeRequests
                                .requestMatchers(HttpMethod.POST, "/api/v1/login", "/api/v1/usuarios/alunos", "/api/v1/usuarios/professores").permitAll()
                                .requestMatchers(HttpMethod.GET,  "/api/v1/usuarios/alunos", "/api/v1/usuarios/professores").permitAll()
                                .requestMatchers(HttpMethod.POST, "/h2-console/**").permitAll()
                                .anyRequest().authenticated()
                )
                .authenticationManager(authenticationManager)

                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .headers(headers ->
                        headers.frameOptions(Customizer.withDefaults()).disable() // Desabilita a proteção contra frame options
                );
        return http.build();
    }
}
