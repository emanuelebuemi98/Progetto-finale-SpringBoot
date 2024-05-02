package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CategoriaDao;
import it.corso.dto.CategoriaDto;
import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	@Autowired
	CategoriaDao categoriaDao;

	private ModelMapper mapper = new ModelMapper();

	@Override
	public CategoriaDto getCategoriaById(int id) {
		Optional<Categoria> categoriaOptional = categoriaDao.findById(id);
		if (categoriaOptional.isPresent()) {
			/*
			 * Categoria categoria = categoriaOptional.get(); CategoriaDto categoriaDto =
			 * new CategoriaDto(); categoriaDto.setId(categoria.getId());
			 * categoriaDto.setNomeCategoria(categoria.getNomeCategoria());
			 */
			CategoriaDto categoriaDto = mapper.map(categoriaOptional.get(), CategoriaDto.class);
			return categoriaDto;
		}
		return null;
	}

	@Override
	public List<CategoriaDto> getAllCategorie() {
		List<Categoria> categorie = (List<Categoria>) categoriaDao.findAll();
		List<CategoriaDto> categorieDto = new ArrayList<>();

		for (Categoria c : categorie) {
			CategoriaDto categoriaDto = mapper.map(c, CategoriaDto.class);
			categorieDto.add(categoriaDto);
		}
		// modo più coinciso utilizzando il metodo forEach;
		// categorie.forEach(c -> categorieDto.add(mapper.map(c, CategoriaDto.class)));
		return categorieDto;
	}
	
	@Override
	public List<CategoriaDto> getAllCategoriaByFiltro(String filtro) {
		List<Categoria> categorie = (List<Categoria>) categoriaDao.findAll();
		List<CategoriaDto> categorieDto = new ArrayList<>();
		
		String[] nomiCategorie = filtro.split(",");
		for(String nome: nomiCategorie) {
			categorie.forEach(c -> {
				if (c.getNomeCategoria().name().equals(nome)) { //name() restituisce il nome del enam di NomeCategoria
					categorieDto.add(mapper.map(c, CategoriaDto.class));
				}
			});
		}
		return categorieDto;
	}

	@Override
	public void createCategoria(CategoriaDto categoriaDto) {
		//nel caso devo inserire solo categorie che non esistono
	    /*Categoria categoria = new Categoria();
	      for (NomeCategoria nomeCategoria : NomeCategoria.values()) {
	    	if (!categoriaDao.existsByNomeCategoria(nomeCategoria)) {
	    		//categoria.setNomeCategoria(categoriaDto.getNomeCategoria());
	    		categoria = mapper.map(categoriaDto, Categoria.class);
	    		categoriaDao.save(categoria);
	    	}
	    }*/
		Categoria categoria = mapper.map(categoriaDto, Categoria.class);
	    categoriaDao.save(categoria);
	}
	
	@Override
	public void updateCategoria(CategoriaDto categoriaDto) {
		Optional<Categoria> categoriaOptional = categoriaDao.findById(categoriaDto.getId());
		if (categoriaOptional.isPresent()) {
			Categoria categoria = mapper.map(categoriaDto, Categoria.class);
			categoriaDao.save(categoria);
		}
	}

	@Override
	public void deleteCategoria(int id) {
		Optional<Categoria> categoriaOptional = categoriaDao.findById(id);
		if (categoriaOptional.isPresent()) {
			categoriaDao.delete(categoriaOptional.get());
		}
	}

	@Override
	public boolean existCategoriaById(int id) {
		return categoriaDao.existsById(id);
	}

	@Override
	public Categoria getCategoriaByNome(NomeCategoria nomeCategoria) {
		return categoriaDao.findByNomeCategoria(nomeCategoria);
	}

}
