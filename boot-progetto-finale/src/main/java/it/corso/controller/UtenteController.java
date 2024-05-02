package it.corso.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.RegistrazioneUtenteDto;
import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteLoginResponseDto;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.LocalDateTime;

@Path("utente")
public class UtenteController {

	@Autowired
	UtenteService utenteService;

	@POST
	@Path("/reg")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response userRegistration(@Valid @RequestBody RegistrazioneUtenteDto utenteDto) {

		try {
			System.out.println("ciao");
			if (!Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}",
					utenteDto.getPassword())) {
				System.out.println("ciaoo");
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			if (utenteService.existsUserByEmail(utenteDto.getEmail())) {
				System.out.println("controller");

				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			utenteService.userRegistration(utenteDto);
			return Response.status(Response.Status.OK).build();

		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@DELETE
	@Path("/delete/{email}")
	public Response deleteUserByEmail(@PathParam("email") String email) {
		try {
			utenteService.deleteUser(email);
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
				UtenteShowDto utenteDto = utenteService.findUser(email);
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
			return Response.status(Response.Status.OK).entity(utenteService.getUsers()).build();
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

			utenteService.updateUser(utente);

			return Response.status(Response.Status.OK).build();

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
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	private UtenteLoginResponseDto generateToken(String email) {

		// Eseguiamo una cifratura attraverso l'algoritmo di crittografia HMAC
		byte[] keySecret = "cusjshsvdthshaiana437882726bshsb2782ddd8282dw".getBytes();

		// creazione della chiave
		Key key = Keys.hmacShaKeyFor(keySecret);
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
		Date creazioneData = new Date();
		Date dataEnd = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(15L));

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

}
