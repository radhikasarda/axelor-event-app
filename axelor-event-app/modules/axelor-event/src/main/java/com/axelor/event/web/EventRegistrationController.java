package com.axelor.event.web;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class EventRegistrationController {

	@Inject EventRegistrationService eventService;
	public void validateDate(ActionRequest request, ActionResponse response) {

		EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);

		Event event = eventRegistration.getEvent();

		if (eventRegistration.getRegistrationDate().toLocalDate().isBefore(event.getRegistrationOpen())
				|| eventRegistration.getRegistrationDate().toLocalDate().isAfter(event.getRegistrationClose())) {
			response.setError("The Registration Date is incorrect");
		}
	}

	public void calculateAmount(ActionRequest request, ActionResponse response) {

		EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);

		Event event =eventRegistration.getEvent();
		
		eventRegistration=eventService.calculate(eventRegistration,event);

		response.setValues(eventRegistration);
	} 
}
