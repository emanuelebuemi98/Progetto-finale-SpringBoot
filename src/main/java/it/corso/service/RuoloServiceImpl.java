package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.RuoloDao;
import it.corso.dto.RuoloDto;
import it.corso.model.Ruolo;
import it.corso.model.Tipologia;

@Service
public class RuoloServiceImpl implements RuoloService {
	
	@Autowired
	RuoloDao ruoloDao;
	
	private ModelMapper mapper = new ModelMapper();

	@Override
	public RuoloDto getRuoloById(int id) {
		Optional<Ruolo> ruoloOptional = ruoloDao.findById(id);
		if (ruoloOptional.isPresent()) {
			RuoloDto ruoloDto = mapper.map(ruoloOptional.get(), RuoloDto.class);
			return ruoloDto;
		}
		return null;
	}

	@Override
	public List<RuoloDto> getAllRuoli() {
		List<Ruolo> ruoli = (List<Ruolo>) ruoloDao.findAll();
		List<RuoloDto> ruoliDto = new ArrayList<>();

		for (Ruolo r : ruoli) {
			RuoloDto ruoloDto = mapper.map(r, RuoloDto.class);
			ruoliDto.add(ruoloDto);
		}
		return ruoliDto;
	}

	@Override
	public void createRuolo(RuoloDto ruoloDto) {
	    /*Ruolo ruolo = new Ruolo();
	    ruoloDto.setTipologia(ruoloDto.getTipologia());*/
		Ruolo ruolo = mapper.map(ruoloDto, Ruolo.class);
	    ruoloDao.save(ruolo);
	}

	@Override
	public void updateRuolo(RuoloDto ruoloDto) {
		Optional<Ruolo> ruoloOptional = ruoloDao.findById(ruoloDto.getId());
		if (ruoloOptional.isPresent()) {
			Ruolo ruolo = mapper.map(ruoloDto, Ruolo.class);
			ruoloDao.save(ruolo);
		}
	}

	@Override
	public void deleteRuolo(int id) {
		Optional<Ruolo> ruoloOptional = ruoloDao.findById(id);
		if (ruoloOptional.isPresent()) {
			ruoloDao.delete(ruoloOptional.get());
		}
	}

	@Override
	public boolean existsRuoloById(int id) {
		return ruoloDao.existsById(id);
	}

	@Override
	public Ruolo getRuoloByTipologia(Tipologia tipologia) {
		return ruoloDao.findByTipologia(tipologia);
	}
	
}
