package ia.mdotm.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PetValidationIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void create_returnsBadRequestWhenValidationFails() throws Exception {

    mockMvc.perform(post("/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":" ","species":" ","age":-1,"ownerName":"Anna"}
          """))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.details.fieldErrors.name").value("must not be blank"))
      .andExpect(jsonPath("$.details.fieldErrors.species").value("must not be blank"))
      .andExpect(jsonPath("$.details.fieldErrors.age").value("must be greater than or equal to 0"));
  }

  @Test
  void update_returnsBadRequestWhenValidationFails() throws Exception {

    mockMvc.perform(put("/pets/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":" ","species":" ","age":-5,"ownerName":"Anna"}
          """))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.details.fieldErrors.name").value("must not be blank"))
      .andExpect(jsonPath("$.details.fieldErrors.species").value("must not be blank"))
      .andExpect(jsonPath("$.details.fieldErrors.age").value("must be greater than or equal to 0"));
  }

  @Test
  void create_returnsBadRequestWhenJsonIsMalformed() throws Exception {

    mockMvc.perform(post("/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":"Fido","species":"dog","age":3,"ownerName":"Anna"
          """))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("Malformed JSON request"));
  }
}
