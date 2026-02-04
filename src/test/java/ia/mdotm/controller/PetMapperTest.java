package ia.mdotm.controller;

import ia.mdotm.model.Pet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PetMapperTest {

    private final PetMapper sut = new PetMapper();

    @Test
    void toDomain_fromRequest_setsNullId() {

        PetRequestDto request = new PetRequestDto("Fido", "dog", 3, "Anna");

        Pet pet = sut.toDomain(request);

        assertThat(pet.id()).isNull();
        assertThat(pet.name()).isEqualTo("Fido");
        assertThat(pet.species()).isEqualTo("dog");
        assertThat(pet.age()).isEqualTo(3);
        assertThat(pet.ownerName()).isEqualTo("Anna");
    }

    @Test
    void toDomain_withId_mapsAllFields() {

        PetRequestDto request = new PetRequestDto("Misty", "cat", 2, "Thomas");

        Pet pet = sut.toDomain(7L, request);

        assertThat(pet.id()).isEqualTo(7L);
        assertThat(pet.name()).isEqualTo("Misty");
        assertThat(pet.species()).isEqualTo("cat");
        assertThat(pet.age()).isEqualTo(2);
        assertThat(pet.ownerName()).isEqualTo("Thomas");
    }

    @Test
    void toResponse_mapsAllFields() {

        Pet pet = new Pet(9L, "Moon", "cat", 1, "Martha");

        PetResponseDto response = sut.toResponse(pet);

        assertThat(response.id()).isEqualTo(9L);
        assertThat(response.name()).isEqualTo("Moon");
        assertThat(response.species()).isEqualTo("cat");
        assertThat(response.age()).isEqualTo(1);
        assertThat(response.ownerName()).isEqualTo("Martha");
    }
}
