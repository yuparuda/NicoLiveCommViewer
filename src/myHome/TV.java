package myHome;

import nicoStation.Broadcast;
import nicoStation.InvalidBroadcastException;
import message.userLevel.CommentFactory;
import message.userLevel.reprezentation.Console;
import message.userLevel.reprezentation.Viewer;

// to connect to the Broadcast
public class TV {
	private Viewer cmmViewer;
	private Broadcast targetBcast;

	public void watch(String BroadcastURL, String userSession) {
		this.targetBcast = new Broadcast(BroadcastURL);
		try {
			this.targetBcast.handleListenerComment(userSession, this.cmmViewer);
		} catch (InvalidBroadcastException e) {
			e.printStackTrace();
		}
	}

	public void stopToWatch() {
		this.targetBcast.down();
	}

	public void change(Viewer newOne) {
		this.cmmViewer = newOne;
	}

	public TV() {
		change(new Console()); // default setting
	}

	public TV(Viewer v) {
		change(v);
	}

}
