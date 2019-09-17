package com.axelor.event.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;


public class EventController {

	public void setTotalEntry(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		List<EventRegistration> eventRegstrationsList = event.getEventRegistrationList();
	
		event.setTotalEntry(eventRegstrationsList.size());
		response.setValues(event);

	}
	
	public void calculateAmountCollected(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		List<EventRegistration> eventRegistrationList = event.getEventRegistrationList();
		BigDecimal amount = BigDecimal.ZERO;
		if(eventRegistrationList != null) {
		for(EventRegistration eventRegistered : eventRegistrationList) {
			amount = amount.add(eventRegistered.getAmount());
		}
		}
		event.setAmountCollected(amount);
		response.setValues(event);
		
	}
	
	public void calculateTotalDiscount(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		BigDecimal totalDiscount = BigDecimal.ZERO;
		
		totalDiscount = (event.getEventFees().multiply(new BigDecimal(event.getTotalEntry()))).subtract(event.getAmountCollected());
		System.out.println(totalDiscount+"TD");
		
		event.setTotalDiscount(totalDiscount);
		response.setValues(event);
		
	}
	

	public void validateEventDate(ActionRequest request, ActionResponse response) {
		
		Event event = request.getContext().asType(Event.class);

		if(event.getStartDate() != null && event.getEndDate() != null) {
			
			if(event.getStartDate().isAfter(event.getEndDate()) || event.getEndDate().isBefore(event.getStartDate())) {
				
				response.setError("Invalid Start Or End Dates");
			}
			
		}
		
		if(event.getRegistrationOpen() !=null && event.getRegistrationClose() != null) {
			if(event.getRegistrationOpen().isAfter(event.getRegistrationClose()) || event.getRegistrationClose().isBefore(event.getRegistrationOpen())) {
				response.setError("Invalid Registration Open Or Close Dates");
			}
			
		}
		
		if(event.getStartDate() !=null && event.getRegistrationClose() != null) {
			if(event.getRegistrationClose().isAfter(event.getStartDate().toLocalDate()) || event.getRegistrationOpen().isAfter(event.getStartDate().toLocalDate())) {
				response.setError("Invalid Dates");
				
			}
		}
	
	}
	
	public void sendEmail(ActionRequest request, ActionResponse response) {
		
		Event event = request.getContext().asType(Event.class);
		List<EventRegistration> eventRegstrationsList = event.getEventRegistrationList();
		List<String> emailAdressList = new ArrayList<String>();
		if(eventRegstrationsList != null) {
			
			for(EventRegistration eventRegistered : eventRegstrationsList) {
				
				if(eventRegistered.getEmail() != null) {
					System.out.println(eventRegistered.getEmail());
					emailAdressList.add(eventRegistered.getEmail());
					
				}
				
				
			}
			if(!emailAdressList.isEmpty()) {
			
			
				System.out.println(emailAdressList);
			}
		}
		
		
		
	}
	
	

}
