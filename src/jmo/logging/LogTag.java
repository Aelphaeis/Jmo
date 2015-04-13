package jmo.logging;

public class LogTag {

	public final static String ERROR = "ERROR";
	public final static String INFO = "INFO";

	String value;

	public LogTag() {

	}

	public LogTag(String tagValue) {
		this();
		this.value = tagValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
