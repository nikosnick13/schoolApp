package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.dto.UserInsertDTO;
import gr.aueb.cf.schoolapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Role;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.repository.RoleRepository;
import gr.aueb.cf.schoolapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final Mapper mapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(rollbackFor = { EntityAlreadyExistsException.class, EntityInvalidArgumentException.class })
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO) throws EntityInvalidArgumentException, EntityAlreadyExistsException {

        try {

            if(userRepository.findByUsername(userInsertDTO.username()).isPresent()){
                throw new EntityAlreadyExistsException("User with username" + userInsertDTO.username() + "already exists");
            }

            User user = mapper.mapToUserEntity(userInsertDTO);
            user.setPassword(passwordEncoder.encode(userInsertDTO.password()));
            Role role = roleRepository.findById(userInsertDTO.roleId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Role id=" + userInsertDTO.roleId() + " invalid"));
            role.addUser(user);

            log.info("Save succeed user with username: {}",userInsertDTO.username());
            return mapper.mapToUserReadOnlyDTO(user);

        }catch (EntityAlreadyExistsException ex) {
            log.error("Save failed. User with username={} already exists", userInsertDTO.username());
            throw ex;
        } catch (EntityInvalidArgumentException ex){
            log.error("Save failed. Invalid arguments for user with username={}", userInsertDTO.username());
            throw ex;
        }

    }
}
