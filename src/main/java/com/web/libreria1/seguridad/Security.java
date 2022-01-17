package com.web.libreria1.seguridad;

import com.web.libreria1.entidades.Usuario;
import com.web.libreria1.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Security extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin().and()
                .authorizeRequests()
                           .antMatchers("/css/*", "/js/*", "/img/*")
                           .permitAll()
                .and(). formLogin()
                           .loginPage("/login")
                                     .loginProcessingUrl("/logincheck")
                                     .usernameParameter("mail")
                                     .passwordParameter("clave")
                                     .defaultSuccessUrl("/inicio")
                                     .permitAll()
                           .and().logout()
                                     .logoutUrl("/logout")
                                     .logoutSuccessUrl("/login")
                                     .permitAll().
                                      and().csrf().disable();
        

    }

}
