package db.migration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.rabobank.powerofattorney.akalasok.SpringUtility;
import nl.rabobank.powerofattorney.akalasok.dto.*;
import nl.rabobank.powerofattorney.akalasok.mapping.*;
import nl.rabobank.powerofattorney.akalasok.persistence.*;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class V2__Initial_data extends BaseJavaMigration {

    private static final Logger LOGGER = LoggerFactory.getLogger(V2__Initial_data.class);
    private static final String REQUESTED_ID_PLACEHOLDER = "{{request.path.[1]}}";

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void migrate(Context context) {
        migrateJsonToDB("common/src/main/resources/testdata/__files/users", UserDto.class,
                SpringUtility.getBean(UserMapper.class), SpringUtility.getBean(UserRepository.class));
        migrateJsonToDB("common/src/main/resources/testdata/__files/accounts", AccountDto.class,
                SpringUtility.getBean(AccountMapper.class), SpringUtility.getBean(AccountRepository.class));
        migrateJsonToDB("common/src/main/resources/testdata/__files/credit-card", CreditCardDto.class,
                SpringUtility.getBean(CreditCardMapper.class), SpringUtility.getBean(CreditCardRepository.class));
        migrateJsonToDB("common/src/main/resources/testdata/__files/debit-card", DebitCardDto.class,
                SpringUtility.getBean(DebitCardMapper.class), SpringUtility.getBean(DebitCardRepository.class));
        migrateJsonToDB("common/src/main/resources/testdata/__files/poa", PoaDto.class,
                SpringUtility.getBean(PoaMapper.class), SpringUtility.getBean(PoaRepository.class));
    }

    private <E, D> void migrateJsonToDB(String path, Class<D> valueType, AbstractMapper<E, D> mapper, CrudRepository<E, ?> repository) {
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            List<String> result = walk.map(Path::toString)
                    .filter(f -> f.endsWith(".json")).collect(Collectors.toList());

            result.forEach(file -> processFile(file, valueType, mapper, repository));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <E, D> void processFile(String filePath, Class<D> valueType, AbstractMapper<E, D> mapper, CrudRepository<E, ?> repository) {
        try {
            LOGGER.info("Processing {}", filePath);
            String fileContent = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
            String jsonString = addRequestedId(fileContent, filePath);
            D dto = objectMapper.readValue(jsonString, valueType);
            E entity = mapper.toEntity(dto);
            repository.save(entity);
            LOGGER.info("{} is migrated", filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String addRequestedId(String content, String filePath) {
        int ind = filePath.lastIndexOf("/");
        String fileName = filePath.substring(ind + 1);
        return content.replace(REQUESTED_ID_PLACEHOLDER, fileName.replace(".json", ""));
    }


}
