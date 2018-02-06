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
import java.util.Set;

import static org.arieled91.hayequipo.auth.model.PrivilegeType.*;
import static org.arieled91.hayequipo.auth.model.RoleType.*;

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
    public void onApplicationEvent(@NotNull final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound(READ_PRIVILEGE.name());
        final Privilege writePrivilege = createPrivilegeIfNotFound(WRITE_PRIVILEGE.name());
        final Privilege passwordPrivilege = createPrivilegeIfNotFound(CHANGE_PASSWORD_PRIVILEGE.name());
        final Privilege fullAccessPrivilege = createPrivilegeIfNotFound(FULL_ACCESS_PRIVILEGE.name());

        // == create initial roles
        //final List<Privilege> adminPrivileges = List.of(readPrivilege, writePrivilege, passwordPrivilege, fullAccessPrivilege);
        final Set<Privilege> fullAccessPrivileges = Set.of(fullAccessPrivilege);
        final Set<Privilege> userPrivileges = Set.of(readPrivilege, writePrivilege, passwordPrivilege);

        final Role adminRole = createRoleIfNotFound(ROLE_ADMIN.name(), fullAccessPrivileges);
        Role userRole = createRoleIfNotFound(ROLE_USER.name(), userPrivileges);
        createRoleIfNotFound(ROLE_MODERATOR.name(), fullAccessPrivileges);

        // == create initial user
        createUserIfNotFound("test@test.com", "Test", "Test", "test", Set.of(userRole, adminRole));

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
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    public void createUserIfNotFound(final String email, final String firstName, final String lastName, final String password, final Set<Role> roles) {
        User user = userRepository.findByEmailAndEnabledIsTrue(email);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setEnabled(true);
        }
        user.setRoles(roles);
        userRepository.save(user);
    }

}