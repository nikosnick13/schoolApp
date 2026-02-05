package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TeacherInsertDTO(
        //BeanValidation
        @NotNull(message = "Το όνομα δεν πρέπει αν είναι κενό")
        @Size(min=2, message = "Το όνομα πρέπει να περιέχει τουλάχιστον 2 χαρακτίρες")
        String firstname,

        @NotNull(message = "Το επώνυμο δεν πρέπει αν είναι κενό")
        @Size(min=2, message = "Το επώνυμο πρέπει να περιέχει τουλάχιστον 2 χαρακτίρες")
        String lastname,


        @Pattern(regexp = "\\d{9,}", message = "Το ΑΦΜ δεν πρέπει να είναι μικρότερο απο 9 ψηφία" )
        String vat,

        @NotNull(message = "Η περιο δεν πρέπει να ειναι κενή")
        Long regionId

    ) {

    public static TeacherInsertDTO empty(){
        return new TeacherInsertDTO("","","",0L);
    }

}
