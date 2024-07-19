package com.janus.cuentashome.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.janus.cuentashome.business.domain.Categoria;
import com.janus.cuentashome.business.domain.Gasto;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CuentasUtil {

     @Autowired
    private CuentasHomeService service;

    public static volatile Map<String, Gasto> totalGastosMap;
    public static volatile List<String> nombresGastos = new ArrayList<>();
    public static volatile List<Categoria> categorias = new ArrayList<>();
    public static String rutaArchivo = "C:/applogs/sms.xml";


    //@PostConstruct

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    private void init(){
         try {
            log.info("Cargando listas iniciales");
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
                    //log.debug("gasto:" + g.getNombre());
                }

                nombresGastos = totalGastosMap.keySet().stream().collect(Collectors.toList());
            }


            categorias = service.getCategorias();


        } catch (Exception e) {
            log.error("Error cargando listas iniciales: ", e);
        }

    }

}
