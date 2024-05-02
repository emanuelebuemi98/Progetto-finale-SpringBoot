package it.corso.service;

import java.util.List;

import it.corso.dto.CategoriaDto;
import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;

public interface CategoriaService {
	
	CategoriaDto getCategoriaById(int id);
	List<CategoriaDto> getAllCategorie();
	List<CategoriaDto> getAllCategoriaByFiltro(String filtro);
	void createCategoria(CategoriaDto categoriaDto);
	void updateCategoria(CategoriaDto categoriaDto);
	void deleteCategoria(int id);
	boolean existCategoriaById(int id);
	Categoria getCategoriaByNome(NomeCategoria nomeCategoria);

}
