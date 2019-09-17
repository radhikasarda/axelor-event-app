package com.axelor.event.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.rpc.Context;

public class EventRegistrationServiceImpl implements EventRegistrationService {

	@Override
	public EventRegistration calculate(EventRegistration eventRegistration, Event event) {

		BigDecimal discountAmount = BigDecimal.ZERO;
		List<BigDecimal> discounts = new ArrayList<BigDecimal>();
		if (eventRegistration.getRegistrationDate() != null && event.getRegistrationClose() != null) {
			LocalDate registrationDate = eventRegistration.getRegistrationDate().toLocalDate();
			LocalDate registrationclose = event.getRegistrationClose();
			Long eventdays = ChronoUnit.DAYS.between(registrationDate, registrationclose);

			if (event.getDiscountsList() != null) {
				List<Discount> discountList = event.getDiscountsList();
				for (Discount discount : discountList) {
					if (discount.getBeforeDays() <= eventdays.intValue()) {

						discounts.add(discount.getDiscountAmount());

					}

				}
				if (discounts.isEmpty()) {

					discountAmount = event.getEventFees();

				} else {
					// finding the max discount
					BigDecimal max = discounts.get(0);
					for (BigDecimal x : discounts) {
						if (x.compareTo(max) == 1)
							max = x;
					}
					BigDecimal maxDiscount = discounts.get(discounts.indexOf(max));
					discountAmount = event.getEventFees().subtract(maxDiscount);
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
