package be.vinci.pae.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import be.vinci.pae.domain.Page;

public class DataServicePageCollection {
	private static final String DB_FILE_PATH = "db.json";
	private static final String COLLECTION_NAME = "pages";
	private final static ObjectMapper jsonMapper = new ObjectMapper();
	
	private static List<Page> pages ;
	static {
		pages = loadDataFromFile();
	}	

	public static Page getPage(int id, int idAuteur) {
		return pages.stream().filter(item -> item.getId() == id && (item.getStatus().equals("published") || item.getIdAuteur() == idAuteur)).findAny().orElse(null);
	}

	public static List<Page> getPages(int idAuteur) {
		return pages.stream().filter(item -> item.getStatus().equals("published") || item.getIdAuteur() == idAuteur).collect(Collectors.toList());
	}

	public static Page addPage(Page Page, int idAuteur) {
		Page.setId(nexPagetId());
		Page.setIdAuteur(idAuteur);
		// escape dangerous chars to protect against XSS attacks
		Page.setTitre(StringEscapeUtils.escapeHtml4(Page.getTitre()));
		Page.setContenu(StringEscapeUtils.escapeHtml4(Page.getContenu()));
		Page.setUri(StringEscapeUtils.escapeHtml4(Page.getUri()));
		Page.setStatus(StringEscapeUtils.escapeHtml4(Page.getStatus()));
		pages.add(Page);
		saveDataToFile();
		return Page;
	}

	public static int nexPagetId() {
		if (pages.size() == 0)
			return 1;
		return pages.get(pages.size() - 1).getId() + 1;
	}

	public static Page deletePage(int id, int idAuteur) {
		if (pages.size() == 0 | id == 0)
			return null;
		Page PageToDelete = getPage(id, idAuteur);
		if (PageToDelete == null)
			return null;
		int index = pages.indexOf(PageToDelete);
		pages.remove(index);
		saveDataToFile();
		return PageToDelete;
	}

	public static Page updatePage(Page page, int idAuteur) {
		if (pages.size() == 0 | page == null)
			return null;
		Page PageToUpdate = getPage(page.getId(), idAuteur);
		if (PageToUpdate == null)
			return null;
		// escape dangerous chars to protect against XSS attacks
		page.setTitre(StringEscapeUtils.escapeHtml4(page.getTitre()));
		page.setContenu(StringEscapeUtils.escapeHtml4(page.getContenu()));
		page.setUri(StringEscapeUtils.escapeHtml4(page.getUri()));
		page.setStatus(StringEscapeUtils.escapeHtml4(page.getStatus()));
		// update the data structure
		int index = pages.indexOf(PageToUpdate);
		pages.set(index, page);
		saveDataToFile();
		return page;
	}

	private static List<Page> loadDataFromFile() {
		try {
			JsonNode node = jsonMapper.readTree(Paths.get(DB_FILE_PATH).toFile());
			JsonNode collection = node.get(COLLECTION_NAME);
			if (collection == null)
				return new ArrayList<Page>();
			return jsonMapper.readerForListOf(Page.class).readValue(node.get(COLLECTION_NAME));

		} catch (FileNotFoundException e) {
			return new ArrayList<Page>();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<Page>();
		}
	}

	private static void saveDataToFile() {
		try {

			// get all collections
			Path pathToDb = Paths.get(DB_FILE_PATH);
			if (!Files.exists(pathToDb)) {
				// write a new collection to the db file
				ObjectNode newCollection = jsonMapper.createObjectNode().putPOJO(COLLECTION_NAME, pages);
				jsonMapper.writeValue(pathToDb.toFile(), newCollection);
				return;

			}
			// get all collections
			JsonNode allCollections = jsonMapper.readTree(pathToDb.toFile());

			if (allCollections.has(COLLECTION_NAME)) {// remove current collection
				((ObjectNode) allCollections).remove(COLLECTION_NAME);
			}

			// create a new JsonNode and add it to allCollections
			String currentCollectionAsString = jsonMapper.writeValueAsString(pages);
			JsonNode updatedCollection = jsonMapper.readTree(currentCollectionAsString);
			((ObjectNode) allCollections).putPOJO(COLLECTION_NAME, updatedCollection);

			// write to the db file
			jsonMapper.writeValue(pathToDb.toFile(), allCollections);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
