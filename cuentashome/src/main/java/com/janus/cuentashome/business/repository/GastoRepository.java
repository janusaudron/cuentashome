package com.janus.cuentashome.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.janus.cuentashome.business.domain.Gasto;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {
    List<Gasto> findByAnioAndMes(Long anio, Long mes);

    @Query("select g from Gasto g where (g.anio > ?1) or (g.anio = ?1 and g.mes >= ?2)")
    List<Gasto> findSinceDateGastos(Long anio, Long mes);
}

