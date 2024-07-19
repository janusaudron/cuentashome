package com.janus.cuentashome.business.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sms {

    public String address;
    public String date;
    @JacksonXmlProperty(localName = "readable_date")
    public String readeableDate;
    public String body;

}
