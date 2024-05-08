package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.corso.dto.CorsoDto;
import it.corso.jwt.JWTTokenNeeded;
import it.corso.jwt.Secured;
import it.corso.service.CorsoService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Secured(role = "Admin")
@JWTTokenNeeded
@Path("/corso")
public class CorsoController {

	@Autowired
	private CorsoService corsoService;

	@GET
	@Path("/corsi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourses() {
		try {
			List<CorsoDto> listaCorsi = corsoService.getCourses();
			return Response.status(Response.Status.OK).entity(listaCorsi).build();

		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Errore caricamento utenti").build();
		}
	}
	
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCourse(CorsoDto corsoDto) {
        try {
            corsoService.createCourse(corsoDto);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Errore durante la creazione del corso").build();
        }
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCourse(CorsoDto corsoDto) {
        try {
            corsoService.updateCourse(corsoDto);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Errore durante l'aggiornamento del corso").build();
        }
    }

    @DELETE
    @Path("/delete/{corsoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCourse(@PathParam("corsoId") int corsoId) {
        try {
            corsoService.deleteCourse(corsoId);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Errore durante l'eliminazione del corso").build();
        }
    }

}
