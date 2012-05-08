package gmerg.control;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class GudmapSessionListener implements HttpSessionListener {

	public GudmapSessionListener() {

	}
	 
	public void sessionCreated(HttpSessionEvent event) {
//		System.out.println("session created : " + event.getSession().getId());
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		// get the destroying session...
		/*
		* nobody can reach user data after this point because session is invalidated already.
		* So, get the user data from session and save its logout information before losing it.
			HttpSession session = event.getSession();
		*/
//		System.out.println("session destroyed :" + session.getId() + " Logging out user...");
	}
	 
}
