package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.CategoriaDto;
import it.corso.service.CategoriaService;
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

@Path("/categoria")
public class CategoriaController {
	
	@Autowired
	CategoriaService categoriaService;
	
	@GET
	@Path("get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategoriaById(@PathParam("id") int id) {
	    try {
	        CategoriaDto categoriaDto = categoriaService.getCategoriaById(id);
	        if (categoriaDto != null) {
	            return Response.status(Response.Status.OK).entity(categoriaDto).build();
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
    public Response getAllCategorie() {
        try {
            List<CategoriaDto> categorieDto = categoriaService.getAllCategorie();
            return Response.status(Response.Status.OK).entity(categorieDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filtro")
	public Response getAllCategorieByFilter(@QueryParam("filtro") String filtro) {
		try {
			if(filtro != null && !filtro.isEmpty()) {
				List<CategoriaDto> categoriaDto = categoriaService.getAllCategoriaByFiltro(filtro);
				return Response.status(Response.Status.OK).entity(categoriaDto).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).build();				
			}
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCategoria(@RequestBody CategoriaDto categoriaDto) {
        try {
            categoriaService.createCategoria(categoriaDto);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
        	return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategoria(@RequestBody CategoriaDto categoriaDto) {
        try {
            categoriaService.updateCategoria(categoriaDto);
            return Response.status(Response.Status.OK).entity(categoriaDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
	@DELETE
	@Path("delete/{id}")
	public Response deleteCategoria(@PathParam("id") int id) {
		try {
			categoriaService.deleteCategoria(id);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(id).build();
		}
	}

}
