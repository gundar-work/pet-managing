package ia.mdotm.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PetCrudIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void crud_flow_worksEndToEnd() throws Exception {

    MvcResult createdResult = mockMvc.perform(post("/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":"Fido","species":"dog","age":3,"ownerName":"Anna"}
          """))
      .andExpect(status().isCreated())
      .andReturn();

    String location = createdResult.getResponse().getHeader("Location");
    assertThat(location).isNotBlank();
    String idPart = location.substring(location.lastIndexOf('/') + 1);
    long id = Long.parseLong(idPart);

    mockMvc.perform(get("/pets/{id}", id))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(id))
      .andExpect(jsonPath("$.name").value("Fido"));

    mockMvc.perform(put("/pets/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":"Rex","species":"dog","age":4,"ownerName":"Anna"}
          """))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(id))
      .andExpect(jsonPath("$.name").value("Rex"));

    mockMvc.perform(get("/pets/{id}", id))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Rex"))
      .andExpect(jsonPath("$.age").value(4));

    mockMvc.perform(delete("/pets/{id}", id))
      .andExpect(status().isNoContent());

    mockMvc.perform(get("/pets/{id}", id))
      .andExpect(status().isNotFound());
  }

  @Test
  void update_returnsNotFoundWhenPetDoesNotExist() throws Exception {

    mockMvc.perform(put("/pets/{id}", 999L)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":"Max","species":"dog","age":4,"ownerName":"Ethan"}
          """))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value("Pet not found for id = [999]"));
  }

  @Test
  void list_returnsPetsSortedById() throws Exception {

    MvcResult first = mockMvc.perform(post("/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":"Bella","species":"dog","age":2,"ownerName":"Olivia"}
          """))
      .andExpect(status().isCreated())
      .andReturn();

    MvcResult second = mockMvc.perform(post("/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":"Milo","species":"cat","age":1,"ownerName":"Sophie"}
          """))
      .andExpect(status().isCreated())
      .andReturn();

    long firstId = Long.parseLong(first.getResponse().getHeader("Location").substring("http://localhost/pets/".length()));
    long secondId = Long.parseLong(second.getResponse().getHeader("Location").substring("http://localhost/pets/".length()));

    mockMvc.perform(get("/pets"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value(firstId))
      .andExpect(jsonPath("$[1].id").value(secondId));
  }

  @Test
  void list_returnsEmptyWhenNoPets() throws Exception {

    mockMvc.perform(get("/pets"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  void delete_returnsNotFoundWhenPetDoesNotExist() throws Exception {

    mockMvc.perform(delete("/pets/{id}", 777L))
      .andExpect(status().isNotFound());
  }

}
