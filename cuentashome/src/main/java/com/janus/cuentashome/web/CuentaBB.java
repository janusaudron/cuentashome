package com.janus.cuentashome.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
//import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.file.UploadedFile;

import com.janus.cuentashome.business.CuentasHomeService;
import com.janus.cuentashome.business.CuentasUtil;
import com.janus.cuentashome.business.domain.Categoria;
import com.janus.cuentashome.business.domain.Constante;
import com.janus.cuentashome.business.domain.DetalleDT;
import com.janus.cuentashome.business.domain.Gasto;
import com.janus.cuentashome.business.xml.XmlUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Named("cuentaBB")
@Setter
@Getter
@Log4j2
@SessionScoped
public class CuentaBB {

    @Inject
    private CuentasHomeService service;

    private String usuario;
    private String mes;
    private String anio;

    private Gasto gasto;
    private List<Gasto> gastos;
    private List<String> nombresGastos = new ArrayList<>();
    private Map<String, Gasto> totalGastosMap;
    private List<Categoria> categorias;
    private List<Gasto> gastosCargados;

    private DetalleDT detalle;
    private boolean creacion = false;

    private PieChartModel pieModel = new PieChartModel();
    private UploadedFile archivo;

    private boolean cargue = false;

    // @PostConstruct
    public void init() {
        try {
            log.info("init CuentaBB");

            creacion = false;
            cargue = false;

            Calendar calendar = Calendar.getInstance();
            anio = String.valueOf(calendar.get(Calendar.YEAR));
            mes = String.valueOf(calendar.get(Calendar.MONTH) + 1);

            consultar();
            //consultarGastosAnteriores();
            totalGastosMap = CuentasUtil.totalGastosMap;
            nombresGastos = CuentasUtil.nombresGastos;
            categorias = CuentasUtil.categorias;
            //categorias = service.getCategorias();

            gasto = new Gasto();
            gasto.setCategoria(new Categoria());
            // createPieModel();
            //log.info("leyendo sms");
            //XmlUtil.procesarArchivo(null);

        } catch (Exception e) {
            MessageUtils.addErrorMessage(e);
            log.error("Error desconocido en el sistema: ", e);
        }
    }

    public String ingresar(String user) {
        if ("1".equals(user)) {
            this.usuario = Constante.LEIDI;
        } else {
            this.usuario = Constante.OSCAR;
        }

        return "principal.xhtml";
    }

    public void consultar() {
        try {
            log.info("entro consultar");
            gastos = service.getGastosAnioyMes(Long.valueOf(anio), Long.valueOf(mes));

            detalle = new DetalleDT();
            Long gastoLeidi = 0L;
            Long gastoOscar = 0L;
            Long diferencia = 0L;
            String mensaje = null;

            if (gastos != null && gastos.size() > 0) {
                for (Gasto gas : gastos) {
                    if (Constante.LEIDI.equals(gas.getResponsable())) {
                        gastoLeidi = gastoLeidi + gas.getValor();
                    } else {
                        gastoOscar = gastoOscar + gas.getValor();
                    }
                }

                diferencia = gastoLeidi - gastoOscar;
                if (diferencia > 0) {
                    mensaje = Constante.OSCAR + " debe ";
                } else {
                    mensaje = Constante.LEIDI + " debe ";
                    diferencia = Math.abs(diferencia);
                }

                DecimalFormat df = new DecimalFormat("###,###,###");
                this.detalle.setMensaje(mensaje + df.format(diferencia));
            }

            this.detalle.setLeidi(gastoLeidi);
            this.detalle.setOscar(gastoOscar);
        } catch (Exception e) {
            MessageUtils.addErrorMessage(e);
            log.error("Error desconocido en el sistema: ", e);
        }
    }

    /*public void consultarGastosAnteriores() {
        try {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -12);
            String anioOld = String.valueOf(calendar.get(Calendar.YEAR));
            String mesOld = String.valueOf(calendar.get(Calendar.MONTH) + 1);

            log.info("mesOld: " + mesOld + "- anioOld: " + anioOld);

            List<Gasto> totalGastos = service.getGastosAnteriores(Long.valueOf(anioOld), Long.valueOf(mesOld));
            totalGastosMap = new HashMap<>();

            if (totalGastos != null && totalGastos.size() > 0) {
                log.info("totalGastos:" + totalGastos.size());
                for (Gasto g : totalGastos) {
                    totalGastosMap.put(g.getNombre(), g);
                    log.debug("gasto:" + g.getNombre());
                }

                nombresGastos = totalGastosMap.keySet().stream().collect(Collectors.toList());
            }

        } catch (Exception e) {
            MessageUtils.addErrorMessage(e);
            log.error("Error desconocido en el sistema: ", e);
        }
    }*/

    public void irCrear() {
        this.gasto = new Gasto();
        this.gasto.setAnio(Long.valueOf(anio));
        this.gasto.setMes(Long.valueOf(mes));
        this.gasto.setResponsable(this.usuario);
        this.gasto.setCategoria(new Categoria());
        this.creacion = false;

    }

    public void irCargue() {
        this.cargue = true; 
    }

    public void guardar() {
        try {
            gasto.setNombre(gasto.getNombre().toUpperCase());
            service.createGasto(gasto);

            MessageUtils.addInfoMessage("guardarexito");

            consultar();

            PrimeFaces.current().executeScript("PF('gastoDialog').hide()");
            PrimeFaces.current().ajax().update("principalForm");

        } catch (Exception e) {
            MessageUtils.addErrorMessage(e);
            log.error("Error desconocido en el sistema: ", e);
        }
    }

    public void eliminar() {
        try {
            service.eliminarGasto(gasto);

            MessageUtils.addInfoMessage("eliminarexito");

            consultar();

            PrimeFaces.current().ajax().update("principalForm");

        } catch (Exception e) {
            MessageUtils.addErrorMessage(e);
            log.error("Error desconocido en el sistema: ", e);
        }
    }

    public void cargar() {
        try {
            log.info("entro a cargar");
            //if (archivo != null) {
                log.info("entro a cargar2");
                //gastosCargados = XmlUtil.procesarArchivo(archivo.getContent());
                gastosCargados = XmlUtil.procesarArchivo(Long.valueOf(mes), Long.valueOf(anio), usuario);
            //}

        } catch (Exception e) {
            MessageUtils.addErrorMessage(e);
            log.error("Error desconocido en el sistema: ", e);
        }
    }

    public List<String> completarGasto(String query) {
        String queryUpperCase = query.toUpperCase();
        return nombresGastos.stream().filter(t -> t.startsWith(queryUpperCase)).collect(Collectors.toList());
    }

    public void seleccionarGasto() {
        log.info("Entro a seleccionarGasto:" + gasto.getNombre());
        Gasto temp = totalGastosMap.get(gasto.getNombre());
        if (temp != null) {
            this.gasto.setCategoria(temp.getCategoria());
            this.gasto.setValor(temp.getValor());
        }
    }

    public String guardarCargados() {
        try {

            for (Gasto g : gastosCargados) {
                Categoria c = g.getCategoria();
                log.info("categoria:" + c.getId());
            }

            service.createGastos(gastosCargados);

            MessageUtils.addInfoMessage("guardarexito");

            consultar();

            PrimeFaces.current().executeScript("PF('gastoDialog').hide()");
            PrimeFaces.current().ajax().update("principalForm");

        } catch (Exception e) {
            MessageUtils.addErrorMessage(e);
            log.error("Error desconocido en el sistema: ", e);
        }

        //return "principal?faces-redirect=true";
        return "principal.xhtml";
    }

    public void eliminarCargado() {
        try {
            gastosCargados.remove(gasto);
            MessageUtils.addInfoMessage("eliminarexito");
            PrimeFaces.current().ajax().update("cargadosForm");

        } catch (Exception e) {
            MessageUtils.addErrorMessage(e);
            log.error("Error desconocido en el sistema: ", e);
        }
    }

    public void irReporte() {
        log.info("ingreso pie model");
        pieModel = new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(102, 204, 0)");
        bgColors.add("rgb(0, 204, 204)");
        bgColors.add("rgb(102, 0, 204)");
        bgColors.add("rgb(221,160,221)");
        bgColors.add("rgb(100,149,237)");
        bgColors.add("rgb(238,130,238)");
        bgColors.add("rgb(255,228,196)");
        bgColors.add("rgb(210,105,30)");
        bgColors.add("rgb(188,143,143)");
        bgColors.add("rgb(72,61,139)");
        bgColors.add("rgb(95,158,160)");
        dataSet.setBackgroundColor(bgColors);

        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        Map<String, Long> categoriaValorMap = new HashMap<>();
        for (Gasto g : gastos) {
            if (categoriaValorMap.containsKey(g.getCategoria().getNombre())) {
                categoriaValorMap.put(g.getCategoria().getNombre(),
                        categoriaValorMap.get(g.getCategoria().getNombre()) + g.getValor());
            } else {
                categoriaValorMap.put(g.getCategoria().getNombre(), g.getValor());
            }
        }

        for (Map.Entry<String, Long> entry : categoriaValorMap.entrySet()) {
            values.add(entry.getValue());
            labels.add(entry.getKey());
        }

        dataSet.setData(values);
        data.setLabels(labels);

        data.addChartDataSet(dataSet);

        pieModel.setData(data);

    }

    public void cambioCategoriaCargue(SelectEvent event) {
        Categoria c = (Categoria) event.getObject();

        log.info("Categoia:" + c.getNombre());

    }

}
