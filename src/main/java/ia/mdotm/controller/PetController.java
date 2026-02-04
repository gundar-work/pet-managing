package ia.mdotm.controller;

import ia.mdotm.model.Pet;
import ia.mdotm.service.PetService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger log = LoggerFactory.getLogger(PetController.class);

  private final PetService petService;
  private final PetMapper mapper;

  public PetController(PetService petService, PetMapper mapper) {
    this.petService = petService;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<PetResponseDto> create(@Valid @RequestBody PetRequestDto request) {

    log.info("Creating Pet");
    Pet newPet = mapper.toDomain(request);
    Pet created = petService.create(newPet);

    PetResponseDto response = mapper.toResponse(created);
    log.info("Pet created with id={}", created.id());

    URI location = URI.create("/pets/" + created.id());
    return ResponseEntity.created(location).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PetResponseDto> getById(@PathVariable("id") long id) {

    log.info("Getting Pet by id = [{}]", id);
    PetResponseDto petDto = petService.getById(id)
      .map(mapper::toResponse)
      .orElse(null);

    if (petDto == null) {
      log.info("Pet not found for id = [{}]", id);
      return ResponseEntity.notFound().build();
    } else {
      log.info("Pet found for id = [{}]", id);
      return ResponseEntity.ok(petDto);
    }

  }

  @GetMapping
  public ResponseEntity<List<PetResponseDto>> list() {

    log.info("Listing Pets");
    List<PetResponseDto> petsResponse = petService.list().stream()
      .map(mapper::toResponse)
      .toList();

    log.info("Found [{}] pets", petsResponse.size());
    return ResponseEntity.ok(petsResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PetResponseDto> update(@PathVariable("id") long id, @Valid @RequestBody PetRequestDto request) {

    log.info("Updating pet id = [{}]", id);
    Pet updatedPet = mapper.toDomain(id, request);

    Pet petCreated = petService.update(updatedPet);
    PetResponseDto petResponse = mapper.toResponse(petCreated);

    log.info("Pet updated id = [{}]", id);
    return ResponseEntity.ok(petResponse);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") long id) {

    log.info("Deleting pet id = [{}]", id);
    boolean deleted = petService.delete(id);

    if (deleted) {
      log.info("Pet deleted id = [{}]", id);
      return ResponseEntity.noContent().build();
    } else {
      log.info("Pet not found to delete id = [{}]", id);
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/throw-error")
  public ResponseEntity<Void> throwError() {

    log.info("Forcing test error");
    throw new RuntimeException("Internal error!");
  }
}
