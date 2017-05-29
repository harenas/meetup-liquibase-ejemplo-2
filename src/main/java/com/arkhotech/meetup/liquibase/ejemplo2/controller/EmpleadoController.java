package com.arkhotech.meetup.liquibase.ejemplo2.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arkhotech.meetup.liquibase.ejemplo2.dao.EmpleadoRepository;
import com.arkhotech.meetup.liquibase.ejemplo2.entity.EmpleadoEntity;

@RestController
@RequestMapping("/ejemplo2")
public class EmpleadoController {

	@Autowired
	EmpleadoRepository repo;

	static final Logger logger = LogManager.getLogger(EmpleadoController.class.getName());

	/**
	 * Retorna los datos de un empleado en particular
	 * @param rut
	 * @return
	 */
    @RequestMapping("/empleado")
    public EmpleadoEntity getEmpleado(@RequestParam(value="rut") Integer rut) {
    	logger.info("RUT : " + rut);
    	EmpleadoEntity emp = repo.findOne(rut);
    	return emp;
    }

    /**
     * Retorna el listado completo de empleados
     * @return
     */
    @RequestMapping("/listaEmpleados")
    public List<EmpleadoEntity> getListaEmpleados() {
    	return (List<EmpleadoEntity>) repo.findAll();
    }

    
}
