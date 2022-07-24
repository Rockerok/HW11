package ru.gb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import ru.gb.services.UserService;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/authenticated/**").authenticated()
                .antMatchers("/only_admins/**").hasAnyAuthority("ADMIN","SUPERADMIN")
                .antMatchers("/profile/**").authenticated()
                .and()
                .formLogin()
                .and()
                .logout().logoutSuccessUrl("/");

    }

//    @Bean
//    public UserDetailsService users(){
//        UserDetails user = User.builder()
//                .username("user")
//                .password ("{bcrypt}$2a$12$syyW0fkLI./uimpJxQznWumuLw1lCk8PEIZBFabblFhH0skiUcxE.")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password ("{bcrypt}$2a$12$syyW0fkLI./uimpJxQznWumuLw1lCk8PEIZBFabblFhH0skiUcxE.")
//                .roles("ADMIN","USER")
//                .build();
//        return new InMemoryUserDetailsManager(user,admin);
//    }

//    @Bean
//    public JdbcUserDetailsManager users(DataSource dataSource) {
//        UserDetails user = User.builder()
//                .username("user")
//                .password ("{bcrypt}$2a$12$syyW0fkLI./uimpJxQznWumuLw1lCk8PEIZBFabblFhH0skiUcxE.")
//                .authorities ("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password ("{bcrypt}$2a$12$syyW0fkLI./uimpJxQznWumuLw1lCk8PEIZBFabblFhH0skiUcxE.")
//                .authorities ("ADMIN")
//                .build();
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        if (jdbcUserDetailsManager.userExists(user.getUsername())){
//            jdbcUserDetailsManager.deleteUser(user.getUsername());
//        }
//        if (jdbcUserDetailsManager.userExists(admin.getUsername())) {
//            jdbcUserDetailsManager.deleteUser(admin.getUsername());
//        }
//        jdbcUserDetailsManager.createUser(user);
//        jdbcUserDetailsManager.createUser(admin);
//        return jdbcUserDetailsManager;
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }
}
