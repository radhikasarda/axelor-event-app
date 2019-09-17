package com.axelor.event.service;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.rpc.Context;

public interface EventRegistrationService {
	public EventRegistration calculate(EventRegistration eventRegistration, Event event);
	public Event getEvent(Context context);
}
