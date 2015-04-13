package jmo.logging;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import jmo.serialization.Serializer;

//TODO Try to remove the exception handling from toString();
public class Log implements Serializable {
	private static final long serialVersionUID = 5259203340243633671L;

	private Date date;
	private String entry;
	private ArrayList<LogTag> tags;

	// -------------------------Constructors-------------------------
	public Log() {
		this.entry = "";
		this.date = new Date();
		this.tags = new ArrayList<LogTag>();
	}

	public Log(String entry) {
		this();
		this.entry = entry;
	}

	public Log(String entry, LogTag... tags) {
		this(entry);
		this.tags.addAll(Arrays.asList(tags));
	}

	public Log(String entry, String... tags) {
		this(entry);
		for (String tagValue : tags) {
			this.tags.add(new LogTag(tagValue));
		}
	}

	public Log(Throwable t, String... tags) {
		this(throwableToString(t));
		for (String tagValue : tags) {
			this.tags.add(new LogTag(tagValue));
		}
	}

	public Log(Throwable t, LogTag... tags) {
		this(throwableToString(t));
		this.tags.addAll(Arrays.asList(tags));
	}

	// -------------------------Getters and Setters-------------------------
	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ArrayList<LogTag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<LogTag> tags) {
		this.tags = tags;
	}

	// -------------------------Private Static Methods -------------------------
	protected static String throwableToString(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	@Override
	public String toString() {
		try {
			return Serializer.serialize(this);
		} catch (Exception e) {
			return super.toString();
		}
	}
}
