package com.janus.cuentashome.business.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleDT implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long leidi;

	private Long oscar;

	private String mensaje;
	

}
