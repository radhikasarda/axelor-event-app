package com.axelor.event.web;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.event.db.Discount;
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
		List<Discount> discountList = event.getDiscountsList();
		BigDecimal discountAmount = BigDecimal.ZERO;
		if(discountList!=null) {
			for(Discount discount:discountList) {
				discountAmount =discountAmount.add(discount.getDiscountAmount());
				
			}
		}
		event.setTotalDiscount(discountAmount);
		response.setValues(event);
		
		
	}
	

}
