package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.RuoloDao;
import it.corso.dao.UtenteDao;
import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.dto.UtenteShowDto;
import it.corso.model.Ruolo;
import it.corso.model.Utente;

@Service
public class UtenteServiceImpl implements UtenteService {
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	UtenteDao utenteDao;
	
	@Autowired
	RuoloDao ruoloDao;

	@Override
	public void utenteRegistration(UtenteRegistrazioneDto registrazioneUtenteDto) {
		Utente utente = new Utente();
		utente.setNome(registrazioneUtenteDto.getNome());
		utente.setCognome(registrazioneUtenteDto.getCognome());
		utente.setEmail(registrazioneUtenteDto.getEmail());
		String sha256hex = DigestUtils.sha256Hex(registrazioneUtenteDto.getPassword());
        utente.setPassword(sha256hex);
        utenteDao.save(utente);
	}

	@Override
	public void deleteUtente(String email) {
		Utente utente = utenteDao.findByEmail(email);
		Optional<Utente> utenteOptional = utenteDao.findById(utente.getId());
		
		if (utenteOptional.isPresent()) {
			utenteDao.delete(utenteOptional.get());
		}
		
	}

	@Override
	public List<UtenteShowDto> getUtente() {
		List<Utente> listaUtenti = (List<Utente>) utenteDao.findAll();
		List<UtenteShowDto> utenteDto = new ArrayList<>();
		listaUtenti.forEach(u -> utenteDto.add(modelMapper.map(u, UtenteShowDto.class)));
		return utenteDto;
	}

	// utilizzo il model mapper per mattare il singolo utente sulla classe UtenteShowDto
	@Override
	public UtenteShowDto findUtente(String email) {
		//Utente utente = getUser(email);
		Utente utente = utenteDao.findByEmail(email);
		UtenteShowDto utenteDto = modelMapper.map(utente, UtenteShowDto.class);
		return utenteDto;
	}

	@Override
	public boolean loginUtente(UtenteLoginRequestDto utenteLoginDto) {
		
		Utente utente = new Utente();
		// recupero la password e la setto su utente (stesso per email)
		utente.setEmail(utenteLoginDto.getEmail());
		utente.setPassword(utenteLoginDto.getPassword());
		//con il getPassword di utente recupero la password hashata
		String sha256hex = DigestUtils.sha256Hex(utente.getPassword());
		
		Utente credenzialiUtente = utenteDao.findByEmailAndPassword(utente.getEmail(), sha256hex);
		return credenzialiUtente != null ? true : false;
	}


	@Override
	public void updateUtente(UtenteAggiornamentoDto utenteAggiornatoDto) {
		try {
			Utente utente = utenteDao.findByEmail(utenteAggiornatoDto.getEmail());
			
			if (utente != null) {
				utente.setNome(utenteAggiornatoDto.getNome());
				utente.setCognome(utenteAggiornatoDto.getCognome());
				utente.setEmail(utenteAggiornatoDto.getEmail());
				
				List<Ruolo> ruoliUtente = new ArrayList<>();
				Optional<Ruolo> ruoloOptional = ruoloDao.findById(utenteAggiornatoDto.getIdRuolo()); 
				
				if (ruoloOptional.isPresent()) {
					Ruolo ruolo = ruoloOptional.get();
					ruolo.setId(utenteAggiornatoDto.getIdRuolo()); 
					
					ruoliUtente.add(ruolo);
					utente.setRuoli(ruoliUtente);
				}
				utenteDao.save(utente);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Utente getUtenteByEmail(String email) {
		return utenteDao.findByEmail(email);
	}
	
	@Override
	public boolean existsUtenteByEmail(String email) {
		return utenteDao.existsByEmail(email);
	}


}
