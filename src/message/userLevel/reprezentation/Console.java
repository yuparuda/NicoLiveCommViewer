package message.userLevel.reprezentation;


public class Console extends Viewer {
	
	@Override
	public void display(String writterName, String comment) {
		System.out.println(String.format(" [ %11.11s ] %-40.38s ", writterName,
				comment));
	}
}
