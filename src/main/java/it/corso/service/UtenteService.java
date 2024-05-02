package it.corso.service;

import java.util.List;

import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.dto.UtenteShowDto;
import it.corso.model.Utente;

public interface UtenteService {
	
	 void utenteRegistration(UtenteRegistrazioneDto registrazioneUtenteDto); 
	 boolean loginUtente(UtenteLoginRequestDto utenteLoginDto);
	 
	 List<UtenteShowDto> getUtente();
	 
	 UtenteShowDto findUtente(String email);
	 
	 void updateUtente(UtenteAggiornamentoDto utenteAggiornatoDto);
	 void deleteUtente(String email);
	 
	 Utente getUtenteByEmail(String email);
	 boolean existsUtenteByEmail(String email); 
}
