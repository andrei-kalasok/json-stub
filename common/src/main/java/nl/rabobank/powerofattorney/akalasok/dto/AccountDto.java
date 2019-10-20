package nl.rabobank.powerofattorney.akalasok.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AccountDto {

    public static final String DD_MM_YYYY = "dd-MM-yyyy";

    private String id;
    private String owner;
    private Double balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DD_MM_YYYY)
    private LocalDate created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DD_MM_YYYY)
    private LocalDate ended;


}
