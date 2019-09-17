package com.axelor.event.module;

import com.axelor.app.AxelorModule;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.event.service.EventRegistrationServiceImpl;
import com.axelor.event.service.ImportEventRegistrationService;
import com.axelor.event.service.ImportEventRegistrationServiceImpl;

public class EventModule extends AxelorModule{

	@Override
	protected void configure() {
		bind(EventRegistrationService.class).to(EventRegistrationServiceImpl.class);
		bind(ImportEventRegistrationService.class).to(ImportEventRegistrationServiceImpl.class);
	}

	
}
