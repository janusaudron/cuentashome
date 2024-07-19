package com.janus.cuentashome.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.janus.cuentashome.business.domain.Categoria;
import com.janus.cuentashome.business.domain.Gasto;
import com.janus.cuentashome.business.repository.CategoriaRepository;
import com.janus.cuentashome.business.repository.GastoRepository;

@Service("cuentasHomeService")
public class CuentasHomeService {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /*** Gasto   ***/
    public void createGasto(Gasto gasto) {
        gastoRepository.save(gasto);
    }

    public void createGastos(List<Gasto> gastos) {
        gastoRepository.saveAll(gastos);
    }

    public List<Gasto> getGastosAnioyMes(Long anio, Long mes) {
        return gastoRepository.findByAnioAndMes(anio, mes);
    }

    public List<Gasto> getGastosAnteriores(Long anio, Long mes) {
        return gastoRepository.findSinceDateGastos(anio, mes);
    }

    public void eliminarGasto(Gasto gasto) {
        gastoRepository.delete(gasto);
    }

    /*** Categoria  ***/
    public void createCategoria(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }
}
