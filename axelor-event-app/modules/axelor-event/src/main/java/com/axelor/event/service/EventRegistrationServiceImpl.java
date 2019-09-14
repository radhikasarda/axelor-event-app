package com.axelor.event.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public class EventRegistrationServiceImpl implements EventRegistrationService{

	@Override
	public EventRegistration calculate(EventRegistration eventRegistration, Event event) {
		
		BigDecimal discountAmount = BigDecimal.ZERO;
		
		if(event.getDiscountsList() != null) {
		List<Discount> discountList = event.getDiscountsList();
		
		for(Discount discountListItem : discountList) {
			 
			discountAmount = discountAmount.add(discountListItem.getDiscountAmount());
			
		}
		}
		else {
			discountAmount =event.getEventFees();
		}
		System.out.println(discountAmount);
		eventRegistration.setAmount(discountAmount);
		return eventRegistration;
	}

}
