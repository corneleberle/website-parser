package ch.coeb.websiteparser.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ch.coeb.websiteparser.service.ParserService;

@Service
public class DefaultParserService implements ParserService {

	@Value("${parser.review.count:20}")
	private Integer numberOfReviews;

	private Pattern stylePattern = Pattern
			.compile(".*width:\\s*(\\d{0,3})%?;.*");

	private static final String EMPTY_STRING = "";

	@Override
	public void parseHtml(String html, List<String> fields) {
		Document document = Jsoup.parse(html);
		Elements reviewElements = document.select("#review dl");

		int count = 0;
		for (Element reviewElement : reviewElements) {
			addStars(reviewElement, fields);
			addText(reviewElement, fields);

			count++;
			if (count >= numberOfReviews) {
				break;
			}
		}
	}

	protected void addStars(Element reviewElement, List<String> fields) {
		Elements rateElements = reviewElement.select("li.currentRate");
		if (!rateElements.isEmpty()) {
			Element rateElement = rateElements.first();
			String style = rateElement.attr("style");
			Matcher matcher = stylePattern.matcher(style);
			if (matcher.matches()) {
				String starsInPercentString = matcher.group(1);
				Double stars = Double.valueOf(starsInPercentString) / 20.0;
				fields.add(stars.toString());
				return;
			}
		}

		fields.add(EMPTY_STRING);
	}

	private void addText(Element reviewElement, List<String> fields) {
		Elements descriptionElements = reviewElement.select("div.description");
		if (!descriptionElements.isEmpty()) {
			fields.add(descriptionElements.first().text());
			return;
		}

		fields.add(EMPTY_STRING);
	}

}
