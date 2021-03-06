package com.roman_musijowski.pgs_lessons.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public SpringSecConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
                                                               UserDetailsService userDetailsService){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Autowired
    public void configureAuthManager(AuthenticationManagerBuilder authenticationManagerBuilder){
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().ignoringAntMatchers("/h2-console").disable()
                .authorizeRequests().antMatchers("/static/css") .permitAll()
                .and().formLogin().loginPage("/login").permitAll()
                .and().authorizeRequests().antMatchers("/").authenticated()
                .and().authorizeRequests().antMatchers("/user/edit").hasAnyAuthority("STUDENT")
                .and().authorizeRequests().antMatchers("/lesson/indexStudent").hasAnyAuthority("STUDENT")
                .and().authorizeRequests().antMatchers("/user/list").hasAnyAuthority("ADMIN")
                .and().authorizeRequests().antMatchers("/user/show/{id}").hasAnyAuthority("ADMIN")
                .and().authorizeRequests().antMatchers("/user/indexAdmin").hasAnyAuthority("ADMIN")
                .and().authorizeRequests().antMatchers("/lesson/show/{id}").hasAnyAuthority("ADMIN")
                .and().authorizeRequests().antMatchers("/lesson/list").hasAnyAuthority("ADMIN")
                .and().authorizeRequests().antMatchers("/lesson/lessonForm").hasAnyAuthority("ADMIN")
                .and().exceptionHandling().accessDeniedPage("/access_denied");
    }
}
