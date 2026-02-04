package ia.mdotm.repository;

import ia.mdotm.service.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepository {

    Pet save(Pet pet);

    Optional<Pet> findById(long id);

    List<Pet> findAll();

    boolean existsById(long id);

    boolean deleteById(long id);
}

