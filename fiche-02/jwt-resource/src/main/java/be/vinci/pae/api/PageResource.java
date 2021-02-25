package be.vinci.pae.api;

import java.util.List;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.Page;
import be.vinci.pae.domain.User;
import be.vinci.pae.services.DataServicePageCollection;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/pages")
public class PageResource {
	
	
	private static enum Status_page {hidden, published};
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Authorize
	public Page create(Page page, @Context ContainerRequestContext requestContext) {
		if (page == null || page.getTitre() == null || page.getContenu() == null || page.getUri() == null || !isStatusCorrect(page.getStatus()))
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());
		User currentUser = (User) requestContext.getProperty("user");
		return DataServicePageCollection.addPage(page, currentUser.getID());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Authorize
	public List<Page> getPage(@Context ContainerRequestContext requestContext) {
		User currentUser = (User) requestContext.getProperty("user");
		return DataServicePageCollection.getPages(currentUser.getID());
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorize
	public Page getPage(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info").type("text/plain").build());
		User currentUser = (User) requestContext.getProperty("user");
		Page pageFound = DataServicePageCollection.getPage(id, currentUser.getID());
		if (pageFound == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Ressource with id = " + id + " could not be found").type("text/plain").build());
		return pageFound;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Authorize
	public Page updatePage(Page page, @PathParam("id") int id, @Context ContainerRequestContext requestContext) {
		if (page == null || page.getTitre() == null || page.getContenu() == null || page.getUri() == null || !isStatusCorrect(page.getStatus()))
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());
		User currentUser = (User) requestContext.getProperty("user");
		page.setId(id);
		page.setIdAuteur(currentUser.getID());
		Page pageUpdated = DataServicePageCollection.updatePage(page, currentUser.getID());
		if (pageUpdated == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Ressource with id = " + id + " could not be found").type("text/plain").build());
		return pageUpdated;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorize
	public Page deletePage(@PathParam("id") int id, @Context ContainerRequestContext requestContext) {
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info").type("text/plain").build());
		User currentUser = (User) requestContext.getProperty("user");
		Page deletedPage = DataServicePageCollection.deletePage(id, currentUser.getID());
		if (deletedPage == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Ressource with id = " + id + " could not be found").type("text/plain").build());
		return deletedPage;
	}
	
	
	private boolean isStatusCorrect(String status) {
		for (Status_page s : Status_page.values()) {
			if (s.name().equals(status))
				return true;
		}
		return false;
	}

}
