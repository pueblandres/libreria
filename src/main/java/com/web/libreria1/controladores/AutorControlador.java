package com.web.libreria1.controladores;

import com.web.libreria1.entidades.Autor;
import com.web.libreria1.entidades.Libro;
import com.web.libreria1.entidades.Usuario;
import com.web.libreria1.enumeraciones.Rol;
import com.web.libreria1.errores.ErrorServicio;
import com.web.libreria1.servicios.AutorServicio;
import com.web.libreria1.servicios.LibroServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_ADMIN')")
@RequestMapping("/")
public class AutorControlador {

    @Autowired
    private AutorServicio autorservicio;

    @Autowired
    private LibroServicio libroservicio;

    @GetMapping("/autores")
    public String lista(HttpSession session, ModelMap modelo) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login.getRol().equals("ADMIN")) {
            List<Autor> autoresLista = autorservicio.listaAutores();
            modelo.addAttribute("autores", autoresLista);
            return "autores";
        } else {
            List<Autor> autoresLista = autorservicio.listaAutoresActivos();
            modelo.addAttribute("autor", autoresLista);

            return "autoresReserva";

        }
    }

    @GetMapping("autores/nuevoautor")
    public String formulario() {
        return "nuevoautor";
    }

    @PostMapping("autores/nuevoautor")
    public String guardar(ModelMap modelo, @RequestParam String nombre) {

        try {
            autorservicio.NuevoAutor(nombre);

            modelo.put("exito", "Autor guardado con exito");

            return "nuevoautor";
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "nuevoautor";
        }

    }

    @GetMapping("autores/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("autor", autorservicio.buscarPorId(id));

        return "autor_modificar";
    }

    @PostMapping("autores/modificar")
    public String modificar(@RequestParam String id, ModelMap modelo, @RequestParam String nombre) {
        Autor autor = null;
        try {
            autor = autorservicio.buscarPorId(id);
            autorservicio.ModificarAutor(id, nombre);
            modelo.put("exito", "Autor modificado con exito");

            return "redirect:/autores";
        } catch (ErrorServicio e) {

            modelo.put("error", e.getMessage());

            return "/autor_modificar";
        }
    }

    @GetMapping("autores/baja/{id}")
    public String baja(@PathVariable String id, ModelMap modelo) {

        modelo.put("autor", autorservicio.buscarPorId(id));
        try {
            autorservicio.BajaAutor(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/autores";
    }

    @GetMapping("autores/alta/{id}")
    public String alta(@PathVariable String id, ModelMap modelo) {

        modelo.put("autor", autorservicio.buscarPorId(id));
        try {
            autorservicio.AltaAutor(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/autores";
    }

    @GetMapping("/autores/infoAutor/{id}")
    public String info(HttpSession session, ModelMap modelo, @PathVariable String id) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login.getRol().equals(Rol.ADMIN)) {
            Autor autor = autorservicio.buscarPorId(id);
            modelo.addAttribute("autor", autor);

            List<Libro> libros = libroservicio.listaLibrosPorAutor(id);
            modelo.addAttribute("libros", libros);
            
             modelo.addAttribute("usuario1", login);
             
            return "infoAutor";

        } else {
            Autor autor = autorservicio.buscarPorIdActivos(id);
            modelo.addAttribute("autor", autor);
          
            List<Libro> libros = libroservicio.listaLibrosPorAutorActivos(id);
            modelo.addAttribute("libros", libros);
            return "infoAutor";

        }
         
    }
}
