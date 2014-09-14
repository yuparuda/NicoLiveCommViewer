package nicoPeople.handleName;

import java.util.Map;

// bad code
abstract class Storage {

	int broadcastId;		// no need?
	
	abstract void open();
	abstract void close();

	abstract void put(String uId, boolean isEncryptedId, String hName);
	abstract Map<String, String> roster();

	Storage(int brdcId) {
		this.broadcastId = brdcId;
	}

}
