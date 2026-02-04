package ia.mdotm.service;

import ia.mdotm.model.Pet;
import ia.mdotm.repository.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {

  private static final Logger log = LoggerFactory.getLogger(PetServiceImpl.class);

  private final PetRepository petRepository;

  public PetServiceImpl(PetRepository petRepository) {
    this.petRepository = petRepository;
  }

  @Override
  public Pet create(Pet pet) {

    log.debug("Creating pet");

    if (pet == null) {
      throw new IllegalArgumentException("Pet must not be null");
    }

    return petRepository.save(pet);
  }

  @Override
  public Optional<Pet> getById(long id) {
    log.debug("Getting pet by id = [{}]", id);
    return petRepository.findById(id);
  }

  @Override
  public List<Pet> list() {
    log.debug("Listing pets");
    return petRepository.findAll();
  }

  @Override
  public Pet update(Pet pet) {

    if (pet == null) {
      throw new IllegalArgumentException("Pet must not be null");
    }

    log.debug("Updating pet with id = [{}]", pet.id());

    if (!petRepository.existsById(pet.id())) {
      throw new PetNotFoundException(pet.id());
    }

    return petRepository.save(pet);
  }

  @Override
  public boolean delete(long id) {
    log.debug("Deleting pet id = [{}]", id);
    return petRepository.deleteById(id);
  }
}
