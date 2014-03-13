package com.example.observationreunion;

import java.util.Comparator;

public class SpeakingTimeComparator implements Comparator<ParticipantAndSpeakingTime> {
		
		public int compare(ParticipantAndSpeakingTime participantandspeakingtime1, 
							ParticipantAndSpeakingTime participantandspeakingtime2) {
			int result =  participantandspeakingtime2.getValue().compareTo(participantandspeakingtime1.getValue());
			if(result == 0){
				result = participantandspeakingtime2.getName().compareTo(participantandspeakingtime1.getName());
			}
			return result;
		}


}

