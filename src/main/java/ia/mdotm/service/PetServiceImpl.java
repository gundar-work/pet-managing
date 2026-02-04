package ia.mdotm.service;

import ia.mdotm.model.Pet;
import ia.mdotm.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Pet create(Pet pet) {

        if (pet == null) {
            throw new IllegalArgumentException("Pet must not be null");
        }

        return petRepository.save(pet);
    }

    @Override
    public Optional<Pet> getById(long id) {
        return petRepository.findById(id);
    }

    @Override
    public List<Pet> list() {
        return petRepository.findAll();
    }

    @Override
    public Pet update(Pet pet) {

        if (pet == null) {
            throw new IllegalArgumentException("Pet must not be null");
        }

        if (!petRepository.existsById(pet.id())) {
            throw new IllegalArgumentException("Pet with id = [" + pet.id() + "] does not exist");
        }

        return petRepository.save(pet);
    }

    @Override
    public boolean delete(long id) {
        return petRepository.deleteById(id);
    }
}
