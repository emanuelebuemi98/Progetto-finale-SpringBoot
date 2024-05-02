package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import it.corso.model.Corso;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CorsoDao;
import it.corso.dto.CorsoDto;

@Service
public class CorsoServiceImpl implements CorsoService {

	@Autowired
	CorsoDao corsoDao;

	private ModelMapper mapper = new ModelMapper();

	@Override
	public List<CorsoDto> getCourses() {

		List<Corso> corso = (List<Corso>) corsoDao.findAll();
		List<CorsoDto> corsoDto = new ArrayList<>();
		corso.forEach(c -> corsoDto.add(mapper.map(c, CorsoDto.class)));

		return corsoDto;
	}
}
