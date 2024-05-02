package it.corso.service;

import java.util.List;

import it.corso.dto.RegistrazioneUtenteDto;
import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteShowDto;
import it.corso.model.Utente;

public interface UtenteService {
	
	 void userRegistration(RegistrazioneUtenteDto registrazioneUtenteDto); // Metodo per la registrazione di un nuovo utente
	 
	 void deleteUser(String email); // Metodo per la cancellazione di un utente in base all'email
	 
	 Utente getUtenteByEmail(String email);
	 
	 List<UtenteShowDto> getUsers();
	 
	 UtenteShowDto findUser(String email);
	 
	 boolean existsUserByEmail(String email); // Metodo per verificare l'esistenza di un utente tramite l'email
	 void updateUser(UtenteAggiornamentoDto utenteAggiornatoDto);
	 
	 boolean loginUtente(UtenteLoginRequestDto utenteLoginDto);
}
