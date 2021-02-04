package be.vinci.pae.api;

import java.util.List;

import be.vinci.pae.domain.Text;
import be.vinci.pae.services.DataServiceTextCollection;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/texts")
public class TextRessource {	

	private static enum DifficultyLevel {easy, medium, hard};

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Text> getAllLevelTexts(@DefaultValue("all") @QueryParam("level") String level) {
		if (level.equals("all"))
			return DataServiceTextCollection.getTexts();
		if (!isLevelCorrect(level))
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("The level " + level + " does not exist").type("text/plain").build());
		return DataServiceTextCollection.getTexts(level);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Text getTextId(@PathParam("id") int id) {
		if (id < 1)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Lacks of mandatory info").type("text/plain").build());
		Text text = DataServiceTextCollection.getTextId(id);
		if (text == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("The text with the id " + id + " does not exist").type("text/plain").build());
		return text;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Text createText(Text text) {
		if (text == null || text.getContent() == null || !isLevelCorrect(text.getLevel()))
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());
		return DataServiceTextCollection.addText(text);
		
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Text deleteTextId(@PathParam("id") int id) {
		if (id < 1)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Lacks of mandatory info").type("text/plain").build());
		Text text = DataServiceTextCollection.deleteTextId(id);
		if (text == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("The text with the id " + id + " does not exist").type("text/plain").build());
		return text;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Text updateTextId(@PathParam("id") int id, Text text) {
		if (text == null || text.getContent() == null || !isLevelCorrect(text.getLevel()))
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());
		if (id < 1)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Lacks of mandatory info").type("text/plain").build());
		Text textUpdate = DataServiceTextCollection.updateTextId(id, text);
		if (textUpdate == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("The text with the id " + id + " does not exist").type("text/plain").build());
		return textUpdate;
	}
	
	private boolean isLevelCorrect(String level) {
		for (DifficultyLevel difficulty : DifficultyLevel.values()) {
			if (difficulty.name().equals(level))
				return true;
		}
		return false;
	}
	
	

}
