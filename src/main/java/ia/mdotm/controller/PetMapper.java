package ia.mdotm.controller;

import ia.mdotm.service.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public Pet toDomain(PetRequestDto request) {

        if (request == null) {
            throw new IllegalArgumentException("CreatePetRequest must not be null");
        }

        return new Pet(
                null,
                request.name(),
                request.species(),
                request.age(),
                request.ownerName()
        );
    }

    public Pet toDomain(long id, PetRequestDto request) {

        if (request == null) {
            throw new IllegalArgumentException("UpdatePetRequest must not be null");
        }

        return new Pet(
                id,
                request.name(),
                request.species(),
                request.age(),
                request.ownerName()
        );
    }

    public ResponsePetDto toResponse(Pet pet) {

        if (pet == null) {
            throw new IllegalArgumentException("Pet must not be null");
        }

        return new ResponsePetDto(
                pet.id(),
                pet.name(),
                pet.species(),
                pet.age(),
                pet.ownerName()
        );
    }
}
