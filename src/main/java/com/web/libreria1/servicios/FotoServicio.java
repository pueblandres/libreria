package com.web.libreria1.servicios;

import com.web.libreria1.entidades.Foto;
import com.web.libreria1.errores.ErrorServicio;
import com.web.libreria1.repositorios.FotoRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fotorepositorio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Foto guardar(MultipartFile archivo) throws ErrorServicio {
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
              

                return fotorepositorio.save(foto);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Foto actualizar(MultipartFile archivo, String id) throws ErrorServicio {

        if (archivo != null) {
            try {
                Foto foto = new Foto();
                if (id != null) {
                    Optional<Foto> respuesta = fotorepositorio.findById(id);
                    if (respuesta.isPresent()) {
                        foto = respuesta.get();
                    }
                }
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fotorepositorio.save(foto);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;

    }

    @Transactional(readOnly = true)
    public Foto buscarPoid(String idFoto) {

        return fotorepositorio.getById(idFoto);
    }
}
