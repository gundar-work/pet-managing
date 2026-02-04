package ia.mdotm.controller;

import ia.mdotm.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public Pet toDomain(PetRequestDto petRequest) {

        return new Pet(
                null,
                petRequest.name(),
                petRequest.species(),
                petRequest.age(),
                petRequest.ownerName()
        );
    }

    public Pet toDomain(long id, PetRequestDto petRequest) {

        return new Pet(
                id,
                petRequest.name(),
                petRequest.species(),
                petRequest.age(),
                petRequest.ownerName()
        );
    }

    public PetResponseDto toResponse(Pet pet) {

        return new PetResponseDto(
                pet.id(),
                pet.name(),
                pet.species(),
                pet.age(),
                pet.ownerName()
        );
    }
}
