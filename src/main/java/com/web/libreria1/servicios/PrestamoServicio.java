package com.web.libreria1.servicios;

import com.web.libreria1.entidades.Libro;
import com.web.libreria1.entidades.Prestamo;
import com.web.libreria1.entidades.Usuario;
import com.web.libreria1.errores.ErrorServicio;
import com.web.libreria1.repositorios.LibroRepositorio;
import com.web.libreria1.repositorios.PrestamoRepositorio;
import com.web.libreria1.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamorepositorio;

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private UsuarioRepositorio usuariorepositorio;

    @Autowired
    private envioMailServicio envioMail;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Prestamo nuevoPrestamo(String idUsuario, String idLibro) throws ErrorServicio {

        if (idUsuario == null || idUsuario.isEmpty()) {
            throw new ErrorServicio("El id de usuario no puede ser nulo");
        }
        if (idLibro == null || idLibro.isEmpty()) {
            throw new ErrorServicio("El id de libro no puede ser nulo");
        }
        Libro libro = libroRepositorio.getById(idLibro);
        Usuario usuario = usuariorepositorio.getById(idUsuario);

        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
        libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() - 1);
        prestamo.setUsuario(usuario);
        prestamo.setFechaprestamo(new Date());
        prestamo.setAlta(true);

        prestamorepositorio.save(prestamo);
        try {
               envioMail.enviar("TU PRESTAMO SE REALIZO CON EXITO", "Libreria Martin", usuario.getMail());
        } catch (Exception e) {
        }
     

        return prestamo;

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Prestamo Devolucion(String idPrestamo, String idUsuario) throws ErrorServicio {

        if (idUsuario == null || idUsuario.isEmpty()) {
            throw new ErrorServicio("El id de usuario no puede ser nulo");
        }

        if (idPrestamo == null || idPrestamo.isEmpty()) {
            throw new ErrorServicio("El id del prestamo no puede ser nulo");
        }
        String idLibro = prestamorepositorio.idlibro(idPrestamo);

        Libro libro = libroRepositorio.getById(idLibro);
        Usuario usuario = usuariorepositorio.getById(idUsuario);

        Optional<Prestamo> respuesta = prestamorepositorio.findById(idPrestamo);
        if (respuesta.isPresent()) {

            Prestamo prestamo = respuesta.get();
            prestamo.setLibro(libro);
            libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() - 1);
            libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() + 1);
            prestamo.setUsuario(usuario);
            prestamo.setFechadevolucion(new Date());
            prestamo.setAlta(false);

            prestamorepositorio.save(prestamo);
            try {
                  envioMail.enviar("TU DEVOLUCION SE REALIZO CON EXITO", "Libreria Martin", usuario.getMail());
            } catch (Exception e) {
            }
   
            return prestamo;

        } else {
            throw new ErrorServicio("El Libro no existe");
        }

    }

    @Transactional(readOnly = true)
    public List<Prestamo> listaPrestamos(String id) {

        return prestamorepositorio.buscarPorUSUARIO(id,true);
    }
    
         @Transactional(readOnly = true)
    public List<Prestamo> listaPrestamos() {

        return prestamorepositorio.findAll();
    }

}
