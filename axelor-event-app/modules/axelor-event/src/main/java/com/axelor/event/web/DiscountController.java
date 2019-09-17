package com.axelor.event.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;

public class DiscountController {

	public void calculateDiscountAmount(ActionRequest request, ActionResponse response) {

		Context context = request.getContext();
		Discount discount = context.asType(Discount.class);

		BigDecimal eventfee = BigDecimal.ZERO;
		BigDecimal discountAmount = BigDecimal.ZERO;

		if (context.getParent() != null && context.getParent().getContextClass().equals(Event.class)) {
			Event event = context.getParent().asType(Event.class);

			if (event.getEventFees() != null) {
				eventfee = event.getEventFees();
				discountAmount = (discount.getDiscountPercent().multiply(eventfee)).divide(new BigDecimal(100));
			}

		}
		discount.setDiscountAmount(discountAmount);
		response.setValues(discount);

	}

	public void validateDuration(ActionRequest request, ActionResponse response) {
		Context context = request.getContext();
		Discount discount = context.asType(Discount.class);
		if (context.getParent() != null && context.getParent().getContextClass().equals(Event.class)) {
			Event event = context.getParent().asType(Event.class);

			if (event != null && event.getRegistrationOpen() != null && event.getRegistrationOpen() != null) {
				Long beforeDays = discount.getBeforeDays().longValue();
				LocalDate registrationstart = event.getRegistrationOpen();
				LocalDate registrationclose = event.getRegistrationClose();

				Long eventdays = ChronoUnit.DAYS.between(registrationstart, registrationclose);
				System.out.println(eventdays + "eventdayss:::");
				if (beforeDays > eventdays) {
					System.out.println("inside before greater");
					response.setError("Incorrect Before Days");
				}

			}
		}
	}
}
