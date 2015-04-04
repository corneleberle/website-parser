package ch.coeb.websiteparser.service;

import java.util.List;

public interface UrlFileReaderService {

	List<String> readUrlsFromFile(String pathToFile);

}
