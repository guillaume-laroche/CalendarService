package fr.axin.gservices.gcal;

public class Gevent {
	private String title;
	private String description;
	private String startDate;
	private String endDate;
	private String location;
	private String gUID;
	private String id;
	private boolean allDay;
	private String colorId;
	private String reminderMethod;
	private int reminderMinutes;
	private String type;
	private String key;
	private String ddate;
	private boolean readOnly;


	public Gevent(String t, String d, String sD, String eD, String l,
			String gU, boolean aD, String cI, String rMet, int rMin,
			String t1, String k, String dd) {
		title = t;
		description = d;
		startDate = sD;
		endDate = eD;
		location = l;
		gUID = gU;
		allDay = aD;
		colorId = cI;
		reminderMethod = rMet;
		reminderMinutes = rMin;
		type = t1;
		key = k;
		ddate = dd;
		readOnly = true;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getDdate() {
		return ddate;
	}
	
	public String getType() {
		return type;
	}

	public String getKey() {
		return key;
	}	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReminderMethod() {
		return reminderMethod;
	}

	public int getReminderMinutes() {
		return reminderMinutes;
	}

	public String getColorId() {
		return colorId;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public Gevent() {

	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getLocation() {
		return location;
	}

	public String getGUID() {
		return gUID;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setGUID(String gUID) {
		this.gUID = gUID;
	}
}
