package fr.axin.gservices.gcal;


import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event.Reminders;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventReminder;

import fr.axin.gservices.auth.GoogleAuth;
import fr.axin.gservices.database.oracle.OracleJob;
import fr.axin.gservices.servlet.html.HTMLPageFactory;
import fr.axin.gservices.util.LogUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class Gcal {
	final int MAX_DAYS = 365;
    String name;
    static String username;
    String code;
    String ID;
    String startDate;
    String endDate;
    Calendar calendar;    
    HTMLPageFactory out;
    PrintWriter log;

    public Gcal(String n, String u, String c, String i, HTMLPageFactory o,
                PrintWriter l) throws MalformedURLException, IOException {
        name = n;
        username = u;
        code = c;
        ID = i;
        out = o;
        log = l;     
        calendar = GoogleAuth.getClient();                        
        fr.axin.gservices.util.LogUtils.log("Connecte a " + calendar.getServicePath(), out, log); 
        try {
        	String control = calendar.calendarList().get(i).execute().getId();
        }
        catch(Exception e) {
        	String error = "Erreur d'acces au calendrier, verifiez que l'agenda " + i + " est partage avec " + GoogleAuth.getClientId();        	
        	throw new IOException(error);
        }
    }

    public void setDateRange(String start, String end) {
        startDate = start;
        endDate = end;
    }

    public void addEvent(Gevent ev) throws IOException {
		
    	Event newEvent = new Event();    	
        
        newEvent.setSummary(ev.getTitle());       
        newEvent.setDescription(ev.getDescription());
        newEvent.setLocation(ev.getLocation());        
        newEvent.setColorId(ev.getColorId());     
                
        if(ev.isAllDay()) {        	
        	newEvent.setStart(new EventDateTime().setDate(DateTime.parseRfc3339(ev.getStartDate())));        
        	newEvent.setEnd(new EventDateTime().setDate(DateTime.parseRfc3339(ev.getEndDate())));
        }
        else {        	
        	newEvent.setStart(new EventDateTime().setDateTime(DateTime.parseRfc3339(ev.getStartDate())));        
        	newEvent.setEnd(new EventDateTime().setDateTime(DateTime.parseRfc3339(ev.getEndDate())));        	
        }       
                
        newEvent.setLocked(ev.isReadOnly());        
        
        Event.ExtendedProperties exProperties = new Event.ExtendedProperties();
        Map<String, String> properties = new HashMap<String, String>(); 
        properties.put("csType", ev.getType());
        properties.put("csKey", ev.getKey());
        properties.put("csDate", ev.getDdate());
        exProperties.setPrivate(properties);
        newEvent.setExtendedProperties(exProperties);
        
        if(ev.getReminderMethod() != null && ev.getReminderMinutes() > 0) {
        	Reminders reminders;
        	EventReminder reminder;
        	List<EventReminder> lReminders;
    	
        	reminders = new Reminders();  
        	reminder = new EventReminder();
        	lReminders = new ArrayList<EventReminder>();
    	
        	reminder.setMethod(ev.getReminderMethod());
        	reminder.setMinutes(ev.getReminderMinutes());
        	lReminders.add(reminder);
    	
        	reminders.setUseDefault(false);
        	reminders.setOverrides(lReminders);
        	newEvent.setReminders(reminders);
        }

        newEvent.setOrganizer(new Event.Organizer().setSelf(true));
        
        //UID
        /*
        SimpleDateFormat datePattern = new SimpleDateFormat("yyyyMMdd_HHmmss");        
        java.util.Date date = new java.util.Date();
        String eventUID = this.name + "_" + ev.getGUID() + "_" + datePattern.format(date); 
        */
        String eventUID = this.name + "_" + ev.getGUID();
        newEvent.setICalUID(eventUID);                 

        calendar.events().insert(ID, newEvent).execute();        
        fr.axin.gservices.util.LogUtils.log("Creation de " + newEvent.getICalUID() + " / " + newEvent.getSummary(), out, log);
        
        newEvent = null;
        
    }

    public void updateEvent(Gevent ev) throws IOException {    	    	
    	    	    	
    	Event currentEvent = calendar.events().get(ID, ev.getId()).execute();    	
    	
    	if(!currentEvent.getSummary().equals(ev.getTitle()))    			
    			currentEvent.setSummary(ev.getTitle());
    	
    	if(!currentEvent.getDescription().equals(ev.getDescription()))
    		currentEvent.setDescription(ev.getDescription());    	
    	
    	if(currentEvent.getLocation() != null) {
    		if(!currentEvent.getLocation().equals(ev.getLocation()))
    			currentEvent.setLocation(ev.getLocation());   
    	}
    	
    	if(!currentEvent.getColorId().equals(ev.getColorId()))
    		currentEvent.setColorId(ev.getColorId());    		    	

        if(currentEvent.getStatus().equals("cancelled"))
        	currentEvent.setStatus("confirmed");
        
        if(currentEvent.getLocked() != null) {
	        if(currentEvent.getLocked() != ev.isReadOnly())
	        	currentEvent.setLocked(ev.isReadOnly());
        }        
        
        if(ev.isAllDay()) {        	
        	currentEvent.setStart(new EventDateTime().setDate(DateTime.parseRfc3339(ev.getStartDate())));        
        	currentEvent.setEnd(new EventDateTime().setDate(DateTime.parseRfc3339(ev.getEndDate())));
        }
        else {        	
        	currentEvent.setStart(new EventDateTime().setDateTime(DateTime.parseRfc3339(ev.getStartDate())));        
        	currentEvent.setEnd(new EventDateTime().setDateTime(DateTime.parseRfc3339(ev.getEndDate())));        	
        }
        
        
        
        Event.ExtendedProperties exProperties = new Event.ExtendedProperties();
        Map<String, String> properties = new HashMap<String, String>(); 
        properties.put("csType", ev.getType());
        properties.put("csKey", ev.getKey());
        properties.put("csDate", ev.getDdate());
        exProperties.setPrivate(properties);
        currentEvent.setExtendedProperties(exProperties);
                              
        if(ev.getReminderMethod() != null && ev.getReminderMinutes() > 0) {        	
        	Reminders reminders;
        	EventReminder reminder;
        	List<EventReminder> lReminders;
    	
        	reminders = new Reminders();  
        	reminder = new EventReminder();
        	lReminders = new ArrayList<EventReminder>();
    	
        	reminder.setMethod(ev.getReminderMethod());
        	reminder.setMinutes(ev.getReminderMinutes());
        	lReminders.add(reminder);
    	
        	reminders.setUseDefault(false);
        	reminders.setOverrides(lReminders);
        	currentEvent.setReminders(reminders);
        }   
        
        Event updatedEvent = calendar.events().update(ID, ev.getId(), currentEvent).execute();
        
        LogUtils.log("Mise a jour de " + updatedEvent.getICalUID() + " / " + updatedEvent.getSummary(), out, log);        
    }
    
    public boolean cancelEvents(Date start, Date end) {    	    	    	
    	int nbDays = 0;
    	boolean next = true;
    	SimpleDateFormat datePattern = new SimpleDateFormat("dd/MM/yyyy");
    	com.google.api.services.calendar.model.Events feed;
    	List<String> exProperties = new ArrayList<String>();
    	exProperties.add("csDate=null");
    	
    	java.util.Calendar loopCalendar = java.util.Calendar.getInstance();
    	loopCalendar.setTime(start);    	
    	
    	try {
    	
	    	while(next) {	
	    		nbDays++;
	    		//LogUtils.log("\nTraitement du " + datePattern.format(loopCalendar.getTime()), out, log);    			    		  		    		
	    		
	    		exProperties.set(0, "csDate=" + datePattern.format(loopCalendar.getTime()));
	    		feed = calendar.events()
	    				.list(ID)
	    				.setShowDeleted(false)
	    				.setPrivateExtendedProperty(exProperties)
	    				.execute();
	    		
	    		for(Event entry : feed.getItems()) {
	    			//LogUtils.log("Evenement : " + entry.getICalUID(), out, log);
	    			if(entry.getICalUID().startsWith(this.name) && !entry.getStatus().equals("cancelled")) {
	    				LogUtils.log("Annulation de  " + entry.getICalUID() + " / " + entry.getSummary(), out, log);
	    				calendar.events().delete(ID, entry.getId()).execute();   	    				
	    			}
	    		}
	    		
	    		loopCalendar.add(java.util.Calendar.DATE, 1);
	    		
	    		//On sort de la boucle si on atteint la date de fin ou si on atteint la limite
	    		//de 365 jours, afin de limiter le nombre de requêtes.
	    		if(loopCalendar.getTime().compareTo(end) >= 0 || nbDays > MAX_DAYS)
	    			next = false;
	    		
	    	}     
	    	
    	}
    	catch(Exception e) {
    		LogUtils.log("Erreur lors de l'annulation des evenements.", out, log);
    		LogUtils.log(e.getMessage(), out, log);
    		return false;
    	}
       	
    	return true;
    }
    
    public boolean eventExists(Gevent ev) throws IOException {      	
    	com.google.api.services.calendar.model.Events feed = calendar.events()
    																 .list(ID)
    																 .setShowDeleted(true)
    																 .setICalUID(this.name + "_" + ev.getGUID())
    																 .execute();
    	for (Event entry : feed.getItems()) {    		
    		ev.setId(entry.getId());
    		return true;
    	}    	
    	return false;
    }
}
