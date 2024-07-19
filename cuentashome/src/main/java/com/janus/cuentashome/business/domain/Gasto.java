package com.janus.cuentashome.business.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Gasto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Long id;
	
	@Column(nullable = false)
	private String nombre;
	
	@Column(nullable = false)
	private Long valor;
	
	@Column(nullable = false)
	private String responsable;

	@Column(nullable = false)
	private Long anio;

	@Column(nullable = false)
	private Long mes;

	@Transient
	private Date fecha;

	@ManyToOne(fetch = FetchType.EAGER)
    private Categoria categoria;
	
	/**
	 * Constructor de Rol
	 */
	public Gasto() {}
	

}
