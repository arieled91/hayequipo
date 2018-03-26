package org.arieled91.hayequipo.auth.repository;

import org.arieled91.hayequipo.auth.model.Privilege;
import org.arieled91.hayequipo.auth.model.Role;
import org.arieled91.hayequipo.auth.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.arieled91.hayequipo.auth.model.PrivilegeType.CHANGE_PASSWORD;
import static org.arieled91.hayequipo.auth.model.PrivilegeType.FULL_ACCESS;
import static org.arieled91.hayequipo.auth.model.PrivilegeType.GAME_PRIORITY;
import static org.arieled91.hayequipo.auth.model.PrivilegeType.READ;
import static org.arieled91.hayequipo.auth.model.PrivilegeType.WRITE;
import static org.arieled91.hayequipo.auth.model.RoleType.ROLE_ADMIN;
import static org.arieled91.hayequipo.auth.model.RoleType.ROLE_MODERATOR;
import static org.arieled91.hayequipo.auth.model.RoleType.ROLE_USER;

@Component
public class UserDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDataLoader(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final @NotNull ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound(READ.name());
        final Privilege writePrivilege = createPrivilegeIfNotFound(WRITE.name());
        final Privilege passwordPrivilege = createPrivilegeIfNotFound(CHANGE_PASSWORD.name());
        final Privilege fullAccessPrivilege = createPrivilegeIfNotFound(FULL_ACCESS.name());
        final Privilege gamePriorityPrivilege = createPrivilegeIfNotFound(GAME_PRIORITY.name());

        // == create initial roles
        //final List<Privilege> adminPrivileges = List.of(readPrivilege, writePrivilege, passwordPrivilege, fullAccessPrivilege);
        final Set<Privilege> fullPrivileges = new HashSet<>(Arrays.asList(fullAccessPrivilege, gamePriorityPrivilege));
        final Set<Privilege> userPrivileges = new HashSet<>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));

        final Role adminRole = createRoleIfNotFound(ROLE_ADMIN.name(), fullPrivileges);
        final Role userRole = createRoleIfNotFound(ROLE_USER.name(), userPrivileges);
        createRoleIfNotFound(ROLE_MODERATOR.name(), fullPrivileges);

        // == create initial user
        createUserIfNotFound("test@test.com", "Test", "Test", "test", new HashSet<>(Arrays.asList(userRole, adminRole)));

        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public Role createRoleIfNotFound(final String name, final Set<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            return roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    public void createUserIfNotFound(final String email, final String firstName, final String lastName, final String password, final Set<Role> roles) {
        if (userRepository.findByEmailAndEnabledIsTrue(email) == null) {
            final User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setEnabled(true);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

}