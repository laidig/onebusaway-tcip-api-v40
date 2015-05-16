package example;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class UTSPullOutRecord {
	String route;
	String depot;
	String runnumber;
	LocalDate date;
	LocalDateTime schedpo;
	LocalDateTime actualpo;
	LocalDateTime schedpi;
	LocalDateTime actualpi;
	String busnumber;
	double busmileage;
	String pass;
	String authid;
	
	public String getRoute() {
		return route;
	}


	public void setRoute(String route) {
		this.route = route;
	}


	public String getDepot() {
		return depot;
	}


	public void setDepot(String depot) {
		this.depot = depot;
	}


	public String getRunnumber() {
		return runnumber;
	}


	public void setRunnumber(String runnumber) {
		this.runnumber = runnumber;
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public LocalTime getSchedpo() {
		return schedpo;
	}


	public void setSchedpo(String intime) {

		
		this.schedpo = time;
	}


	public LocalTime getActualpo() {
		return actualpo;
	}


	public void setActualpo(String intime) {
		String[] parts= intime.split(":");
		String modifier= parts[1].split("[A-Z]")[1];
		int hour = Integer.parseInt(parts[0]);
		int minute = Integer.parseInt(parts[1]); 
		
		LocalTime time = new LocalTime(hour, minute);
		
		this.actualpo = time;
	}


	public LocalTime getSchedpi() {
		return schedpi;
	}


	public void setSchedpi(String intime) {
		String[] parts= intime.split(":");
		String modifier= parts[1].split("[A-Z]")[1];
		int hour = Integer.parseInt(parts[0]);
		int minute = Integer.parseInt(parts[1]); 
		
		LocalTime time = new LocalTime(hour, minute);
		
		this.schedpi = time;
	}


	public LocalTime getActualpi() {
		return actualpi;
	}


	public void setActualpi(String intime) {
		String[] parts= intime.split(":");
		String modifier= parts[1].split("[A-Z]")[1];
		int hour = Integer.parseInt(parts[0]);
		int minute = Integer.parseInt(parts[1]); 
		
		LocalTime time = new LocalTime(hour, minute);
		
		this.actualpi = time;
	}


	public String getBusnumber() {
		return busnumber;
	}


	public void setBusnumber(String busnumber) {
		this.busnumber = busnumber;
	}


	public double getBusmileage() {
		return busmileage;
	}


	public void setBusmileage(double busmileage) {
		this.busmileage = busmileage;
	}


	public String getPass() {
		return pass;
	}


	public void setPass(String pass) {
		this.pass = pass;
	}


	public String getAuthid() {
		return authid;
	}


	public void setAuthid(String authid) {
		this.authid = authid;
	}



	

	public UTSPullOutRecord(){
		
	}
	

}
