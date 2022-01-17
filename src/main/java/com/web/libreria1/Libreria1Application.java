package com.web.libreria1;

import com.web.libreria1.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Libreria1Application {

    @Autowired
    private UsuarioServicio usuarioservicio;

    public static void main(String[] args) {
        SpringApplication.run(Libreria1Application.class, args);
       
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioservicio).passwordEncoder(new BCryptPasswordEncoder());

    }


}
