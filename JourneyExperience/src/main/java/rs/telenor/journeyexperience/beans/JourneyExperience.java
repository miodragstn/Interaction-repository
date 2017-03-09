package rs.telenor.journeyexperience.beans;

import java.util.ArrayList;

public class JourneyExperience {
	String brand;
	String mobileNumber;
	String journeyName;
	String channel;
	ArrayList<JourneyInteraction> interactions;
	
	public JourneyExperience() {
		interactions = new ArrayList<JourneyInteraction>();
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getJourneyName() {
		return journeyName;
	}

	public void setJourneyName(String journeyName) {
		this.journeyName = journeyName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public ArrayList<JourneyInteraction> getInteractions() {
		return interactions;
	}

	public void setInteractions(ArrayList<JourneyInteraction> interactions) {
		this.interactions = interactions;
	}
	
	
}
