package com.web.libreria1.servicios;

import com.web.libreria1.entidades.Autor;
import com.web.libreria1.errores.ErrorServicio;
import com.web.libreria1.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;
    @Transactional
    public Autor NuevoAutor(String nombre) throws ErrorServicio {

        validar(nombre);
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(true);
        return autorRepositorio.save(autor);

    }
     @Transactional
    public Autor ModificarAutor(String id, String nombre) throws ErrorServicio {

        validar(nombre);
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
        
           return autorRepositorio.save(autor);

        } else {
            throw new ErrorServicio("El Autor no existe");

        }
    }
     @Transactional
    public void BajaAutor(String id) throws ErrorServicio {

        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(false);
            autorRepositorio.save(autor);

        } else {
            throw new ErrorServicio("El Autor no existe");

        }

    }
    
      @Transactional
    public void AltaAutor(String id) throws ErrorServicio {

        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(true);
            autorRepositorio.save(autor);

        } else {
            throw new ErrorServicio("El Autor no existe");

        }

    }
    
    @Transactional
    public List<Autor> listaAutores(){
   
        return autorRepositorio.findAll();
    }
     @Transactional
    public List<Autor> listaAutoresActivos(){
   
        return autorRepositorio.autoresActivos(true);
    }

    public void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre de autor no puede ser nulo");
        }

    }
      @Transactional
    public Autor  buscarPorId(String id) {

        return autorRepositorio.getById(id);
    }

        @Transactional
    public Autor  buscarPorIdActivos(String id) {

        return autorRepositorio.autoresActivosPorId(true, id);
    }
}
