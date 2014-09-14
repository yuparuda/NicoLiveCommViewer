package nicoStation;

import message.userLevel.reprezentation.Viewer;
import message.xmlLevel.CommentReceiver;
import nicoPeople.handleName.Roster;
import nicoStation.network.XmlClient;
import nicoStation.network.XmlServer;

// encapsulate an TCP(Socket) connection
public class Broadcast {
	private static final StatusAuthentication STATUS_ATH = new StatusAuthentication();
	public final String id; // for creating HTTP connection
	private XmlClient xmlClient;
	private final Roster roster; // to manage every handle name

	public void handleListenerComment(String userSession, Viewer viewer)
			throws InvalidBroadcastException {
		XmlServer xmlCommentProvider = STATUS_ATH.selectServerBy(this.id, userSession);
		if (xmlCommentProvider == null) {
			throw new InvalidBroadcastException(1001,
					" Selected Broadcast ID : " + this.id
							+ "\n User Session : " + userSession);
		} else {
			this.xmlClient.connectTo(xmlCommentProvider.address, new CommentReceiver(xmlCommentProvider.reqToStart(),
					viewer, this.roster));
		}
	}

	public void down() {
		this.xmlClient.shutDown();
		this.roster.close();
	}

	// sample input : http://live.nicovideo.jp/watch/lv176099114?ref=rtrec,,,
	// sample output : lv176099114
	// sample (for cruise) : http://live.nicovideo.jp/watch/lv176340286
	private static String extractBroadcastID(String sorce) {
		String id = "";
		int begin = sorce.indexOf("lv");
		if (begin == -1) {		// not exists
			id = "lv" + sorce;
		} else {
			int end = sorce.indexOf("?", begin);
			if (end == -1) {		// not exists
				end = sorce.length();
			}
			id = sorce.substring(begin, end);
		}
		return id;
	}

	private int integerId() {
		return Integer.parseInt(this.id.substring(2));
	}

	public Broadcast(String identifier) {
		this.id = extractBroadcastID(identifier);
		this.xmlClient = new XmlClient();
		this.roster = new Roster(integerId(), "testId");
		this.roster.open();
	}
}
