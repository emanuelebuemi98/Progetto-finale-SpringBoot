package it.corso.dao;

import org.springframework.data.repository.CrudRepository;

import it.corso.model.Ruolo;
import it.corso.model.Tipologia;

public interface RuoloDao extends CrudRepository<Ruolo, Integer>{
	
	boolean existsById(int id);
	Ruolo findByTipologia(Tipologia tipologia);
}
