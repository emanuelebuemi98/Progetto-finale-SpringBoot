package it.corso.jwt;

import java.io.IOException;
import java.security.Key;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.NotAcceptableException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@JWTTokenNeeded
@Provider
public class JWTTokenNeededFilter implements ContainerRequestFilter {

	//Iniezione delle dipendenze con context anzicche il @autowired
	@Context 
	private ResourceInfo resorceInfo;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println("filtro");
		Secured annotationRole = resorceInfo.getResourceMethod().getAnnotation(Secured.class);
		if (annotationRole == null) {
			annotationRole = resorceInfo.getResourceClass().getAnnotation(Secured.class);
		}
		//Estraiamo dall'header della richiesta l'autorizzazione che contiene il nostro token
		String autorizzazioneHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		//se è null o se non inizia con bearer 
		if (autorizzazioneHeader == null || !autorizzazioneHeader.startsWith("Bearer ")) { //bearer è un tipo di token
			throw new NotAcceptableException("Authorization header must be provider");
		}
		// estrae il token dal HTTP authorization header
		String token = autorizzazioneHeader.substring("Bearer".length()).trim();
		
		try {
			//utilizzo la stessa chiave per verificare se è la stessa oppure no
			byte[] keySecret = "cusjshsvdthshaiana437882726bshsb2782ddd8282dw".getBytes();	
			Key key = Keys.hmacShaKeyFor(keySecret);
			
			//verifica la firma del toten tramite setSigningKey
			
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			
			Claims body = claims.getBody();
			List<String> rolesToken = body.get("ruoli", List.class);
			Boolean hasRole = false;
			
			//facciamo un controllo per verificare se il ruolo dell'annotation è contenuto dentro la lista dei ruoli
			for (String role : rolesToken) {
				if (role.equals(annotationRole.role())) {
					System.out.println(role);
					hasRole = true;
				}
			}
			// nel caso in cui non diventa mai true con il not diventa true
			if (!hasRole) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
			
		} catch (Exception e) {
			//se qualcosa va storto ritorniamo un 401 UNAUTHORIZED
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		
	}

}
