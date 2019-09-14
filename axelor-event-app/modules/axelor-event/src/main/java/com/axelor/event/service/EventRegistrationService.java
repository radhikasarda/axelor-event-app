package com.axelor.event.service;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public interface EventRegistrationService {
	public EventRegistration calculate(EventRegistration eventRegistration, Event event);
}
