package com.axelor.event.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.rpc.Context;


public class EventRegistrationServiceImpl implements EventRegistrationService {

	@Override
	public EventRegistration calculate(EventRegistration eventRegistration, Event event) {
		
		BigDecimal discountAmount = BigDecimal.ZERO;
		if(eventRegistration.getRegistrationDate() != null && event.getRegistrationClose() !=null ) {
			LocalDate registrationDate = eventRegistration.getRegistrationDate().toLocalDate();
			LocalDate registrationclose = event.getRegistrationClose();
			Long eventdays = ChronoUnit.DAYS.between(registrationDate, registrationclose);
			System.out.println(eventdays + "eventdayss:::");
			
			if(event.getDiscountsList() != null) {
				List<Discount> discountList = event.getDiscountsList();
				for(Discount discount : discountList) {
					if(discount.getBeforeDays() == eventdays.intValue()) {
						discountAmount = discount.getDiscountAmount();
						System.out.println("Discount Amount list::"+discountAmount);
						break;
					}	
					else {
						discountAmount = event.getEventFees();
						System.out.println("DiscountAmount==eventfee"+discountAmount);
					}
					
				}
			}
		
		}
		
		eventRegistration.setAmount(discountAmount);

		return eventRegistration;
	}

	@Override
	public Event getEvent(Context context) {
		Context parentContext = context.getParent();

		EventRegistration eventRegistration = context.asType(EventRegistration.class);
		Event event = eventRegistration.getEvent();

		if (parentContext != null && !parentContext.getContextClass().equals(Event.class)) {
			parentContext = parentContext.getParent();
		}

		if (parentContext != null && parentContext.getContextClass().equals(Event.class)) {
			event = parentContext.asType(Event.class);
		}

		return event;
	}

}
