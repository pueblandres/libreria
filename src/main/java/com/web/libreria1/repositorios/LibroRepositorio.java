package com.web.libreria1.repositorios;

import com.web.libreria1.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query("SELECT a FROM Libro a WHERE a.autor.id = :id")
    public List<Libro> buscarPorAutor(@Param("id") String id);
    
      @Query("SELECT a FROM Libro a WHERE a.autor.id = :id AND a.alta = :alta")
    public List<Libro> buscarPorAutorActivos(@Param("id") String id,@Param("alta")Boolean alta);

    @Query("SELECT a FROM Libro a WHERE a.editorial.id = :id")
    public List<Libro> buscarPorEditorial(@Param("id") String id);
    
      @Query("SELECT a FROM Libro a WHERE a.editorial.id = :id  AND a.alta = :alta ")
    public List<Libro> buscarPorEditorialActivos(@Param("id") String id,@Param("alta")Boolean alta);
    
      @Query("SELECT a FROM Libro a WHERE a.alta = :alta")
    public List<Libro> librosActivos(@Param("alta")Boolean alta);

}
