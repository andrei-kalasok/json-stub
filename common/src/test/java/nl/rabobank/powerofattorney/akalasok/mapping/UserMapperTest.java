package nl.rabobank.powerofattorney.akalasok.mapping;

import nl.rabobank.powerofattorney.akalasok.dto.UserDto;
import nl.rabobank.powerofattorney.akalasok.persistence.User;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserMapperTest {

    private static final PasswordEncoder PASSWORD_ENCODER = new Pbkdf2PasswordEncoder("encoderSecret");

    private final UserMapper mapper = new UserMapper(PASSWORD_ENCODER);

    @Test
    public void dtoToEntity() {
        UserDto dto = new UserDto();
        dto.setPassword("password");

        User entity = mapper.toEntity(dto);

        assertTrue(PASSWORD_ENCODER.matches("password", entity.getPassword()));
    }

    @Test
    public void dontMapPassword() {
        User entity = new User();
        entity.setPassword(mapper.passwordToHash("password"));

        UserDto dto = mapper.toDto(entity);

        assertNull(dto.getPassword());
    }
}