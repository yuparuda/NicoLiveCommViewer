package nicoPeople.handleName;

import java.util.HashMap;
import java.util.Map;

public class Roster { // for handle name
	private int broadcastId;
	private final String myselfId;
	private Map<String, String> cache;
	private final Storage storage; // implement later
	private boolean opend;

	public void register(String userId, boolean isEncrypted, String handleName) {
		if (this.opend) {
			this.cache.put(userId, handleName);
//			this.storage.put(userId, isEncrypted, handleName);
		} else {
			System.err.println("Not opend");
		}
	}

	public String selectHandleNameBy(String id, HandleNameType type)
			throws NoRosterCacheException {
		String hName = "";

		switch (type) {
		case LISTENER:
			if (this.cache == null) {
				throw new NoRosterCacheException(2001,
						"Selected Broadcast ID : " + this.broadcastId);
			} else {
				final String result = this.cache.get(id);

				if (result != null) {
					hName = result;
				}
				if (id.equals(myselfId)) {
					hName = "(" + hName + ")";
				}
			}
			break;

		case SYS_ADMIM:
			hName = "/";
			break;

		case BROADCASTER:
			hName = "*";
			break;

		default:
			System.err.println("Unknown Type : " + type);

		}
		return hName;
	}

	public void open() {
		this.opend = true;
		this.cache = new HashMap<>();
		
//		this.storage.open();
//		this.cache = this.storage.roster();
//		System.out.println("---- Roster From Local Storage ----\n  "
//				+ this.storage + "\n      " + " -- Cache -- \n     "
//				+ this.cache.toString());
	}

	public void close() {
//		this.storage.close();
		this.cache = null;
	}

	public Roster(int brdcstId, String myselfId) {
		this.broadcastId = brdcstId;
		this.myselfId = myselfId;
		this.storage = null;		// debugging
		this.opend = false;
	}

}
