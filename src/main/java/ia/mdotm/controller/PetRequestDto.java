package ia.mdotm.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PetRequestDto(@NotBlank String name,
                            @NotBlank String species,
                            @Min(0) Integer age,
                            String ownerName
) {
}