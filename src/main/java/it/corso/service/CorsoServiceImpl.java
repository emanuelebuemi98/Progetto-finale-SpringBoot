package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	@Override
	public void createCourse(CorsoDto corsoDto) {
		Corso corso = mapper.map(corsoDto, Corso.class);
        corsoDao.save(corso);
	}

	@Override
	public void updateCourse(CorsoDto corsoDto) {
		Optional<Corso> corsoOptional = corsoDao.findById(corsoDto.getId());
        if (corsoOptional.isPresent()) {
            Corso corso = corsoOptional.get();
            mapper.map(corsoDto, corso);
            corsoDao.save(corso);
        }
		
	}

	@Override
	public void deleteCourse(int corsoId) {
		Optional<Corso> corsoOptional = corsoDao.findById(corsoId);
        if (corsoOptional.isPresent()) {
            corsoDao.delete(corsoOptional.get());
        } 
	}
}
