package nl.rabobank.powerofattorney.akalasok.controller;

import nl.rabobank.powerofattorney.akalasok.dto.CreditCardDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.model.AuthorityService;
import nl.rabobank.powerofattorney.akalasok.service.CreditCardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CreditCardController.class)
public class CreditCardControllerTest {

    private static final String CREDIT_CARD_ID = "1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorityService authorityService;

    @MockBean
    private CreditCardService creditCardService;

    @MockBean
    private DataSource dataSource;

    @Test
    public void ifNoUser_401() throws Exception {
        mockMvc.perform(get("/credit-cards/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "CREDIT_CARD+HOLDER+2")
    public void ifBadAuthority_403() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.CREDIT_CARD, CREDIT_CARD_ID, List.of("CREDIT_CARD+HOLDER+2"))).thenReturn(false);

        mockMvc.perform(get("/credit-cards/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "CREDIT_CARD+HOLDER+1")
    public void ifNotFound_404() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.CREDIT_CARD, CREDIT_CARD_ID, List.of("CREDIT_CARD+HOLDER+1"))).thenReturn(true);
        when(creditCardService.getById(CREDIT_CARD_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/credit-cards/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "CREDIT_CARD+HOLDER+1")
    public void ifFound_200() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.CREDIT_CARD, CREDIT_CARD_ID, List.of("CREDIT_CARD+HOLDER+1"))).thenReturn(true);
        CreditCardDto creditCard = creditCard();
        when(creditCardService.getById(CREDIT_CARD_ID)).thenReturn(Optional.of(creditCard));

        mockMvc.perform(get("/credit-cards/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(creditCard.getId())))
                .andExpect(jsonPath("$.cardNumber", is(creditCard.getCardNumber())))
                .andExpect(jsonPath("$.sequenceNumber", is(creditCard.getSequenceNumber())))
                .andExpect(jsonPath("$.cardHolder", is(creditCard.getCardHolder())))
                .andExpect(jsonPath("$.monthlyLimit", is(creditCard.getMonthlyLimit())));
    }

    private CreditCardDto creditCard() {
        CreditCardDto dto = new CreditCardDto();
        dto.setId(CREDIT_CARD_ID);
        dto.setCardHolder("CardHolder");
        dto.setCardNumber(1234567890);
        dto.setMonthlyLimit(500.);
        dto.setStatus("status");
        dto.setSequenceNumber(54321);
        return dto;
    }
}