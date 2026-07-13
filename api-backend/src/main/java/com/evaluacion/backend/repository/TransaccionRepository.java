package com.evaluacion.backend.repository;

import com.evaluacion.backend.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Transaccion t SET t.estatus = 'Cancelada' WHERE t.id = :id AND t.referencia = :referencia")
    int cancelarTransaccion(@Param("id") Long id, @Param("referencia") String referencia);
}
