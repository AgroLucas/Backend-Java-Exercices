package be.vinci.pae.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import be.vinci.pae.api.utils.Json;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.User;
import be.vinci.pae.services.DataServiceUserCollection;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/auths")
public class Authentication {

	private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
	private final ObjectMapper jsonMapper = new ObjectMapper();

	@Inject
	private DataServiceUserCollection dataService;

	@Inject
	private UserFactory userFactory;

	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(JsonNode json) {
		// Get and check credentials
		if (!json.hasNonNull("login") || !json.hasNonNull("password")) {
			return Response.status(Status.UNAUTHORIZED).entity("Login and password needed").type(MediaType.TEXT_PLAIN)
					.build();
		}
		String login = json.get("login").asText();
		String password = json.get("password").asText();
		// Try to login
		User user = this.dataService.getUser(login);
		if (user == null || !user.checkPassword(password)) {
			return Response.status(Status.UNAUTHORIZED).entity("Login or password incorrect").type(MediaType.TEXT_PLAIN)
					.build();
		}
		// Create token
		String token;
		try {
			token = JWT.create().withIssuer("auth0").withClaim("user", user.getID()).sign(this.jwtAlgorithm);
		} catch (Exception e) {
			throw new WebApplicationException("Unable to create token", e, Status.INTERNAL_SERVER_ERROR);
		}
		// Build response

		// load the user data from a public JSON view to filter out the private info not
		// to be returned by the API (such as password)
		User publicUser = Json.filterPublicJsonView(user, User.class);
		ObjectNode node = jsonMapper.createObjectNode().put("token", token).putPOJO("user", publicUser);
		return Response.ok(node, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Path("register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(JsonNode json) {
		// Get and check credentials
		if (!json.hasNonNull("login") || !json.hasNonNull("password")) {
			return Response.status(Status.UNAUTHORIZED).entity("Login and password needed").type(MediaType.TEXT_PLAIN)
					.build();
		}
		String login = json.get("login").asText();
		String password = json.get("password").asText();
		// Check if user exists
		if (this.dataService.getUser(login) != null) {
			return Response.status(Status.CONFLICT).entity("This login is already in use").type(MediaType.TEXT_PLAIN)
					.build();
		}
		// create user
		User user = this.userFactory.getUser();
		user.setID(1);
		user.setLogin(login);
		user.setPassword(user.hashPassword(password));
		this.dataService.addUser(user);

		// Create token
		String token;
		try {
			token = JWT.create().withIssuer("auth0").withClaim("user", user.getID()).sign(this.jwtAlgorithm);
		} catch (Exception e) {
			throw new WebApplicationException("Unable to create token", e, Status.INTERNAL_SERVER_ERROR);
		}
		// Build response

		// load the user data from a public JSON view to filter out the private info not
		// to be returned by the API (such as password)
		String publicSerializedUser = Json.serializePublicJsonView(user);
		User publicUser = Json.filterPublicJsonView(user, User.class);
		ObjectNode node = jsonMapper.createObjectNode().put("token", token).putPOJO("user", publicUser);
		return Response.ok(node, MediaType.APPLICATION_JSON).build();

	}

}
