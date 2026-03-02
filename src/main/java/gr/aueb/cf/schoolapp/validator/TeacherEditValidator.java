package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.TeacherEditDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class TeacherEditValidator implements Validator {

    private final ITeacherService teacherService;

    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherEditDTO.class == clazz ;
    }

    @Override
    public void validate(Object target, Errors errors) {

        TeacherEditDTO teacherEditDTO = (TeacherEditDTO) target;

         try {
             TeacherEditDTO savedTeacher  = teacherService.getTeacherByUUID(teacherEditDTO.uuid());
             if( savedTeacher != null && !savedTeacher.vat().equals(teacherEditDTO.vat())){
                 if(teacherService.isTeacherExists(teacherEditDTO.vat())){
                     log.warn("Update failed. The Teacher with vat = {} already exist", teacherEditDTO.vat());
                     errors.rejectValue("vat",  "vat.teacher exist");
                 }
             }

         }catch (EntityNotFoundException ex){
             log.warn("Update failed. The teacher with uuid={} is already exist", teacherEditDTO.uuid());
             errors.rejectValue("uuid", "uuid.teacher.notfound");
         }


    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }
}
