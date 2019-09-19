package com.axelor.csv.script;

import java.util.Map;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public class ImportEventRegistration {
	
	public Object ImportInvoice(Object bean, Map<String, Object> values) {
	    assert bean instanceof EventRegistration ;

	    int successfullImports = 0;
	    int unsuccessfullImports = 0;
	    EventRegistration eventRegistration = (EventRegistration) bean;
	    Event event = eventRegistration.getEvent();
	    if (event != null && eventRegistration.getRegistrationDate()!=null &&  event.getRegistrationOpen() != null && event.getRegistrationClose() != null) {
			if (eventRegistration.getRegistrationDate().toLocalDate().isBefore(event.getRegistrationOpen())
					|| eventRegistration.getRegistrationDate().toLocalDate().isAfter(event.getRegistrationClose())) {
				//response.setError("The Registration Date is incorrect");
				unsuccessfullImports++;
				 
			}
			else successfullImports++;
			
		}
		
	    if(successfullImports > 0) {
	    	return eventRegistration;
	    }
	    else return null;
	  
	   
	  }
}
