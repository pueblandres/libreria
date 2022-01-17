
package com.web.libreria1.repositorios;


import com.web.libreria1.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String>{
    @Query("SELECT a FROM Editorial a WHERE a.alta = :alta")
    public List<Editorial> editorialesActivas(@Param("alta")Boolean alta);
    
     @Query("SELECT a FROM Editorial a WHERE a.alta = :alta AND a.id = :id")
    public Editorial editorialesActivasPorId(@Param("alta")Boolean alta,@Param("id") String id);


}
