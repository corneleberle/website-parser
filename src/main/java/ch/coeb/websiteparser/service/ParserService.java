package ch.coeb.websiteparser.service;

import java.util.List;

public interface ParserService {

	void parseHtml(String html, List<String> fields);

}
