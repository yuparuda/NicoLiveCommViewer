package top;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import myHome.TV;

public class Main {
	private static String BROADCAST_INFO;
	private static String USER_SESSION = "user_session=";

	private static void procUserInput() {
		try (BufferedReader r = new BufferedReader(new InputStreamReader(
				System.in));) {

			// Step 1 :
			System.out.println("--- Start ---");
			System.out.println("Login to the nico service on your WebBrowser");
			System.out.println("Next, Input a user session from cookie ( cookie name : user_session )");
			System.out.println("	   ! Sample User Session  : user_session_28386567_7255,,,");
			USER_SESSION += r.readLine();

			// Step 2:
			System.out.println("Input a target Broadcast information such as URL,,,");
			System.out
					.println("   ! Sample URL : http://live.nicovideo.jp/watch/lv176256837?ref=r,,,,");
			BROADCAST_INFO = r.readLine();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		procUserInput();
		TV tv = new TV();
		tv.watch(BROADCAST_INFO, USER_SESSION);
	}

}
