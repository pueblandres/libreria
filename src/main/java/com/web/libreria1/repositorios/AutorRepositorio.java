
package com.web.libreria1.repositorios;

import com.web.libreria1.entidades.Autor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String>{
     @Query("SELECT a FROM Autor a WHERE a.alta = :alta")
    public List<Autor> autoresActivos(@Param("alta")Boolean alta);
    
      @Query("SELECT a FROM Autor a WHERE a.alta = :alta AND a.id = :id")
    public Autor autoresActivosPorId(@Param("alta")Boolean alta, @Param("id") String id);

}
