package ca.mcgill.ecse321.treeple.service;

public class Token {
	private String tokenValue;
	private String email;
	private long unixTime;
	
	private static int threshold = 1000; //10000 seconds

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getUnixTime() {
		return unixTime;
	}

	public void setUnixTime(long unixTime) {
		this.unixTime = unixTime;
	}

	public static int getThreshold() {
		return threshold;
	}

	public static void setThreshold(int threshold) {
		Token.threshold = threshold;
	}

	public Token(String tokenValue, String email, long unixTime) {
		super();
		this.tokenValue = tokenValue;
		this.email = email;
		this.unixTime = unixTime;
	}

	
	
}
