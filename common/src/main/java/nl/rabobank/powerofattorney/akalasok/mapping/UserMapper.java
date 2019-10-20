package nl.rabobank.powerofattorney.akalasok.mapping;

import nl.rabobank.powerofattorney.akalasok.dto.UserDto;
import nl.rabobank.powerofattorney.akalasok.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserDto> {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected User newEntity() {
        return new User();
    }

    @Override
    protected UserDto newDto() {
        return new UserDto();
    }

    @Override
    protected void customEntityMapping(UserDto dto, User entity) {
        entity.setPassword(passwordToHash(dto.getPassword()));
    }

    @Override
    protected void customDtoMapping(User entity, UserDto dto) {
        dto.setPassword(null);
    }

    String passwordToHash(String password) {
        return passwordEncoder.encode(password);
    }
}
