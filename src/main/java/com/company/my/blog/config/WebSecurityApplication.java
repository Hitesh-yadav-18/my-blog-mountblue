package com.company.my.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityApplication extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SuccessHandler successHandler;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().ignoringAntMatchers("/post/postDelete/**","/deleteComment/**")
            .and()
            .authorizeRequests().antMatchers("/post/create").hasAnyAuthority("Admin", "Author")
            .antMatchers("/**","/css/**","/JS/**").permitAll()            
            .anyRequest().authenticated()
            .and()            
            .formLogin()
            .usernameParameter("email")
            .passwordParameter("password")
            .successHandler(successHandler)
            .loginPage("/login").permitAll()
            .and()
            .logout().invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/logout-success").permitAll();
    }
}
