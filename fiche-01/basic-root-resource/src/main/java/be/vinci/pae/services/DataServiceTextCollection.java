package be.vinci.pae.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import be.vinci.pae.domain.Text;

public class DataServiceTextCollection {
	private static final String DB_FILE_PATH = "textes.json";
	private static final String COLLECTION_NAME = "textes";
	private final static ObjectMapper jsonMapper = new ObjectMapper();

	private static List<Text> texts = loadDataFromFile();
	private static int id = 1;
	
	public static List<Text> getTexts() {
		return texts;
	}
	
	public static List<Text> getTexts(String level) {
		return texts.stream().filter(text -> text.getLevel().equals(level)).collect(Collectors.toList());
	}
	
	public static Text getTextId(int id) {
		return texts.stream().filter(text -> text.getId() == id).findAny().orElse(null);
	}
	
	public static Text addText(Text text) {
		text.setId(id);
		id++;
		text.setLevel(text.getLevel());
		// escape dangerous chars to protect against XSS attacks
		text.setContent(StringEscapeUtils.escapeHtml4(text.getContent()));
		texts.add(text);
		saveDataToFile();
		return text;
	}

	public static Text deleteTextId(int id) {
		Text textToDelete = getTextId(id);
		if (textToDelete == null)
			return null;
		texts.remove(texts.indexOf(textToDelete));
		saveDataToFile();
		return textToDelete;
	}

	public static Text updateTextId(int id, Text text) {
		Text textToUpdate = getTextId(id);
		if (textToUpdate == null)
			return null;
		texts.remove(texts.indexOf(textToUpdate));
		text.setId(textToUpdate.getId());
		texts.add(text);
		saveDataToFile();
		return text;
	}
	
	private static List<Text> loadDataFromFile() {
		try {
			JsonNode node = jsonMapper.readTree(Paths.get(DB_FILE_PATH).toFile());
			JsonNode collection = node.get(COLLECTION_NAME);
			if (collection == null)
				return new ArrayList<Text>();
			return jsonMapper.readerForListOf(Text.class).readValue(node.get(COLLECTION_NAME));

		} catch (FileNotFoundException e) {
			return new ArrayList<Text>();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<Text>();
		}
	}

	private static void saveDataToFile() {
		try {

			// get all collections
			Path pathToDb = Paths.get(DB_FILE_PATH);
			if (!Files.exists(pathToDb)) {
				// write a new collection to the db file
				ObjectNode newCollection = jsonMapper.createObjectNode().putPOJO(COLLECTION_NAME, texts);
				jsonMapper.writeValue(pathToDb.toFile(), newCollection);
				return;

			}
			// get all collections
			JsonNode allCollections = jsonMapper.readTree(pathToDb.toFile());

			if (allCollections.has(COLLECTION_NAME)) {// remove current collection
				((ObjectNode) allCollections).remove(COLLECTION_NAME);
			}

			// create a new JsonNode and add it to allCollections
			String currentCollectionAsString = jsonMapper.writeValueAsString(texts);
			JsonNode updatedCollection = jsonMapper.readTree(currentCollectionAsString);
			((ObjectNode) allCollections).putPOJO(COLLECTION_NAME, updatedCollection);

			// write to the db file
			jsonMapper.writeValue(pathToDb.toFile(), allCollections);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
