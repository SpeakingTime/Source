package com.example.observationreunion;

import java.util.Comparator;

public class SpeakingTimeComparator2 implements Comparator<ParticipantAndSpeakingTime> {
	
	public int compare(ParticipantAndSpeakingTime participantandspeakingtime1, 
						ParticipantAndSpeakingTime participantandspeakingtime2) {
		int result =  participantandspeakingtime1.getValue().compareTo(participantandspeakingtime2.getValue());
		if(result == 0){
			result = participantandspeakingtime1.getName().compareTo(participantandspeakingtime2.getName());
		}
		return result;
	}


}
