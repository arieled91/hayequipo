package arieled91.hayequipo.auth.service;

import arieled91.hayequipo.auth.model.Privilege;
import arieled91.hayequipo.auth.model.Role;
import arieled91.hayequipo.auth.model.User;
import arieled91.hayequipo.auth.repository.UserRepository;
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
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @Autowired
//    private final HttpServletRequest request;

//    @Autowired
//    private LoginAttemptService loginAttemptService;

//    @Autowired
//    public UserDetailsServiceImpl(UserRepository userRepository, UserService service, MessageSource messages, RoleRepository roleRepository) {
//        this.userRepository = userRepository;
//        this.service = service;
//        this.messages = messages;
//        this.roleRepository = roleRepository;
//    }


    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
//        final String ip = getClientIP();
//        if (loginAttemptService.isBlocked(ip)) {
//            throw new RuntimeException("blocked");
//        }

        final User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("No user found with username: " + email);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, getAuthorities(user.getRoles()));
    }

    // UTIL

    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<>();
        final List<Privilege> collection = new ArrayList<>();
        for (final Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
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