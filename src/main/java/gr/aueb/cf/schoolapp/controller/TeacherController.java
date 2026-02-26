package gr.aueb.cf.schoolapp.controller;


import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.dto.RegionReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.service.IRegionService;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.validator.TeacherInsertValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final ITeacherService teacherService;
    private final IRegionService regionService;
    private final TeacherInsertValidator teacherInsertValidator;

    @GetMapping("/insert")
    public String getTeacherPage(Model model) {
        model.addAttribute("teacherInsertDTO", TeacherInsertDTO.empty());
        //model.addAttribute(RegionReadOnlyDTO, regions());
        return "teacher-insert";
    }

    @PostMapping("/insert")
    public String insertTeacher(@Valid @ModelAttribute("teacherInsertDTO")TeacherInsertDTO teacherInsertDTO,
                                 BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes){

        teacherInsertValidator.validate(teacherInsertDTO,bindingResult);

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

    @GetMapping({"", "/"})
    public String getPaginationTeachers(@PageableDefault(page = 0, size = 5, sort = "lastname") Pageable pageable, Model model ){
        Page<TeacherReadOnlyDTO> teachersPage = teacherService.getPaginatedTeachers(pageable);
        // Page<TeacherReadOnlyDTO> teachersPage = new PageImpl<>(
//                Stream.of(
//                        new TeacherReadOnlyDTO( "abc123", "Nikos","Nikolaidis","123","Athens"),
//                        new TeacherReadOnlyDTO( "abc123", "Gearge","Papapdopoylos","234","Athens"),
//                        new TeacherReadOnlyDTO( "abc123", "Theofilos","Kasimiw","2345","Athens"),
//                        new TeacherReadOnlyDTO( "abc123", "Panagiotis","Androytis","5623","Athens"),
//                        new TeacherReadOnlyDTO( "abc123", "Maria","Branaki","16453","Athens"),
//                        new TeacherReadOnlyDTO( "abc123", "Andriani","Ksanthopoulou ","65344","Athens"))
//                        .sorted(Comparator.comparing(TeacherReadOnlyDTO::lastname))
//                        .skip(pageable.getOffset())
//                        .limit(pageable.getPageSize())
//                        .toList(), pageable,6
//                );

        model.addAttribute("teachers", teachersPage.getContent());
        model.addAttribute("page", teachersPage);
        return "teachers";
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