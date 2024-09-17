package brianpelinku.u5w3d2.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // regole di autorizzazione direttamente sui metodi
public class Config {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // disabilito i componenti di defualt che non mi interessano
        httpSecurity.formLogin(http -> http.disable());
        httpSecurity.csrf(http -> http.disable());

        // disabilito le sessioni
        httpSecurity.sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // disabilito il 401 su ogni richiesta
        httpSecurity.authorizeHttpRequests(http -> http.requestMatchers("/**").permitAll());

        return httpSecurity.build();
    }

    // metodo di sicurezza per le password
    @Bean
    PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(11); // l'11 è il num di round (volte che viene eseguito l'algoritmo) utile per regolare la velocità di esecuzione.
    }
}
