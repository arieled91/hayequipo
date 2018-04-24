package org.arieled91.hayequipo.auth.service;

import org.arieled91.hayequipo.auth.model.Privilege;
import org.arieled91.hayequipo.auth.model.Role;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        final User user = userRepository.findByEmailAndEnabledIsTrue(email);
        if (user == null) throw new UsernameNotFoundException("No user found with username: " + email);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, getAuthorities(user.getRoles()));
    }


    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(final Collection<Role> roles) {
        final List<Privilege> collection = new ArrayList<>();
        for (final Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        final List<String> privileges = new ArrayList<>();
        for (final Privilege item : collection) {
            privileges.add(item.getName());
        }

        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

//    private String getClientIP() {
//        final String xfHeader = request.getHeader("X-Forwarded-For");
//        if (xfHeader == null) {
//            return request.getRemoteAddr();
//        }
//        return xfHeader.split(",")[0];
//    }
}