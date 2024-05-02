package it.corso.dao;

import org.springframework.data.repository.CrudRepository;

import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;

public interface CategoriaDao extends CrudRepository<Categoria, Integer>{
	
	boolean existsById(int id);
	Categoria findByNomeCategoria(NomeCategoria nomeCategoria);
	//boolean existsByNomeCategoria(NomeCategoria nomeCategoria);
}
