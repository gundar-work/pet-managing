package ia.mdotm.controller;

import ia.mdotm.model.Pet;
import ia.mdotm.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class PetControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PetService petService;

  @MockitoBean
  private PetMapper mapper;

  @Test
  void create_returnsCreatedWithLocation() throws Exception {

    Pet toCreate = new Pet(null, "Fido", "dog", 3, "Anna");
    Pet created = new Pet(5L, "Fido", "dog", 3, "Anna");
    PetResponseDto response = new PetResponseDto(5L, "Fido", "dog", 3, "Anna");

    when(mapper.toDomain(any(PetRequestDto.class))).thenReturn(toCreate);
    when(petService.create(toCreate)).thenReturn(created);
    when(mapper.toResponse(created)).thenReturn(response);

    mockMvc.perform(post("/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":"Fido","species":"dog","age":3,"ownerName":"Anna"}
          """))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", "/pets/5"))
      .andExpect(jsonPath("$.id").value(5))
      .andExpect(jsonPath("$.name").value("Fido"));

    verify(petService).create(toCreate);
  }

  @Test
  void getById_returnsOkWhenFound() throws Exception {

    Pet pet = new Pet(7L, "Luna", "cat", 2, "Martha");
    PetResponseDto response = new PetResponseDto(7L, "Luna", "cat", 2, "Martha");

    when(petService.getById(7L)).thenReturn(Optional.of(pet));
    when(mapper.toResponse(pet)).thenReturn(response);

    mockMvc.perform(get("/pets/7"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(7))
      .andExpect(jsonPath("$.name").value("Luna"));
  }

  @Test
  void getById_returnsNotFoundWhenMissing() throws Exception {

    when(petService.getById(9L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/pets/9"))
      .andExpect(status().isNotFound());
  }

  @Test
  void list_returnsAllPets() throws Exception {

    List<Pet> pets = List.of(
      new Pet(1L, "Bella", "dog", 3, "Oliver"),
      new Pet(2L, "Milo", "cat", 1, "Sophie")
    );
    when(petService.list()).thenReturn(pets);
    when(mapper.toResponse(pets.get(0))).thenReturn(new PetResponseDto(1L, "Bella", "dog", 3, "Oliver"));
    when(mapper.toResponse(pets.get(1))).thenReturn(new PetResponseDto(2L, "Milo", "cat", 1, "Sophie"));

    mockMvc.perform(get("/pets"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$[0].id").value(1))
      .andExpect(jsonPath("$[1].id").value(2));
  }

  @Test
  void update_returnsOk() throws Exception {

    Pet toUpdate = new Pet(3L, "Max", "dog", 4, "Ethan");
    PetResponseDto response = new PetResponseDto(3L, "Max", "dog", 4, "Ethan");

    when(mapper.toDomain(eq(3L), any(PetRequestDto.class))).thenReturn(toUpdate);
    when(petService.update(toUpdate)).thenReturn(toUpdate);
    when(mapper.toResponse(toUpdate)).thenReturn(response);

    mockMvc.perform(put("/pets/3")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"name":"Max","species":"dog","age":4,"ownerName":"Ethan"}
          """))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(3))
      .andExpect(jsonPath("$.name").value("Max"));
  }

  @Test
  void delete_returnsNoContentWhenDeleted() throws Exception {

    when(petService.delete(4L)).thenReturn(true);

    mockMvc.perform(delete("/pets/4"))
      .andExpect(status().isNoContent());
  }

  @Test
  void delete_returnsNotFoundWhenMissing() throws Exception {

    when(petService.delete(8L)).thenReturn(false);

    mockMvc.perform(delete("/pets/8"))
      .andExpect(status().isNotFound());
  }
}
