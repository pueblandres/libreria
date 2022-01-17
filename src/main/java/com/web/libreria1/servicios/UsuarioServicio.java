package com.web.libreria1.servicios;

import com.web.libreria1.entidades.Usuario;
import com.web.libreria1.entidades.Foto;
import com.web.libreria1.enumeraciones.Rol;
import com.web.libreria1.errores.ErrorServicio;
import com.web.libreria1.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.Random;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuariorepositorio;

    @Autowired
    private FotoServicio fotoservicio;

    @Autowired
    private envioMailServicio envioMail;

    @Transactional
    public Usuario Registrar(MultipartFile archivo, String dni, String nombre, String apellido, String telefono, String mail, String clave, String clave2) throws ErrorServicio {

        validar(dni, nombre, apellido, telefono, clave, clave2);
        if (usuariorepositorio.buscarPorMail(mail) != null) {
            throw new ErrorServicio("El mail ingresado ya esta registrado");
        } else {
            Usuario usuario = new Usuario();
            usuario.setDni(dni);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setTelefono(telefono);
            usuario.setAlta(true);
            usuario.setMail(mail);
            usuario.setRol("USUARIO");

            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            Foto foto = fotoservicio.guardar(archivo);
            usuario.setFoto(foto);

            usuariorepositorio.save(usuario);
            try {
                envioMail.enviar("BIENVENIDO A LA LIBRERIA WEB", "Libreria Martin", usuario.getMail());
            } catch (Exception e) {
            }

            return usuario;
        }
    }

    @Transactional
    public void verificarMail() throws ErrorServicio {

    }

    @Transactional
    public Usuario Modificar(MultipartFile archivo, String id, String dni, String nombre, String apellido, String telefono, String mail, String clave, String clave2) throws ErrorServicio {

        validar(dni, nombre, apellido, telefono, clave, clave2);

        Optional<Usuario> respuesta = usuariorepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setDni(dni);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setTelefono(telefono);
            usuario.setAlta(true);
            usuario.setMail(mail);
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            String idfoto = null;
            if (usuario.getFoto() != null) {
                idfoto = usuario.getFoto().getId();
            }
            Foto foto = fotoservicio.actualizar(archivo, idfoto);
            usuario.setFoto(foto);

            return usuariorepositorio.save(usuario);

        } else {
            throw new ErrorServicio("El usuario no existe");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuariorepositorio.buscarPorMail(mail);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());
            GrantedAuthority p2 = new SimpleGrantedAuthority("ALTA_" + usuario.getAlta());
            permisos.add(p1);
            permisos.add(p2);

            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);

            return user;

        } else {
            return null;
        }

    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(String id) {

        return usuariorepositorio.getById(id);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPormail(String mail) {

        return usuariorepositorio.buscarPorMail(mail);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listaUsuarios() {

        return usuariorepositorio.findAll();
    }

    public void validar(String dni, String nombre, String apellido, String telefono, String clave, String clave2) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }

        if (dni == null || dni.isEmpty()) {
            throw new ErrorServicio("El dni no puede ser nulo");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El dni no puede ser nulo");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new ErrorServicio("El dni no puede ser nulo");

        }
        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ErrorServicio("La clave no puede ser nula y tiene que ser mayor a 6 caracteres");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("La claves deben ser iguales");
        }
    }
}
