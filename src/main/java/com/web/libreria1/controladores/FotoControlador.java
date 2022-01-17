package com.web.libreria1.controladores;

import com.web.libreria1.entidades.Libro;
import com.web.libreria1.entidades.Usuario;
import com.web.libreria1.errores.ErrorServicio;
import com.web.libreria1.servicios.LibroServicio;
import com.web.libreria1.servicios.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/foto")
public class FotoControlador {

    @Autowired
    private LibroServicio libroservicio;

    @Autowired
    private UsuarioServicio usuarioservicio;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) {
        try {
            Usuario usuario = usuarioservicio.buscarPorId(id);
            if (usuario.getFoto() == null) {
                throw new ErrorServicio("el usuario no tiene una foto asignada");
            }
            byte[] foto = usuario.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoControlador.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    
      @GetMapping("/libro/{id}")
    public ResponseEntity<byte[]> fotoLibro(@PathVariable String id) {
        try {
           Libro libro = libroservicio.buscarPorId(id);
            if (libro.getFoto() == null) {
                throw new ErrorServicio("el libro no tiene una foto asignada");
            }
            byte[] foto = libro.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoControlador.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
