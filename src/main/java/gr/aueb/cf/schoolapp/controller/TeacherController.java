package gr.aueb.cf.schoolapp.controller;


import gr.aueb.cf.schoolapp.dto.RegionReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeachersInsertDTO;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    @GetMapping("/insert")
    public String getTeacherPage(Model model) {
        model.addAttribute("teacherInsertDTO", TeachersInsertDTO.empty());
        //model.addAttribute(RegionReadOnlyDTO, regions());
        return "teacher-insert";
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