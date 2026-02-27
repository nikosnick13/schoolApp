package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record TeacherEditDTO(


        @NotNull
        UUID uuid,

        //BeanValidation
        // @NotNull(message = "Το όνομα δεν πρέπει αν είναι κενό")
        //@Size(min=2, message = "Το όνομα πρέπει να περιέχει τουλάχιστον 2 χαρακτίρες")
        @NotNull
        @Size
        String firstname,

        //@NotNull(message = "Το επώνυμο δεν πρέπει αν είναι κενό")
        //@Size(min=2, message = "Το επώνυμο πρέπει να περιέχει τουλάχιστον 2 χαρακτίρες")
        @NotNull
        @Size
        String lastname,

        //@Pattern(regexp = "\\d{9,}", message = "Το ΑΦΜ δεν πρέπει να είναι μικρότερο απο 9 ψηφία" )
        @Pattern(regexp = "\\d{9,}")
        String vat,

        //@NotNull(message = "Η περιοxή δεν πρέπει να ειναι κενή")
        @NotNull
        Long regionId


    ) {
}
