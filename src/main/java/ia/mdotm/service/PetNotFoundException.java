package ia.mdotm.service;

public class PetNotFoundException extends RuntimeException {

    public PetNotFoundException(long id) {
        super("Pet not found for id = [" + id + "]");
    }
}
