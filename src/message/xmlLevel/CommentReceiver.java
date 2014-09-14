package message.xmlLevel;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.Charset;

import javax.xml.stream.XMLStreamException;

import nicoPeople.handleName.Roster;
import top.Main;
import message.userLevel.Comment;
import message.userLevel.CommentFactory;
import message.userLevel.reprezentation.Viewer;

public class CommentReceiver extends IOHandler {
	private static final int BFF_SIZE = 102400;

	private final CommentFactory cFactory;
	private final Roster roster;
	private final Viewer viewer;
	
	static final Charset CHARSET = Charset.forName("UTF-8");
	static final String COMMENT_DELIMITER = String.valueOf((char) 0);
	private boolean dontReceiveComment;

	private static ByteBuffer createIOBuffer(String req) {
		ByteBuffer bff = ByteBuffer.allocate(BFF_SIZE);
		byte[] byteMessage = req.getBytes();
		bff.put(byteMessage);
		bff.flip();
		return bff;
	}

	// think about any data. later,,,
	private void xmlToComment() {
		String xml = CHARSET.decode(this.mssgBox).toString();
		String[] nCommentXmls = xml.split(COMMENT_DELIMITER);

		for (String s : nCommentXmls) {
			Comment c = this.cFactory.parse(s);

			if (c != null) {
				if (c.finishedBroadcast()) {
					this.dontReceiveComment = true;
				}
				c.showContentOn(viewer, roster);
				c.updateHandleNameTo(roster);
			}
		}
		this.mssgBox.clear();
	}

	@Override
	public int procResponse() {
		if (this.dontReceiveComment) {
			return END;
		} else {
			xmlToComment();
		}
		
		if (this.dontReceiveComment) {
			return END;
		}
		return SelectionKey.OP_READ;
	}

	public CommentReceiver(String requestXML, Viewer viewer, Roster r) {
		super(createIOBuffer(requestXML));
		this.viewer = viewer;
		this.cFactory = new CommentFactory();
		this.roster = r;
	}

}
