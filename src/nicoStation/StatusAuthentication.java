package nicoStation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import nicoStation.network.XmlServer;

// purpose : approve a listener 
class StatusAuthentication {
	static final String BASIC_URL = "http://live.nicovideo.jp/api/getplayerstatus?v=";
	static final String CHARACTOR_CODE = "UTF-8";
	private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory
			.newInstance();
	private static final QName ATTRIBUTE_NAME = new QName("status");

	private static boolean canWatchBroadcast(StartElement getplaystatus) {
		boolean result = false;
		// StartElement startElement = event.asStartElement();
		Attribute attribute = getplaystatus.getAttributeByName(ATTRIBUTE_NAME);
		String status = attribute.getValue();
		switch (status) {

		case "ok":
			result = true;
			break;
		case "fail":
			System.err.println("Error : Broadcast Status is FAIL");
			break;
		default:
			System.err.println("Error : Unknown Status -> " + status);
			break;
		}
		return result;
	}

	// bad code, so improve this method
	private static XmlServer provider(XMLEventReader reader)
			throws XMLStreamException {
		String address = null;
		String port = null;
		String threadID = null;

		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();

			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().toString();

				switch (elementName) {
				case "getplayerstatus":
					if (!canWatchBroadcast(element)) {
						return null;
					}
					break;
				case "addr":
					address = reader.getElementText();
					break;
				case "port":
					port = reader.getElementText();
					break;
				case "thread":
					threadID = reader.getElementText();
					if (address != null && port != null && threadID != null) {
						return new XmlServer(new InetSocketAddress(address,
								Integer.parseInt(port)), threadID);
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}

	private static HttpURLConnection connection(String broadcastID,
			String userSession) {
		
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(BASIC_URL + broadcastID)
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Cookie", userSession);
			connection.setDoOutput(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}

	 XmlServer selectServerBy(String broadcastID, String userSession) {
		HttpURLConnection connection = connection(broadcastID, userSession);

		XMLEventReader reader = null;
		XmlServer provider = null;
		try (BufferedInputStream inStrm = new BufferedInputStream(
				connection.getInputStream())) {

			connection.connect();
			reader = INPUT_FACTORY.createXMLEventReader(inStrm, CHARACTOR_CODE);
			provider = provider(reader);

		} catch (IOException | XMLStreamException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
			connection.disconnect();
		}
		return provider;

	}

}
