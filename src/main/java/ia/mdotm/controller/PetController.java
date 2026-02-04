package ia.mdotm.controller;

import ia.mdotm.model.Pet;
import ia.mdotm.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

  private final PetService petService;
  private final PetMapper mapper;

  public PetController(PetService petService, PetMapper mapper) {
    this.petService = petService;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<PetResponseDto> create(@Valid @RequestBody PetRequestDto request) {

    Pet newPet = mapper.toDomain(request);
    Pet created = petService.create(newPet);

    PetResponseDto response = mapper.toResponse(created);
    URI location = URI.create("/pets/" + created.id());

    return ResponseEntity.created(location).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PetResponseDto> getById(@PathVariable("id") long id) {

    PetResponseDto petDto = petService.getById(id)
      .map(mapper::toResponse)
      .orElse(null);

    if (petDto == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(petDto);
    }

  }

  @GetMapping
  public ResponseEntity<List<PetResponseDto>> list() {

    List<PetResponseDto> petsResponse = petService.list().stream()
      .map(mapper::toResponse)
      .toList();

    return ResponseEntity.ok(petsResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PetResponseDto> update(@PathVariable("id") long id, @Valid @RequestBody PetRequestDto request) {

    Pet updatedPet = mapper.toDomain(id, request);

    Pet petCreated = petService.update(updatedPet);
    PetResponseDto petResponse = mapper.toResponse(petCreated);

    return ResponseEntity.ok(petResponse);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") long id) {

    boolean deleted = petService.delete(id);

    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}

