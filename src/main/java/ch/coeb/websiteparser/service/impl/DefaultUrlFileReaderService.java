package ch.coeb.websiteparser.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import ch.coeb.websiteparser.exception.WebsiteParserException;
import ch.coeb.websiteparser.service.UrlFileReaderService;

@Service
public class DefaultUrlFileReaderService implements UrlFileReaderService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultUrlFileReaderService.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public List<String> readUrlsFromFile(String pathToFile) {
		Resource urlsResource = applicationContext.getResource(pathToFile);
		try {
			List<String> urls = FileUtils.readLines(urlsResource.getFile());
			LOGGER.info("Succefully read {} URL(s)", urls.size());
			return urls;
		} catch (IOException e) {
			throw new WebsiteParserException("Could not read URLs from file", e);
		}

	}

}
