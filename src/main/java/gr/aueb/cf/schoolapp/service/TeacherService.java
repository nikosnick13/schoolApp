package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.TeacherEditDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.model.static_data.Region;
import gr.aueb.cf.schoolapp.repository.RegionRepository;
import gr.aueb.cf.schoolapp.repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;


@Service                    //IoC container
@RequiredArgsConstructor    //DI
@Slf4j                      //Logger
public class TeacherService implements ITeacherService{

    private final TeacherRepository teacherRepository;
    private final RegionRepository  regionRepository;
    private final Mapper mapper;

//    public TeacherService(TeacherRepository teacherRepository, RegionRepository regionRepository,Mapper mapper) {
//        this.teacherRepository = teacherRepository;
//        this.regionRepository = regionRepository;
//        this.mapper = mapper;
//    }


    @Override
    @Transactional(rollbackFor = { EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO dto) throws EntityAlreadyExistsException, EntityInvalidArgumentException {

        try {
            if (dto.vat() != null && teacherRepository.findByVat(dto.vat()).isPresent()) {
                throw new EntityAlreadyExistsException( "Teacher with vat " + dto.vat() + " already exists" );
            }
            Region region = regionRepository.findById(dto.regionId()).orElseThrow(
                    ()-> new EntityInvalidArgumentException("Region id= " + dto.regionId()+ "is invalid" ));

           Teacher teacher =  mapper.mapToTeacherDTOtoEntity(dto);
           region.addTeacher(teacher);

           teacherRepository.save(teacher);
           log.info("Teacher with vat={} saved successfully", dto.vat());

           return mapper.mapToTeacherReadOnlyDTO(teacher);

        }
        catch (EntityAlreadyExistsException ex){
            log.error("Save failed with teacher with vat={}.Teacher already exist", dto.vat());
            throw ex;
        }
        catch (EntityInvalidArgumentException ex){
            log.error("Save failed with teacher with vat={}. Region id={}",dto.vat(),dto.regionId());
            throw ex;
        }

    }
    @Override
    public boolean isTeacherExists(String vat){
        return teacherRepository.findByVat(vat).isPresent();
    }

    @Override
    @Transactional(readOnly = true) //MOno gia read
    public Page<TeacherReadOnlyDTO> getPaginatedTeachers(Pageable pageable) {

        Page<Teacher> teachersPage = teacherRepository.findAll(pageable);
        log.debug("Get paginated not deleted returned successfully page={} and size={}", teachersPage.getNumber(), teachersPage.getSize());
        return  teachersPage.map(mapper::mapToTeacherReadOnlyDTO) ;
    }


    @Override
    @Transactional(readOnly = true)
    public TeacherEditDTO getTeacherByUUID(UUID uuid) throws EntityNotFoundException {

        try{
            Teacher teacher = teacherRepository.findByUuid(uuid)
                    .orElseThrow( ()-> new EntityNotFoundException("Teacher with uuid = " + uuid + "not exist"));
            log.debug("Get teacher by uuid = {} return successfully", uuid);
            return mapper.mapToTeacherEditDTO(teacher);

        }
        catch (EntityNotFoundException ex){
            log.error("Get teacher by uuid:{} failed",uuid);
            throw ex;
        }
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class,EntityInvalidArgumentException.class, EntityAlreadyExistsException.class})
    public TeacherReadOnlyDTO updateTeacher(TeacherEditDTO teacherEditDTO) throws EntityInvalidArgumentException,
            EntityAlreadyExistsException,EntityNotFoundException {

        try {
            Teacher teacher = teacherRepository.findByUuid(teacherEditDTO.uuid())
                            .orElseThrow(() -> new EntityNotFoundException("Teacher with Uuid: "+ teacherEditDTO.uuid()+ " not found"));

            teacher.setFirstname(teacherEditDTO.firstname());
            teacher.setLastname(teacherEditDTO.lastname());
            if(!teacher.getVat().equals(teacherEditDTO.vat())){
                if(teacherRepository.findByVat(teacherEditDTO.vat()).isPresent()){
                    throw new EntityAlreadyExistsException("Teacher with vat " +teacherEditDTO.vat()+ " is already exist");
                }
                teacher.setVat(teacherEditDTO.vat());
            }

            if(!Objects.equals(teacherEditDTO.regionId(),teacher.getRegion().getId())){
                Region region =  regionRepository.findById(teacherEditDTO.regionId())
                        .orElseThrow(() -> new EntityInvalidArgumentException("Region id: " + teacherEditDTO.regionId() + "invalid"));
                Region oldRegion = teacher.getRegion();
                if(oldRegion  != null) oldRegion.removeTeacher(teacher);
                region.addTeacher(teacher);
            }

            teacherRepository.save(teacher);
            log.info("Teacher with uuid= {} update successfully",teacher.getUuid());
            return mapper.mapToTeacherReadOnlyDTO(teacher);

        } catch (EntityAlreadyExistsException ex){
            log.error("Teacher with uuid = {} and vat = {} is already exist",teacherEditDTO.uuid(),teacherEditDTO.vat());
            throw ex;
        }catch (EntityInvalidArgumentException ex){
            log.error("Update failed for teacher with uuid: {}. Region id={} is not valid",teacherEditDTO.uuid(),teacherEditDTO.regionId());
            throw ex;
        }catch (EntityNotFoundException ex){
            log.error("Update failed for teacher with uuid: {}.Teacher not found", teacherEditDTO.uuid());
            throw ex;
        }
    }
}
