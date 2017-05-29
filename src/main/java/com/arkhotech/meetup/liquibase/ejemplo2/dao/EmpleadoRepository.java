package com.arkhotech.meetup.liquibase.ejemplo2.dao;

import org.springframework.data.repository.CrudRepository;

import com.arkhotech.meetup.liquibase.ejemplo2.entity.EmpleadoEntity;

public interface EmpleadoRepository extends CrudRepository<EmpleadoEntity, Integer> {

}
