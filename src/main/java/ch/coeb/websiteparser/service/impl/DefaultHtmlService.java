package ch.coeb.websiteparser.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import ch.coeb.websiteparser.exception.ImportException;
import ch.coeb.websiteparser.exception.ResourceNotAvailableException;
import ch.coeb.websiteparser.service.HtmlService;

@Service
public class DefaultHtmlService implements HtmlService {

	@PostConstruct
	public void setUp() {
		HttpURLConnection.setFollowRedirects(false);
	}

	public String getHtml(URL url) {
		try {
			URLConnection connection = url.openConnection();

			if (connection instanceof HttpURLConnection) {
				HttpURLConnection httpUrlConnection = (HttpURLConnection) connection;
				if (httpUrlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					// TODO ceberle: implement proper exception handling
					throw new ResourceNotAvailableException(
							"Online resource not available for URL ["
									+ url.toString() + "]");
				}

			}
			String html = StreamUtils.copyToString(connection.getInputStream(),
					Charset.forName("UTF-8"));
			return html;
		} catch (IOException e) {
			throw new ImportException("Could not download HTML for URL ["
					+ url.toString() + "]", e);
		}
	}

}
