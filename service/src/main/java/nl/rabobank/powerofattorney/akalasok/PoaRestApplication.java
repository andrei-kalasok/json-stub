package nl.rabobank.powerofattorney.akalasok;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@SpringBootApplication
public class PoaRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoaRestApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(@Value("${user.password.encoder.secret}") String encoderSecret) {
        return new Pbkdf2PasswordEncoder(encoderSecret);
    }
}
