package ia.mdotm.service;


import java.util.List;
import java.util.Optional;

public interface PetService {

    Pet create(Pet request);

    Optional<Pet> getById(long id);

    List<Pet> list();

    Pet update(Pet request);

    boolean delete(long id);
}
