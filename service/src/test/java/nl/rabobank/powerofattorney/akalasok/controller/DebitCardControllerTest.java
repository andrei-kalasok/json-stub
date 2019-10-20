package nl.rabobank.powerofattorney.akalasok.controller;

import nl.rabobank.powerofattorney.akalasok.dto.DebitCardDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.model.AuthorityService;
import nl.rabobank.powerofattorney.akalasok.service.DebitCardService;
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
@WebMvcTest(DebitCardController.class)
public class DebitCardControllerTest {

    private static final String DEBIT_CARD_ID = "1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorityService authorityService;

    @MockBean
    private DebitCardService debitCardService;

    @MockBean
    private DataSource dataSource;

    @Test
    public void ifNoUser_401() throws Exception {
        mockMvc.perform(get("/debit-cards/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "DEBIT_CARD+HOLDER+2")
    public void ifBadAuthority_403() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.DEBIT_CARD, DEBIT_CARD_ID, List.of("DEBIT_CARD+HOLDER+2"))).thenReturn(false);

        mockMvc.perform(get("/debit-cards/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "DEBIT_CARD+HOLDER+1")
    public void ifNotFound_404() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.DEBIT_CARD, DEBIT_CARD_ID, List.of("DEBIT_CARD+HOLDER+1"))).thenReturn(true);
        when(debitCardService.getById(DEBIT_CARD_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/debit-cards/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "DEBIT_CARD+HOLDER+1")
    public void ifFound_200() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.DEBIT_CARD, DEBIT_CARD_ID, List.of("DEBIT_CARD+HOLDER+1"))).thenReturn(true);
        DebitCardDto debitCard = debitCard();
        when(debitCardService.getById(DEBIT_CARD_ID)).thenReturn(Optional.of(debitCard));

        mockMvc.perform(get("/debit-cards/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(debitCard.getId())))
                .andExpect(jsonPath("$.cardNumber", is(debitCard.getCardNumber())))
                .andExpect(jsonPath("$.sequenceNumber", is(debitCard.getSequenceNumber())))
                .andExpect(jsonPath("$.cardHolder", is(debitCard.getCardHolder())))
                .andExpect(jsonPath("$.atmLimit.limit", is(debitCard.getAtmLimit().getLimit())))
                .andExpect(jsonPath("$.atmLimit.periodUnit", is(debitCard.getAtmLimit().getPeriodUnit())))
                .andExpect(jsonPath("$.posLimit.limit", is(debitCard.getPosLimit().getLimit())))
                .andExpect(jsonPath("$.posLimit.periodUnit", is(debitCard.getPosLimit().getPeriodUnit())))
                .andExpect(jsonPath("$.contactless", is(debitCard.getContactless())));
    }

    private DebitCardDto debitCard() {
        DebitCardDto dto = new DebitCardDto();
        dto.setId(DEBIT_CARD_ID);
        dto.setCardHolder("CardHolder");
        dto.setCardNumber(1234567890);
        dto.setAtmLimit(limit(20., "PER_DAY"));
        dto.setPosLimit(limit(200., "PER_WEEK"));
        dto.setStatus("status");
        dto.setSequenceNumber(54321);
        dto.setContactless(true);
        return dto;
    }

    public DebitCardDto.Limit limit(Double limit, String periodUnit) {
        DebitCardDto.Limit cardLimit = new DebitCardDto.Limit();
        cardLimit.setLimit(limit);
        cardLimit.setPeriodUnit(periodUnit);
        return cardLimit;
    }
}