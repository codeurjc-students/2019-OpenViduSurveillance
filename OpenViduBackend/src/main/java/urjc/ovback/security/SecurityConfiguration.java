package urjc.ovback.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import urjc.ovback.entity.User;
import urjc.ovback.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements ApplicationRunner, UserDetailsService {
    public SecurityConfiguration(UserRepository userRepository, Encoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    private final Encoder encoder;
    private final UserRepository userRepository;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this);
        authProvider.setPasswordEncoder(encoder.passwordEncoder());
        return authProvider;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider());
    }

    @Override
    protected void configure(HttpSecurity http)
            throws Exception {
        http.cors();
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers().permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    //Sample users, OVERWRITE WITH YOUR OWN
    @Override
    public void run(ApplicationArguments args) {
        User u = new User();
        User u1 = new User();
        u1.setUserName("admin2");
        u1.setId(5);
        u1.setPassword(encoder.passwordEncoder().encode("admin2"));
        u.setUserName("user2");
        u.setPassword(encoder.passwordEncoder().encode("user2"));
        u.setId(6);
        userRepository.save(u);
        userRepository.save(u1);
    }

    public User getUser(String username){
        return userRepository.findByUserName(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User u = getUser(s);
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        return new org.springframework.security.core.userdetails.User(u.getUserName(),u.getPassword(),roles);
    }
}
