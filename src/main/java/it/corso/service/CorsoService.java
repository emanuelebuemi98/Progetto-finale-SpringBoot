package it.corso.service;

import java.util.List;

import it.corso.dto.CorsoDto;

public interface CorsoService {
	
	List<CorsoDto> getCourses();
    void createCourse(CorsoDto corsoDto);
    void updateCourse(CorsoDto corsoDto);
    void deleteCourse(int corsoId);
	
}
