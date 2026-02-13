package gr.aueb.cf.schoolapp.controller;


import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFountException;
import gr.aueb.cf.schoolapp.dto.RegionReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.service.IRegionService;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final ITeacherService teacherService;
    private  final IRegionService regionService;

    @GetMapping("/insert")
    public String getTeacherPage(Model model) {
        model.addAttribute("teacherInsertDTO", TeacherInsertDTO.empty());
        //model.addAttribute(RegionReadOnlyDTO, regions());
        return "teacher-insert";
    }

    @PostMapping("/insert")
    public String insertTeacher(@Valid @ModelAttribute("teacherInsertDTO")TeacherInsertDTO teacherInsertDTO,
                                 BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            //model.addAttribute("teacherInsertDTO",regions());
            return "teacher-insert";
        }

       // TeacherReadOnlyDTO teacherReadOnlyDTO = new TeacherReadOnlyDTO("acf-100","Nικόλαος","Νικολαίδης");

        try{

            TeacherReadOnlyDTO teacherReadOnlyDTO = teacherService.saveTeacher(teacherInsertDTO);
            redirectAttributes.addFlashAttribute("teacherReadOnlyDTO",teacherReadOnlyDTO);

        }catch (EntityInvalidArgumentException | EntityAlreadyExistsException e){

            model.addAttribute("errorMessage", e.getMessage());
            return "teacher-insert";

        }

        return  "redirect:/teachers/success";

    }

    @GetMapping("/success")
    public String teacherSuccess(Model model){
        return "teacher-success";
    }

    @ModelAttribute("regionsReadOnlyDTO")  //Εκτελίτε μετά απο κάθε request handler
    public List<RegionReadOnlyDTO> regions(){

       return regionService.findAllRegionByName();
//        return List.of(
//                new RegionReadOnlyDTO(1L,"Αθήνα"),
//                new RegionReadOnlyDTO(2L,"Θεσσαλονίκη"),
//                new RegionReadOnlyDTO(3L,"Πάτρα"),
//                new RegionReadOnlyDTO(4L,"Ηράκλιο")
//        );
    }
}