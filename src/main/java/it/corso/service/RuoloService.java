package it.corso.service;

import java.util.List;

import it.corso.dto.RuoloDto;
import it.corso.model.Ruolo;
import it.corso.model.Tipologia;

public interface RuoloService {
	
    RuoloDto getRuoloById(int id);
    List<RuoloDto> getAllRuoli();
    void createRuolo(RuoloDto ruolo);
    void updateRuolo(RuoloDto ruolo);
    void deleteRuolo(int id);
    boolean existsRuoloById(int id);
    Ruolo getRuoloByTipologia(Tipologia tipologia);

}
