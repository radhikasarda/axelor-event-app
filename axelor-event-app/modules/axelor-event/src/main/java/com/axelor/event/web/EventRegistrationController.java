package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.exception.IExceptionMessage;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.i18n.I18n;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.axelor.rpc.Context;

public class EventRegistrationController {

	@Inject
	EventRegistrationService eventRegistrationService;

	public void validateRegistrationDate(ActionRequest request, ActionResponse response) {
		Context context = request.getContext();
		
		EventRegistration eventRegistration = context.asType(EventRegistration.class);
	
		Event event = eventRegistrationService.getEvent(context);

		if (event != null && eventRegistration.getRegistrationDate()!=null &&  event.getRegistrationOpen() != null && event.getRegistrationClose() != null) {
			if (eventRegistration.getRegistrationDate().toLocalDate().isBefore(event.getRegistrationOpen())
					|| eventRegistration.getRegistrationDate().toLocalDate().isAfter(event.getRegistrationClose())) {
				//response.setError("The Registration Date is incorrect");
				response.setError(I18n.get(IExceptionMessage.REGISTRATION_DATE_INCORRECT));
			}
		}
	}

	public void calculateAmount(ActionRequest request, ActionResponse response) {
		Context context = request.getContext();

		EventRegistration eventRegistration = context.asType(EventRegistration.class);

		Event event = eventRegistrationService.getEvent(context);
		
		if(event!=null) {
		eventRegistration = eventRegistrationService.calculate(eventRegistration, event);
		}
		response.setValues(eventRegistration);
	}

	public void validateCapacity(ActionRequest request, ActionResponse response) {

		Context context = request.getContext();
		Event event = eventRegistrationService.getEvent(context);

		if (event.getCapacity() == 0 || event.getCapacity() <= event.getTotalEntry()) {
			response.setError("Registrations exceeds Capacity!!");
		}

	}
	
	public void setParentEvent(ActionRequest request, ActionResponse response) {
		
		EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);

	    if (eventRegistration.getEvent() == null) {
	      Event event = request.getContext().getParent().asType(Event.class);
	      eventRegistration.setEvent(event);
	      response.setValues(eventRegistration);
	    }
		
		
	}
}
