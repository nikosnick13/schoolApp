package gr.aueb.cf.schoolapp.controller;


import gr.aueb.cf.schoolapp.dto.RegionReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import jakarta.validation.Valid;
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
@RequestMapping("/teachers")
public class TeacherController {

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

        TeacherReadOnlyDTO teacherReadOnlyDTO = new TeacherReadOnlyDTO("acf-100","Nικόλαος","Νικολαίδης");

        redirectAttributes.addFlashAttribute("teacherReadOnlyDTO",teacherReadOnlyDTO);

        return  "redirect:/teachers/success";

    }

    @GetMapping("/success")
    public String teacherSuccess(Model model){
        return "teacher-success";
    }

    @ModelAttribute("regionsReadOnlyDTO")  //Εκτελίτε μετά απο κάθε request handler
    public List<RegionReadOnlyDTO> regions(){
        return List.of(
                new RegionReadOnlyDTO(1L,"Αθήνα"),
                new RegionReadOnlyDTO(2L,"Θεσσαλονίκη"),
                new RegionReadOnlyDTO(3L,"Πάτρα"),
                new RegionReadOnlyDTO(4L,"Ηράκλιο")
        );
    }
}