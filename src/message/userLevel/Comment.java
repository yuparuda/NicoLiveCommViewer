package message.userLevel;

import message.userLevel.reprezentation.Viewer;
import nicoPeople.CommSender;
import nicoPeople.handleName.Roster;
import nicoPeople.handleName.HandleNameType;

public class Comment {
	static final int DEFAULT_NO = -1;
	static final String FINISHED_BROADCAST = "/disconnect";
	final int no;
	final String text;
	private final CommSender sender;

	private String startWith(char c) {
		int start = this.text.indexOf(c);
		if (start != -1) {
			return this.text.substring(start + 1, this.text.length());
		}
		return null;
	}

	private String extractHandleName() {
		String name = this.startWith('@');
		if (name != null) {
			return name;
		}
		return this.startWith('＠');
	}

	public boolean finishedBroadcast() {
		return this.text.equals(FINISHED_BROADCAST);
	}

	public void updateHandleNameTo(Roster r) {
		if (sender.type != HandleNameType.LISTENER) {
			return;
		}
		String newHName = extractHandleName();
		if (newHName != null) {
			sender.update(newHName, r);
			System.out.println("! Updated a Handle Name : " + newHName);
		}
	}

	public void showContentOn(Viewer v, Roster r) {
		this.sender.initHandleName(r);
		v.display(this.sender.handleName(), text);
	}

	Comment(String text, CommSender writter) {
		this.no = 1111;	// sample
		this.text = text;
		this.sender = writter;
	}
}
