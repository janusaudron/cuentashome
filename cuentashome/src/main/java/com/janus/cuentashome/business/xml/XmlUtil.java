package com.janus.cuentashome.business.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.janus.cuentashome.business.CuentasUtil;
import com.janus.cuentashome.business.domain.Categoria;
import com.janus.cuentashome.business.domain.Gasto;

import lombok.extern.java.Log;

@Log
public class XmlUtil {

    public static List<Gasto> procesarArchivo(Long mes, Long anio, String responsable) throws JsonParseException, JsonMappingException, IOException {

        List<Gasto> gastos = new ArrayList<>();

        File file = new File("C:/applogs/sms.xml");
        InputStream is = new FileInputStream(file);

        // InputStream is = new ByteArrayInputStream(archivo);

        XmlMapper xmlMapper = new XmlMapper();

        List<Sms> mensajes = xmlMapper.readValue(is, new TypeReference<List<Sms>>() {
        });

        log.info("mensajes:" + mensajes.size());

        List<Sms> mensajesFiltrados = new ArrayList<>();

        Calendar cal = Calendar.getInstance();

        // Define patterns for extracting text and value
        Pattern textPattern = Pattern.compile("en (.*?) por");
        Pattern valuePattern = Pattern.compile("por (\\d+(?:,\\d+)?)");
        String extractedText = "";
        Double extractedValue = 0.0;
        Gasto gasto = null;
        Categoria categoria = CuentasUtil.categorias.get(0);

        for (Sms sms : mensajes) {
            Long milisec = 0L;

            if (sms.getDate() != null) {
                milisec = Long.valueOf(sms.getDate());
                cal.setTimeInMillis(milisec);

                if (cal.get(Calendar.MONTH) == (mes-1) && cal.get(Calendar.YEAR) == anio) {

                    if (sms.getBody() != null && sms.getBody().contains("Scotiabank Colpatria: Realizaste trans")) {
                        log.info(sms.getAddress() + " - " + sms.getDate() + " - " + sms.getReadeableDate() + " - ");
                        mensajesFiltrados.add(sms);

                        Matcher textMatcher = textPattern.matcher(sms.getBody());
                        Matcher valueMatcher = valuePattern.matcher(sms.getBody());

                        if (textMatcher.find()) {
                            extractedText = textMatcher.group(1);
                        }

                        if (valueMatcher.find()) {
                            String valueString = valueMatcher.group(1).replace(",", "");
                            extractedValue = Double.parseDouble(valueString);
                        }

                        Categoria cat = new Categoria();
                        cat.setNombre(categoria.getNombre());
                        cat.setId(categoria.getId());

                        //log.info(extractedText + " - " + extractedValue);
                        gasto = new Gasto();
                        gasto.setNombre(extractedText.toUpperCase());
                        gasto.setValor(extractedValue.longValue());
                        gasto.setFecha(cal.getTime());
                        gasto.setAnio(anio);
                        gasto.setMes(mes);
                        gasto.setResponsable(responsable);
                        gasto.setCategoria(cat);

                        gastos.add(gasto);
                        

                    }

                }
            }
        }

        return gastos;
    }

}