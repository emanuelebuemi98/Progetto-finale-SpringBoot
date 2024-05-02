package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.corso.dto.RuoloDto;
import it.corso.service.RuoloService;
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

@Path("/ruolo")
public class RuoloController {
	
	   @Autowired
	    private RuoloService ruoloService;

	    @GET
	    @Path("get/{id}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getRuoloById(@PathParam("id") int id) {
	        try {
	            RuoloDto ruoloDto = ruoloService.getRuoloById(id);
	            if (ruoloDto != null) {
	                return Response.status(Response.Status.OK).entity(ruoloDto).build();
	            } else {
	                return Response.status(Response.Status.NOT_FOUND).build();
	            }
	        } catch (Exception e) {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }
	    }

	    @GET
	    @Path("/all")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getAllRuoli() {
	        try {
	            List<RuoloDto> ruoliDto = ruoloService.getAllRuoli();
	            return Response.status(Response.Status.OK).entity(ruoliDto).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }
	    }

	    @POST
	    @Path("/create")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response createRuolo(RuoloDto ruoloDto) {
	        try {
	            ruoloService.createRuolo(ruoloDto);
	            return Response.status(Response.Status.OK).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }
	    }

	    @PUT
	    @Path("/update")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response updateRuolo(RuoloDto ruoloDto) {
	        try {
	            ruoloService.updateRuolo(ruoloDto);
	            return Response.status(Response.Status.OK).entity(ruoloDto).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }
	    }

	    @DELETE
	    @Path("delete/{id}")
	    public Response deleteRuolo(@PathParam("id") int id) {
	        try {
	            ruoloService.deleteRuolo(id);
	            return Response.status(Response.Status.OK).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.BAD_REQUEST).entity(id).build();
	        }
	    }

}
