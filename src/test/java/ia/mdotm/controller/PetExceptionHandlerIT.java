package ia.mdotm.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PetExceptionHandlerIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void handleNotFound_returnsNotFound() throws Exception {

    mockMvc.perform(put("/pets/{id}", 999L)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":"Max","species":"dog","age":4,"ownerName":"Ethan"}
          """))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value("Pet not found for id = [999]"));
  }

  @Test
  void handleValidation_returnsBadRequest() throws Exception {

    mockMvc.perform(post("/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":" ","species":" ","age":-1,"ownerName":"Anna"}
          """))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("Validation failed"))
      .andExpect(jsonPath("$.details.fieldErrors.name").value("must not be blank"))
      .andExpect(jsonPath("$.details.fieldErrors.species").value("must not be blank"))
      .andExpect(jsonPath("$.details.fieldErrors.age").value("must be greater than or equal to 0"));
  }

  @Test
  void handleBadJson_returnsBadRequest() throws Exception {

    mockMvc.perform(post("/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":"Fido","species":"dog","age":3,"ownerName":"Anna"
          """))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("Malformed JSON request"));
  }

  @Test
  void handleGeneric_returnsInternalServerError() throws Exception {

    mockMvc.perform(get("/pets/throw-error"))
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.message").value("Internal error!"));
  }

}
