package rs.telenor.journeyexperience;

import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		String message = JourneyExperienceManager.getJourneyExperienceMessage(10898009L);
		System.out.println(message);
	}

}
