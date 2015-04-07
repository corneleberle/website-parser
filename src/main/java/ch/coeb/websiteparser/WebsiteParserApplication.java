package ch.coeb.websiteparser;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.coeb.websiteparser.exception.ResourceNotAvailableException;
import ch.coeb.websiteparser.service.HtmlService;
import ch.coeb.websiteparser.service.ParserService;
import ch.coeb.websiteparser.service.UrlFileReaderService;

@SpringBootApplication
public class WebsiteParserApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WebsiteParserApplication.class);

	@Autowired
	private UrlFileReaderService urlFileReader;

	@Autowired
	private HtmlService htmlService;

	@Autowired
	private ParserService parserService;

	public static void main(String[] args) {
		if (args == null || args.length != 2) {
			System.out.println("Wrong arguments given");
			System.out.println("Usage: website-parser fileWithUrls outputFile");
			return;
		}

		SpringApplication.run(WebsiteParserApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length < 2) {
			return;
		}

		List<String> urls = urlFileReader.readUrlsFromFile(args[0]);

		Writer writer = null;
		CSVPrinter printer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(args[1]),
					Charset.forName("Latin1"));

			printer = new CSVPrinter(writer, CSVFormat.EXCEL.withDelimiter(';'));

			for (String url : urls) {
				List<String> csvColumns = new ArrayList<String>();
				csvColumns.add(url);

				try {
					String html = htmlService.getHtml(new URL(url));
					parserService.parseHtml(html, csvColumns);
				} catch (ResourceNotAvailableException e) {
					LOGGER.warn("URL [{}] not valid", url);
				}

				printer.printRecord(csvColumns);
			}
		} finally {
			if (printer != null) {
				printer.close();
			}
			if (writer != null) {
				writer.close();
			}
		}

	}
}
