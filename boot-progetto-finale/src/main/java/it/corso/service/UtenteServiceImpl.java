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
import it.corso.dto.RegistrazioneUtenteDto;
import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteShowDto;
import it.corso.model.Ruolo;
import it.corso.model.Utente;
//import jakarta.validation.constraints.AssertFalse.List;

@Service
public class UtenteServiceImpl implements UtenteService {
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	UtenteDao utenteDao;
	
	@Autowired
	RuoloDao ruoloDao;

	@Override
	public void userRegistration(RegistrazioneUtenteDto registrazioneUtenteDto) {
		Utente utente = new Utente();
		utente.setNome(registrazioneUtenteDto.getNome());
		utente.setCognome(registrazioneUtenteDto.getCognome());
		utente.setEmail(registrazioneUtenteDto.getEmail());
		String sha256hex = DigestUtils.sha256Hex(registrazioneUtenteDto.getPassword());
        utente.setPassword(sha256hex);
        utenteDao.save(utente);
	}

	@Override
	public boolean existsUserByEmail(String email) {
		return utenteDao.existsByEmail(email);
	}

	@Override
	public void deleteUser(String email) {
		Utente utente = utenteDao.findByEmail(email);
		Optional<Utente> utenteOptional = utenteDao.findById(utente.getId());
		
		if (utenteOptional.isPresent()) {
			utenteDao.delete(utenteOptional.get());
		}
		
	}

	@Override
	public List<UtenteShowDto> getUsers() {
		List<Utente> listaUtenti = (List<Utente>) utenteDao.findAll();
		List<UtenteShowDto> utenteDto = new ArrayList<>();
		listaUtenti.forEach(u -> utenteDto.add(modelMapper.map(u, UtenteShowDto.class)));
		return utenteDto;
	}
	
	@Override
	public Utente getUtenteByEmail(String email) {
		return utenteDao.findByEmail(email);
	}

	// utilizzo il model mapper per mattare il singolo utente sulla classe UtenteShowDto
	@Override
	public UtenteShowDto findUser(String email) {
		//Utente utente = getUser(email);
		Utente utente = utenteDao.findByEmail(email);
		UtenteShowDto utenteDto = modelMapper.map(utente, UtenteShowDto.class);
		return utenteDto;
	}

	@Override
	public boolean loginUtente(UtenteLoginRequestDto utenteLoginDto) {
		
		Utente utente = new Utente();
		utente.setEmail(utenteLoginDto.getEmail());
		// recupero la password e la setto su utente
		utente.setPassword(utenteLoginDto.getPassword());
		//con il getPassword di utente recupero la password hashata
		String sha256hex = DigestUtils.sha256Hex(utente.getPassword());
		
		Utente credenzialiUtente = utenteDao.findByEmailAndPassword(utente.getEmail(), sha256hex);
		return credenzialiUtente != null ? true : false;
	}


	@Override
	public void updateUser(UtenteAggiornamentoDto utenteAggiornatoDto) {
		try {
			Utente utente = utenteDao.findByEmail(utenteAggiornatoDto.getEmail());
			
			if (utente != null) {
				utente.setNome(utenteAggiornatoDto.getNome());
				utente.setCognome(utenteAggiornatoDto.getCognome());
				utente.setEmail(utenteAggiornatoDto.getEmail());
				
				List<Ruolo> ruoliUtente = new ArrayList<>();
				Optional<Ruolo> ruoloOptional = Optional.ofNullable(ruoloDao.findById(utenteAggiornatoDto.getIdRuolo())); 
				
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

}
