package ia.mdotm.controller;

import ia.mdotm.service.Pet;
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
    public ResponseEntity<ResponsePetDto> create(@Valid @RequestBody PetRequestDto request) {

        Pet newPet = mapper.toDomain(request);
        Pet created = petService.create(newPet);

        ResponsePetDto response = mapper.toResponse(created);
        URI location = URI.create("/pets/" + created.id());

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePetDto> getById(@PathVariable Long id) {

        ResponsePetDto petDto = petService.getById(id)
                .map(mapper::toResponse)
                .orElse(null);

        if (petDto == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(petDto);
        }

    }

    @GetMapping
    public ResponseEntity<List<ResponsePetDto>> list() {

        List<ResponsePetDto> petDtos = petService.list().stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(petDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePetDto> update(@PathVariable Long id, @Valid @RequestBody PetRequestDto request) {

        Pet updatedPet = mapper.toDomain(id, request);

        Pet petCreated = petService.update(updatedPet);
        ResponsePetDto petResponse = mapper.toResponse(petCreated);

        return ResponseEntity.ok(petResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        boolean deleted = petService.delete(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

