package com.web.libreria1.servicios;

import com.web.libreria1.entidades.Autor;
import com.web.libreria1.entidades.Editorial;
import com.web.libreria1.entidades.Foto;
import com.web.libreria1.entidades.Libro;
import com.web.libreria1.errores.ErrorServicio;
import com.web.libreria1.repositorios.AutorRepositorio;
import com.web.libreria1.repositorios.EditorialRepositorio;
import com.web.libreria1.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Autowired
    private FotoServicio fotoservicio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Libro NuevoLibro(MultipartFile archivo1, String isbn, String titulo, Integer anio, String idAutor, String idEditorial, Integer ejemplares, Integer ejemplaresPrestados) throws ErrorServicio {

        validar(isbn, titulo, anio, ejemplares, ejemplaresPrestados);
        Autor autor = autorRepositorio.getOne(idAutor);
        Editorial editorial = editorialRepositorio.getOne(idEditorial);
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(libro.getEjemplares() - libro.getEjemplaresPrestados());
        libro.setAlta(true);
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        Foto foto = fotoservicio.guardar(archivo1);
        libro.setFoto(foto);
    
        return libroRepositorio.save(libro);

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Libro ModificaLibro(MultipartFile archivo, String id, String isbn, String titulo, Integer anio, String idAutor, String idEditorial, Integer ejemplares, Integer ejemplaresPrestados) throws ErrorServicio {

        validar(isbn, titulo, anio, ejemplares, ejemplaresPrestados);

        if (idAutor == null || titulo.isEmpty()) {
            throw new ErrorServicio("El id de autor no puede ser nulo");
        }
        if (idEditorial == null || titulo.isEmpty()) {
            throw new ErrorServicio("El id de editorial no puede ser nulo");
        }

        Autor autor = autorRepositorio.getOne(idAutor);
        Editorial editorial = editorialRepositorio.getOne(idEditorial);

        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setEjemplaresRestantes(libro.getEjemplares() - libro.getEjemplaresPrestados());
            libro.setAlta(true);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            String idfoto = null;
            if (libro.getFoto() != null) {
                idfoto = libro.getFoto().getId();
            }
            Foto foto = fotoservicio.actualizar(archivo, idfoto);
            libro.setFoto(foto);

            return libroRepositorio.save(libro);

        } else {
            throw new ErrorServicio("El Libro no existe");

        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void bajaLibro(String id) throws ErrorServicio {

        Optional< Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(false);
            libroRepositorio.save(libro);

        } else {
            throw new ErrorServicio("El Libro no existe");

        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void altaLibro(String id) throws ErrorServicio {

        Optional< Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(true);
            libroRepositorio.save(libro);

        } else {
            throw new ErrorServicio("El Libro no existe");

        }

    }

    @Transactional(readOnly = true)
    public List<Libro> listaLibros() {

        return libroRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Libro> listaLibrosActivos() {

        return libroRepositorio.librosActivos(true);
    }

    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) {

        return libroRepositorio.getById(id);
    }

    @Transactional(readOnly = true)
    public List<Libro> listaLibrosPorAutor(String id) {

        return libroRepositorio.buscarPorAutor(id);
    }

    @Transactional(readOnly = true)
    public List<Libro> listaLibrosPorAutorActivos(String id) {

        return libroRepositorio.buscarPorAutorActivos(id, true);
    }

    @Transactional(readOnly = true)
    public List<Libro> listaLibrosPorEdutorial(String id) {

        return libroRepositorio.buscarPorEditorial(id);
    }

    @Transactional(readOnly = true)
    public List<Libro> listaLibrosPorEditorialActivo(String id) {

        return libroRepositorio.buscarPorEditorialActivos(id, true);
    }

    public void validar(String isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados) throws ErrorServicio {

        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("El nombre de Libro no puede ser nulo");
        }
        if (anio == null) {
            throw new ErrorServicio("El anio de Libro no puede ser nulo");
        }
        if (ejemplares == null || ejemplares == 0) {
            throw new ErrorServicio("La cantidad de ejemplares no puede ser nula ni cero");
        }
        if (ejemplaresPrestados == null) {
            throw new ErrorServicio("La cantidad de ejemplares no puede ser nula");
        }
        if (isbn == null || titulo.isEmpty()) {
            throw new ErrorServicio("El isbn de Libro no puede ser nulo");
        }
    }
}
