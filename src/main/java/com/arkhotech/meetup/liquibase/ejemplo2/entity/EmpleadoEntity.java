package com.arkhotech.meetup.liquibase.ejemplo2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="empleados")
public class EmpleadoEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="rut")
	private Integer rut;
	
	@Column(name="dv")
	private String dv;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="apellido")
	private String apellido;
	
	@Column(name="email")
	private String email;
	
	@Column(name="address")
	private String address;
	
	public EmpleadoEntity() {
		
	}
	
	public EmpleadoEntity(Integer rut, String dv, String nombre, String apellido, String email, String address) {
		this.rut = rut;
		this.dv = dv;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.address = address;
	}
	
	public Integer getRut() {
		return rut;
	}
	public void setRut(Integer rut) {
		this.rut = rut;
	}
	
	public String getDv() {
		return dv;
	}
	
	public void setDv(String dv) {
		this.dv = dv;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
