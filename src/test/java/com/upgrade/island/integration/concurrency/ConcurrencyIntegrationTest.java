package com.upgrade.island.integration.concurrency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.island.dto.BookingDTO;
import com.upgrade.island.dto.CreatedBookingDTO;
import com.upgrade.island.integration.db.BookingDBContainer;
import com.upgrade.island.repository.BookingRepository;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConcurrencyIntegrationTest {

  @ClassRule
  public static PostgreSQLContainer postgreSQLContainer = BookingDBContainer.getInstance();

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext wac;
  @Autowired
  private BookingRepository repository;

  private ObjectMapper mapper;
  private DateTimeFormatter dtf;

  @Before
  public void setup() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    mapper = new ObjectMapper();
    dtf = DateTimeFormatter.ISO_DATE;
  }

  @Test
  @SneakyThrows
  public void testOneBooking() {
    ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/island/booking")
        .contentType(MediaType.APPLICATION_JSON)
        .content(createPostBody()))
        .andExpect(status().isOk());
    CreatedBookingDTO response = mapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), CreatedBookingDTO.class);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/island/booking/{bookingId}",response.getBookingId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @SneakyThrows
  private String createPostBody() {
    BookingDTO bookingDTO = BookingDTO.builder()
        .username("joaquincosta")
        .email("joaquincosta@gmail.com")
        .checkin(dtf.format(LocalDate.now().plusDays(1)))
        .checkout(dtf.format(LocalDate.now().plusDays(2)))
        .build();
    return mapper.writeValueAsString(bookingDTO);
  }


}
