package urjc.ovback.security;

import urjc.ovback.entity.User;
import urjc.ovback.repository.UserRepository;
import urjc.ovback.service.UserService;
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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements ApplicationRunner {
    public SecurityConfiguration(UserRepository userRepository, UserService userService, Encoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userService = userService;
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }
    private final UserService userService;

    private final Encoder encoder;
    private final UserRepository userRepository;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
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

    //Sample users, overwrite with your own
    @Override
    public void run(ApplicationArguments args) {
        User u = new User();
        User u1 = new User();
        u1.setUserName("admin");
        u1.setId(3);
        u1.setPassword(encoder.passwordEncoder().encode("admin"));
        u.setUserName("user");
        u.setPassword(encoder.passwordEncoder().encode("user"));
        u.setId(4);
        userRepository.save(u);
        userRepository.save(u1);
    }
}
