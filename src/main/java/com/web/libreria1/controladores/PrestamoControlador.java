package com.web.libreria1.controladores;

import com.web.libreria1.entidades.Libro;
import com.web.libreria1.entidades.Prestamo;
import com.web.libreria1.entidades.Usuario;
import com.web.libreria1.servicios.LibroServicio;
import com.web.libreria1.servicios.PrestamoServicio;
import java.util.List;
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


@RequestMapping("/")
public class PrestamoControlador {

    @Autowired
    private PrestamoServicio prestamoservicio;

    @Autowired
    private LibroServicio libroservicio;

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/libros/prestamo")
    public String guardar(ModelMap modelo, @RequestParam String idLibro, HttpSession session) {
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");

            Prestamo prestamo = prestamoservicio.nuevoPrestamo(login.getId(), idLibro);
            Libro libro = libroservicio.buscarPorId(idLibro);

            modelo.addAttribute("libro", libro);

            modelo.put("exito", "El prestamo se realizo con exito");

            modelo.addAttribute("prestamo", prestamo);

            return "prestamo";

        } catch (Exception e) {

            modelo.put("error", e.getMessage());

            return "prestamo";

        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/inicio/devolucion/{id}")
    public String DEVOLUCION(ModelMap modelo, @PathVariable String id) {

        List<Prestamo> prestamos = prestamoservicio.listaPrestamos(id);
        modelo.addAttribute("prestamo", prestamos);

        return "devolucion";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/prestamos")
    public String prestamos(ModelMap modelo) {

        List<Prestamo> prestamos = prestamoservicio.listaPrestamos();
        modelo.addAttribute("prestamo", prestamos);

        return "listaPrestamos";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("inicio/devolucion")
    public String DEVOLUCION(ModelMap modelo, @RequestParam String idPrestamo, HttpSession session) {
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");

            Prestamo prestamo = prestamoservicio.Devolucion(idPrestamo, login.getId());

            modelo.put("exito", "La devolucion se realizo con exito");

            modelo.addAttribute("prestamo", prestamo);

            return "exitoDevolucion";

        } catch (Exception e) {

            modelo.put("error", e.getMessage());

            return "exitoDevolucion";

        }

    }
}
