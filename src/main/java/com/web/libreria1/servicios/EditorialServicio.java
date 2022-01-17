package com.web.libreria1.servicios;

import com.web.libreria1.entidades.Editorial;
import com.web.libreria1.errores.ErrorServicio;
import com.web.libreria1.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;
     @Transactional
    public Editorial NuevaEditorial(String nombre) throws ErrorServicio {

        validar(nombre);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(true);
        return editorialRepositorio.save(editorial);

    }
     @Transactional
    public Editorial ModificarEditorial(String id, String nombre) throws ErrorServicio {

        validar(nombre);
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
         
           return editorialRepositorio.save(editorial);

        } else {
            throw new ErrorServicio("La editorial no existe");

        }
    }
     @Transactional
    public void BajaEditorial(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(false);
            editorialRepositorio.save(editorial);

        } else {
            throw new ErrorServicio("La editorial no existe");

        }

    }
    
       @Transactional
    public void altaEditorial(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(true);
            editorialRepositorio.save(editorial);

        } else {
            throw new ErrorServicio("La editorial no existe");

        }

    }
    
    
      @Transactional
    public List<Editorial> listaEditoriales(){
   
        return editorialRepositorio.findAll();
    }
      @Transactional
    public List<Editorial> listaEditorialesActivos(){
   
        return editorialRepositorio.editorialesActivas(true);
    }
     @Transactional
    public Editorial  buscarPorId(String id) {

        return editorialRepositorio.getById(id);
    }
    
       @Transactional
    public Editorial  buscarPorIdActivos(String id) {

        return editorialRepositorio.editorialesActivasPorId(true, id);
    }

    public void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre de la editorial no puede ser nulo");
        }

    }
}
