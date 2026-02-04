package ia.mdotm.repository;

import ia.mdotm.model.Pet;
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

        if (pet.id() == null) {
            long id = seq.incrementAndGet();
            pet = pet.withId(id);
        }

        store.put(pet.id(), pet);
        return pet;
    }

    @Override
    public Optional<Pet> findById(long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Pet> findAll() {

        List<Pet> all = new ArrayList<>(store.values());
        all.sort(Comparator.comparing(Pet::id));//Return always the same order
        return all;
    }

    @Override
    public boolean existsById(long id) {
        return store.containsKey(id);
    }

    @Override
    public boolean deleteById(long id) {
        Pet petRemoved = store.remove(id);
        return petRemoved != null;
    }
}

