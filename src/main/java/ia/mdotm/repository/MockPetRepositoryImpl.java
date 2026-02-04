package ia.mdotm.repository;

import ia.mdotm.model.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MockPetRepositoryImpl implements PetRepository {

  private static final Logger log = LoggerFactory.getLogger(MockPetRepositoryImpl.class);

  private final Map<Long, Pet> store;
  private final AtomicLong seq;

  @Autowired
  public MockPetRepositoryImpl() {
    this(
      new ConcurrentHashMap<>(),
      new AtomicLong(1)
    );
  }

  MockPetRepositoryImpl(Map<Long, Pet> store, AtomicLong seq) {
    this.store = store;
    this.seq = seq;
  }

  @Override
  public Pet save(Pet pet) {

    log.debug("Saving pet with incoming id = [{}]", pet.id());

    if (pet.id() == null) {
      long id = seq.getAndIncrement();
      pet = pet.withId(id);
      log.debug("Assigned new id = [{}] to pet", id);
    }

    store.put(pet.id(), pet);
    log.debug("Saved pet id = [{}]", pet.id());

    return pet;
  }

  @Override
  public Optional<Pet> findById(long id) {
    log.debug("Finding pet by id = [{}]", id);
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public List<Pet> findAll() {

    log.debug("Listing all pets");
    List<Pet> all = new ArrayList<>(store.values());
    log.debug("Found [{}] pets", all.size());

    all.sort(Comparator.comparing(Pet::id));//Return always the same order
    return all;
  }

  @Override
  public boolean existsById(long id) {
    log.debug("Checking if pet exists id = [{}]", id);
    return store.containsKey(id);
  }

  @Override
  public boolean deleteById(long id) {

    log.debug("Deleting pet id = [{}]", id);
    Pet petRemoved = store.remove(id);
    log.debug("Deleted pet id = [{}] result = [{}]", id, petRemoved != null);

    return petRemoved != null;
  }
}
