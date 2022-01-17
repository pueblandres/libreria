package com.web.libreria1.controladores;

import com.web.libreria1.entidades.Autor;
import com.web.libreria1.entidades.Editorial;
import com.web.libreria1.entidades.Libro;
import com.web.libreria1.entidades.Usuario;
import com.web.libreria1.enumeraciones.Rol;
import com.web.libreria1.errores.ErrorServicio;
import com.web.libreria1.servicios.AutorServicio;
import com.web.libreria1.servicios.EditorialServicio;
import com.web.libreria1.servicios.LibroServicio;
import com.web.libreria1.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_ADMIN')")
@RequestMapping("/")
public class LibroControlador {

    @Autowired
    private LibroServicio libroservicio;
    @Autowired
    private AutorServicio autorservicio;
    @Autowired
    private EditorialServicio editorialservicio;

    @Autowired
    private UsuarioServicio usuarioservicio;

    @GetMapping("/libros")
    public String lista(HttpSession session, ModelMap modelo) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login.getRol().equals("ADMIN")) {
            List<Libro> autoresLista = libroservicio.listaLibros();
            modelo.addAttribute("libros", autoresLista);

            return "libros";
        } else {
            List<Libro> autoresLista = libroservicio.listaLibrosActivos();
            modelo.addAttribute("libros", autoresLista);

            return "librosReserva";

        }
    }

    @GetMapping("libros/nuevolibro")
    public String libros(ModelMap modelo) {

        List<Editorial> editorialesLista = editorialservicio.listaEditorialesActivos();
        modelo.addAttribute("editoriales", editorialesLista);

        List<Autor> autoresLista = autorservicio.listaAutoresActivos();
        modelo.addAttribute("autores", autoresLista);
        return "nuevolibro";
    }

    @PostMapping("libros/nuevolibro")
    public String guardar(ModelMap modelo,  MultipartFile archivo1, @RequestParam String isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam String idAutor, @RequestParam String idEditorial, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados) {

        try {
            libroservicio.NuevoLibro(archivo1, isbn, titulo, anio, idAutor, idEditorial, ejemplares, ejemplaresPrestados);
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("anio", anio);
            modelo.put("ejemplares", ejemplares);
            modelo.put("ejemplaresPrestados", ejemplaresPrestados);
            modelo.put("exito", "Libro guardado con exito");
            return "redirect:/libros/nuevolibro";

        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "nuevolibro";
        }

    }

    @GetMapping("libros/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {

        List<Editorial> editorialesLista = editorialservicio.listaEditorialesActivos();
        modelo.addAttribute("editoriales", editorialesLista);

        List<Autor> autoresLista = autorservicio.listaAutoresActivos();
        modelo.addAttribute("autores", autoresLista);

        Libro libro = libroservicio.buscarPorId(id);

        modelo.addAttribute("libro", libro);

        return "libro_modificar";

    }

    @PostMapping("libros/modificar")
    public String modificar(MultipartFile archivo,@RequestParam String id, ModelMap modelo, @RequestParam String isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam String idAutor,  @RequestParam String idEditorial,  @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados) {

        Libro libro = null;
        try {

            libro = libroservicio.buscarPorId(id);
            libroservicio.ModificaLibro(archivo, id, isbn, titulo, anio, idAutor, idEditorial, ejemplares, ejemplaresPrestados);

            modelo.put("exito", "Libro guardado con exito");
            modelo.addAttribute("libro", libro);
            return "redirect:/libros";

        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "libro_modificar";
        }

    }

    @GetMapping("libros/baja/{id}")
    public String baja(@PathVariable String id, ModelMap modelo
    ) {

        modelo.put("libro", libroservicio.buscarPorId(id));
        try {
            libroservicio.bajaLibro(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/libros";
    }

    @GetMapping("libros/alta/{id}")
    public String alta(@PathVariable String id, ModelMap modelo
    ) {

        modelo.put("libro", libroservicio.buscarPorId(id));
        try {
            libroservicio.altaLibro(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/libros";
    }

    @GetMapping("/libros/infoLibro/{id}")
    public String info(ModelMap modelo,
            @PathVariable String id
    ) {
        Libro libro = libroservicio.buscarPorId(id);
        modelo.put("libro", libro);

        Autor autor = libro.getAutor();
        modelo.put("autor", autor);

        Editorial editorial = libro.getEditorial();
        modelo.put("editorial", editorial);

        return "infoLibro";
    }

    @GetMapping("/nuevoAutor")
    public String formulario() {
        return "libroAutor";
    }

    @PostMapping("libros/nuevolibro/nuevoAutor")
    public String guardar(ModelMap modelo,
            @RequestParam String nombre
    ) {

        try {
            autorservicio.NuevoAutor(nombre);

            return "redirect:";
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "libroAutor";
        }

    }

    @GetMapping("/nuevaEditorial")
    public String formulario2() {
        return "libroEditorial";
    }

    @PostMapping("libros/nuevolibro/nuevaEditorial")
    public String guardar2(ModelMap modelo,
            @RequestParam String nombre
    ) {

        try {
            editorialservicio.NuevaEditorial(nombre);

            return "redirect:";
        } catch (ErrorServicio e) {
            modelo.put("error", "Falto algun dato");
            return "libroEditorial";
        }

    }

}
