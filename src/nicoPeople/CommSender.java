package nicoPeople;

import nicoPeople.handleName.NoRosterCacheException;
import nicoPeople.handleName.Roster;
import nicoPeople.handleName.HandleNameType;

public class CommSender {
	public final String id; // limit digit = 8?
	private String handleName = "";
	public final HandleNameType type;
	final String grade;
	final boolean isAnonymity;
	
	public String handleName() {
		return this.handleName;
	}
	
	public void update(String newName, Roster r) {
		this.handleName = newName;
		r.register(id, isAnonymity, handleName);
	}

	public void initHandleName(Roster r) {
		try {
			this.handleName = r.selectHandleNameBy(id, type);
		} catch (NoRosterCacheException e) {
			e.printStackTrace();
		} 
	}

	public CommSender(String id, HandleNameType type, String grade,
			boolean isAnonimous) {
		this.id = id;
		this.type = type;
		this.grade = grade;
		this.isAnonymity = isAnonimous;
	}

}
