package com.axelor.event.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

import java.time.temporal.ChronoUnit;
import java.time.temporal.ChronoUnit.*;
public class DiscountController {

	public void calculateDiscountAmount(ActionRequest request, ActionResponse response) {
		
		Discount discount=request.getContext().asType(Discount.class);
		Event event= request.getContext().getParent().asType(Event.class);
		BigDecimal eventfee = BigDecimal.ZERO;
		BigDecimal discountAmount =  BigDecimal.ZERO;
		if(event.getEventFees()!=null) {
			eventfee=event.getEventFees();
			discountAmount = (discount.getDiscountPercent().multiply(eventfee)).divide(new BigDecimal(100));
		} 
		discount.setDiscountAmount(discountAmount);
		response.setValues(discount);
	}
	
	public void validateDuration(ActionRequest request, ActionResponse response) {
		Discount discount=request.getContext().asType(Discount.class);
		Event event= request.getContext().getParent().asType(Event.class);
		
		Long beforeDays = discount.getBeforeDays().longValue();
		LocalDate registrationstart = event.getRegistrationOpen().now();
		LocalDate registrationclose = event.getRegistrationClose().now();
		
		
		Long eventdays = ChronoUnit.DAYS.between(registrationstart, registrationclose);
		if(beforeDays > eventdays) {
			response.setError("Incorrect Before Days");
		}
				
	} 
}
