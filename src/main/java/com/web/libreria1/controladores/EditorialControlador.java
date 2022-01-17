package com.web.libreria1.controladores;

import com.web.libreria1.entidades.Editorial;
import com.web.libreria1.entidades.Libro;
import com.web.libreria1.entidades.Usuario;
import com.web.libreria1.enumeraciones.Rol;
import com.web.libreria1.errores.ErrorServicio;
import com.web.libreria1.servicios.EditorialServicio;
import com.web.libreria1.servicios.LibroServicio;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_ADMIN')")
@RequestMapping("/")
public class EditorialControlador {

    @Autowired
    private EditorialServicio editorialrservicio;

    @Autowired
    private LibroServicio libroservicio;

    @GetMapping("/editoriales")
    public String lista(HttpSession session, ModelMap modelo) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login.getRol().equals("ADMIN")) {
            List<Editorial> editorialesLista = editorialrservicio.listaEditoriales();
            modelo.addAttribute("editoriales", editorialesLista);
            return "editoriales";
        } else {
            List<Editorial> editorialesLista = editorialrservicio.listaEditorialesActivos();
            modelo.addAttribute("editorial", editorialesLista);

            return "editorialesReserva";

        }
    }

    @GetMapping("editoriales/nuevaeditorial")
    public String formulario() {
        return "nuevaeditorial";
    }

    @PostMapping("editoriales/nuevaeditorial")
    public String guardar(ModelMap modelo, @RequestParam String nombre) {

        try {
            editorialrservicio.NuevaEditorial(nombre);

            modelo.put("exito", "Editorial guardada con exito");

            return "nuevaeditorial";
        } catch (ErrorServicio e) {
            modelo.put("error", "Falto algun dato");
            return "nuevaeditorial";
        }

    }

    @GetMapping("editoriales/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("editorial", editorialrservicio.buscarPorId(id));

        return "editorial_modificar";
    }

    @PostMapping("editoriales/modificar")
    public String modificar(@RequestParam String id, ModelMap modelo, @RequestParam String nombre) {
        Editorial editorial = null;
        try {
            editorial = editorialrservicio.buscarPorId(id);
            editorialrservicio.ModificarEditorial(id, nombre);
            modelo.put("exito", "Editorial modificado con exito");

            return "redirect:/editoriales";
        } catch (ErrorServicio e) {

            modelo.put("error", e.getMessage());

            return "/editorial_modificar";
        }
    }

    @GetMapping("editoriales/baja/{id}")
    public String baja(@PathVariable String id, ModelMap modelo) {

        modelo.put("editorial", editorialrservicio.buscarPorId(id));
        try {
            editorialrservicio.BajaEditorial(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/editoriales";
    }

    @GetMapping("editoriales/alta/{id}")
    public String alta(@PathVariable String id, ModelMap modelo) {

        modelo.put("editorial", editorialrservicio.buscarPorId(id));
        try {
            editorialrservicio.altaEditorial(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/editoriales";
    }

    @GetMapping("/editoriales/infoEditorial/{id}")
    public String info(HttpSession session, ModelMap modelo, @PathVariable String id) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login.getRol().equals(Rol.ADMIN)) {
            Editorial editorial = editorialrservicio.buscarPorId(id);
            modelo.addAttribute("editorial", editorial);

            List<Libro> libros = libroservicio.listaLibrosPorEdutorial(id);
            modelo.addAttribute("libros", libros);

            modelo.addAttribute("usuario1", login);

            return "infoEditorial";

        } else {
            Editorial editorial = editorialrservicio.buscarPorIdActivos(id);
            modelo.addAttribute("editorial", editorial);

            List<Libro> libros = libroservicio.listaLibrosPorEditorialActivo(id);
            modelo.addAttribute("libros", libros);

            return "infoEditorial";
        }

    }
}
