package prj.resources.mgmt.services;

import java.security.SecureRandom;

public class RandomGenerator {

private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	
	private static final int RANDOM_STRING_LENGTH = 10;
	
	public String generatePwd() {
		StringBuffer randStr = new StringBuffer();
		for(int i=0; i<RANDOM_STRING_LENGTH; i++){
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}
	
	private int getRandomNumber(){
		int randomInt = 0;
		SecureRandom randomGenerator = new SecureRandom();
		randomInt = randomGenerator.nextInt(CHAR_LIST.length());
		if ((randomInt - 1) == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}		
	}
	
}