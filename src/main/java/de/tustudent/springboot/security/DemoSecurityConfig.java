package de.tustudent.springboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {

    // using database to check
    @Bean
    public UserDetailsManager manager(DataSource dataSource) {

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("select user_id, pw, active from members where user_id=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select user_id, role from roles where user_id=?");
        return jdbcUserDetailsManager;
    }

    // alternatively using InMemeoryUserDetailsManager

//    @Bean
//    public InMemoryUserDetailsManager mUserdetailsManager(){
//        UserDetails john = User.builder().username("john").password("{noop}test123").roles("EMPLOYEE").build();
//        UserDetails mary = User.builder().username("mary").password("{noop}test123").roles("EMPLOYEE", "MANAGER").build();
//        UserDetails susan = User.builder().username("susan").password("{noop}test123").roles("EMPLOYEE", "MANAGER", "ADMIN").build();
//        return new InMemoryUserDetailsManager(john, mary, susan);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/employees/**").hasRole("EMPLOYEE")
                                .requestMatchers("/leaders/**").hasRole("MANAGER")
                                .requestMatchers("/systems/**").hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()

                )
                .formLogin(
                        form ->
                                form
                                        .loginPage("/showMyLoginPage")
                                        .loginProcessingUrl("/authenticateTheUser")
                                        .permitAll()
                )
                .logout(logout -> {
                    logout.permitAll()
                            .logoutSuccessUrl("/");
                })
                .exceptionHandling(configurer -> {
                    configurer.accessDeniedPage("/access-denied");
                });
        return httpSecurity.build();
    }

}
