package it.corso.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteLoginResponseDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.service.Blacklist;
import it.corso.service.UtenteService;
import jakarta.validation.Valid;
import it.corso.dto.UtenteShowDto;
import it.corso.model.Ruolo;
import it.corso.model.Utente;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Path("utente")
public class UtenteController {

	@Autowired
	UtenteService utenteService;
	
	@Autowired
	private Blacklist blacklist;

	@POST
	@Path("/reg")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response userRegistration(@Valid @RequestBody UtenteRegistrazioneDto utenteDto) {

		try {
			if (!Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}", utenteDto.getPassword())) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			if (utenteService.existsUtenteByEmail(utenteDto.getEmail())) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			utenteService.utenteRegistration(utenteDto);
			return Response.status(Response.Status.OK).build();

		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}


	@GET
	@Path("/showuser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByEmail(@QueryParam("email") String email) {

		try {
			if (email != null && !email.isEmpty()) {
				UtenteShowDto utenteDto = utenteService.findUtente(email);
				if (utenteDto != null) {
					return Response.status(Response.Status.OK).entity(utenteDto).build();
				}
			}
			return Response.status(Response.Status.BAD_REQUEST).build();

		} catch (Exception e) {

			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		try {
			return Response.status(Response.Status.OK).entity(utenteService.getUtente()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@RequestBody UtenteAggiornamentoDto utente) {
		try {
			utenteService.updateUtente(utente);
			return Response.status(Response.Status.OK).entity(utente).build();
		} catch (Exception e) {

			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@DELETE
	@Path("/delete/{email}")
	public Response deleteUserByEmail(@PathParam("email") String email) {
		try {
			utenteService.deleteUtente(email);
			return Response.status(Response.Status.OK).entity(email).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userLogin(@RequestBody UtenteLoginRequestDto utenteLoginDto) {

		try {
			if (utenteService.loginUtente(utenteLoginDto)) {
				return Response.ok(generateToken(utenteLoginDto.getEmail())).build();
			}
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	private UtenteLoginResponseDto generateToken(String email) {

		// Eseguiamo una cifratura attraverso l'algoritmo di crittografia HMAC
		byte[] keySecret = "cusjshsvdthshaiana437882726bshsb2782ddd8282dw".getBytes();

		// creazione della chiave
		Key key = Keys.hmacShaKeyFor(keySecret);
		//Ci prendiamo il singolo utente
		Utente infoUtente = utenteService.getUtenteByEmail(email);

		Map<String, Object> mappa = new HashMap<>();
		mappa.put("email", email);
		mappa.put("nome", infoUtente.getNome());
		mappa.put("cognome", infoUtente.getCognome());

		List<String> ruoli = new ArrayList<>();

		for (Ruolo ruolo : infoUtente.getRuoli()) {
			ruoli.add(ruolo.getTipologia().name()); // name restituisce il nome dell'enam di Tipologia
		}
		mappa.put("ruoli", ruoli);

		// Data di crezione e ttl del token
		//Date creazioneData = new Date();
		//Date dataEnd = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(15L));
		Instant now = Instant.now();
		Date creazioneData = Date.from(now);
		Date dataEnd = Date.from(now.plus(15, ChronoUnit.MINUTES));

		// creazione del token firmato con la chiave segreta creata sopra
		String jwtToken = Jwts.builder().setClaims(mappa).setIssuer("http//localhost:8080").setIssuedAt(creazioneData)
				.setExpiration(dataEnd).signWith(key).compact(); // bisgna compattare tutto per tanto si fa il metodo
																	// (senno da errore)

		UtenteLoginResponseDto token = new UtenteLoginResponseDto();
		token.setToken(jwtToken);
		token.setTokenCreationTime(creazioneData);
		token.setTtl(dataEnd);

		return token;
	}
	
	@GET
	@Path("/logout")
	public Response userLogout (ContainerRequestContext requestContext) {
		try {
			// si potrebbe utilizzare Redis per salvare le sessioni
			String autorizzazioneHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			String token = autorizzazioneHeader.substring("Bearer".length()).trim();
			
			blacklist.invalidateToken(token);
			return Response.status(Response.Status.OK).build();			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	

}
