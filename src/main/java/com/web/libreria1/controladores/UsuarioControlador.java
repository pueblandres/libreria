package com.web.libreria1.controladores;

import com.web.libreria1.entidades.Foto;
import com.web.libreria1.entidades.Usuario;
import com.web.libreria1.servicios.FotoServicio;
import com.web.libreria1.servicios.UsuarioServicio;
import com.web.libreria1.servicios.envioMailServicio;
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
import org.springframework.web.multipart.MultipartFile;

@Controller

@RequestMapping("/")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioservicio;

    @Autowired
    private FotoServicio fotoservicio;

    @Autowired
    private envioMailServicio envioMail;

   

    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_ADMIN')")
    @GetMapping("/inicio/editar-perfil/{id}")
    public String editarperfil(HttpSession session, @PathVariable String id, ModelMap modelo) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        Usuario usuario = usuarioservicio.buscarPorId(id);

        modelo.addAttribute("usuario", usuario);

        return "eidtarPerfil";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_ADMIN')")
    @PostMapping("/inicio/editar-perfil")
    public String registro(HttpSession session, @RequestParam String id, ModelMap modelo, MultipartFile archivo, @RequestParam String dni, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String telefono, @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2) {
        Usuario usuario = null;

        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }
            usuario = usuarioservicio.buscarPorId(id);
            usuarioservicio.Modificar(archivo, id, dni, nombre, apellido, telefono, mail, clave, clave2);
            return "redirect:/inicio";

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "eidtarPerfil";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/usuarios")
    public String usuarios(ModelMap modelo) {

        List<Usuario> usuarios = usuarioservicio.listaUsuarios();
        modelo.addAttribute("usuario", usuarios);

        return "listaUsuarios";
    }

}
