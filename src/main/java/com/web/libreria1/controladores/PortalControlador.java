package com.web.libreria1.controladores;

import com.web.libreria1.entidades.Usuario;
import com.web.libreria1.enumeraciones.Rol;
import com.web.libreria1.servicios.UsuarioServicio;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioservicio;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o clave incorrectos");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente.");
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo, HttpSession session) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login.getRol().equalsIgnoreCase("ADMIN")) {
            modelo.addAttribute("usuario1", login);
        }

        return "inicio";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registrarse")
    public String registro(ModelMap modelo, MultipartFile archivo, @RequestParam String dni, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String telefono, @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2) {
        try {

           Usuario usuario= usuarioservicio.Registrar(archivo, dni, nombre, apellido, telefono, mail, clave, clave2);
         
           
            
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "registro";
        }
        return "/login";
    }

    @GetMapping("/validar")
    public String validar() {
        return "/login";
    }

    @PostMapping("/validar")
    public String validarMail(@RequestParam String mail,@RequestParam String validar,@RequestParam String num) {

        return "inicio";
    }

}
