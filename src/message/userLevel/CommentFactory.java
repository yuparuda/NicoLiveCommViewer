package message.userLevel;


import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import nicoPeople.CommSender;
import nicoPeople.handleName.HandleNameType;

public class CommentFactory {
	private final XMLInputFactory inFactory = XMLInputFactory.newInstance();

	private static final QName NO = new QName("no");
	private static final QName MAIL = new QName("mail");
	private static final QName LOCALE = new QName("locale");
	private static final QName USER_ID = new QName("user_id");
	private static final QName PREMIUM = new QName("premium");
	private static final QName ANONIMITY = new QName("anonymity");

	private String userId(StartElement element) {
		Attribute attr = element.getAttributeByName(USER_ID);
		if (attr != null) {
			return attr.getValue();
		}
		return null;
	}

	static final String ANONIMOUS = "1";
	private boolean isAnonymity(StartElement element) {
		Attribute attr = element.getAttributeByName(ANONIMITY);
		// String userId = null;
		if (attr != null && attr.getValue().equals(ANONIMOUS)) {
			return true;
		}
		return false;
	}

	// PREMIUM
	private String userGrade(StartElement element) {
		Attribute attr = element.getAttributeByName(PREMIUM);
		// String userId = null;
		if (attr != null) {
			return attr.getValue();
		}
		return null;
	}
	
	private HandleNameType judgeType() {
		// for debugging
		return HandleNameType.LISTENER;
	}

	private Comment findOutComment(XMLEventReader reader) throws XMLStreamException {
		Comment newComment = null;

		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();

			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().toString();

				switch (elementName) {
				case "chat":
					// writer
					CommSender user = new CommSender(this.userId(element),judgeType(),
							 this.userGrade(element),
							this.isAnonymity(element));

					newComment = new Comment(reader.getElementText(), user);
					break;

				default:
					break;
				}
			}
		}
		return newComment;
	}

	public Comment parse(String xml) {
		XMLEventReader xmlReader = null;
		Comment newComment = null;
		try (StringReader stringReader = new StringReader(xml)) {
			xmlReader = inFactory.createXMLEventReader(stringReader);
			newComment = this.findOutComment(xmlReader);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} finally {
			if (xmlReader != null) {
				try {
					xmlReader.close();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		}
		return newComment;
	}

}
