package bookstore.controller;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;

/**
 * ToDo Zentrale Factory fuer Jackson ObjectMapper Konfiguration.
 *
 * Einsatz in der Pruefung:
 * - Diese Klasse verwenden, wenn Servlets JSON lesen oder schreiben muessen.
 * - ObjectMapper nicht in jeder Methode neu konfigurieren.
 * - In Servlets typischerweise einmal als Feld verwenden:
 *   private final ObjectMapper objectMapper = __ObjectMapperFactoryTemplate.createObjectMapper();
 */
public class __ObjectMapperFactoryTemplate {

	/**
	 * ToDo Erstellt einen ObjectMapper fuer JSON APIs.
	 *
	 * Der ObjectMapper ist die zentrale Jackson-Klasse fuer:
	 * - Java Objekt zu JSON schreiben
	 * - JSON zu Java Objekt lesen
	 */
	public static ObjectMapper createObjectMapper() {
		/* ToDo Neuer ObjectMapper. Ohne Konfiguration funktioniert JSON grundsaetzlich,
		aber manche Defaults sind fuer Web APIs unpraktisch.*/
		ObjectMapper objectMapper = new ObjectMapper();

		/* ToDo Felder mit null-Wert werden nicht ins JSON geschrieben.
		 Beispiel: subtitle == null wird im JSON ausgelassen.*/
		objectMapper.setDefaultPropertyInclusion(Include.NON_NULL);

		/* ToDo Unbekannte JSON-Felder beim Einlesen ignorieren.
		 Vorteil: Client darf mehr Felder senden, als die Java-Klasse kennt.
		 Ohne diese Einstellung kann Jackson beim Deserialisieren abbrechen.*/
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		/* ToDo JSON wird schoen formatiert ausgegeben.
		Fuer Pruefung und Debugging sehr angenehm. Fuer Produktion optional.*/
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		/* ToDo Java Datumswerte nicht als Timestamp schreiben.
		 Mit dieser Einstellung wird z. B. LocalDate als "2026-05-20" ausgegeben.*/
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		/* ToDo Unterstuetzung fuer java.time Klassen wie LocalDate, LocalDateTime usw.
		 Dafuer braucht das Projekt die Dependency jackson-datatype-jsr310 im pom.xml.*/
		objectMapper.registerModule(new JavaTimeModule());

		return objectMapper;
	}

	/**
	 * ToDo Kleine Demo-Methode, damit die Klasse fuer sich verstaendlich bleibt.
	 *
	 * Diese Methode brauchst du in einer echten Pruefungsloesung meistens nicht.
	 * Sie zeigt nur, wie der konfigurierte ObjectMapper JSON erzeugt.
	 */
	public static String demoJson() throws JsonProcessingException {
		ObjectMapper objectMapper = createObjectMapper();
		ExampleDto dto = new ExampleDto("Clean Code", null, LocalDate.of(2008, 8, 1));
		return objectMapper.writeValueAsString(dto);
	}

	/**
	 * ToDo Beispiel DTO fuer die Demo.
	 *
	 * Wichtig fuer Jackson:
	 * - Getter machen private Felder serialisierbar.
	 * - Fuer reines Schreiben reicht ein Konstruktor mit Parametern.
	 * - Fuer JSON zu Java Objekt wird meistens ein leerer Konstruktor benoetigt.
	 */
	private static class ExampleDto {
		private final String title;
		private final String subtitle;
		private final LocalDate publishedAt;

		private ExampleDto(String title, String subtitle, LocalDate publishedAt) {
			this.title = title;
			this.subtitle = subtitle;
			this.publishedAt = publishedAt;
		}

		public String getTitle() {
			return title;
		}

		public String getSubtitle() {
			return subtitle;
		}

		public LocalDate getPublishedAt() {
			return publishedAt;
		}
	}
}
