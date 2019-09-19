package com.axelor.event.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;

import com.axelor.apps.message.db.EmailAddress;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.repo.EmailAccountRepository;
import com.axelor.apps.message.db.repo.TemplateRepository;
import com.axelor.apps.message.service.MessageService;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;


public class EventController {
	
	@Inject MessageService messageService;

	public void setTotalEntry(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		List<EventRegistration> eventRegstrationsList = event.getEventRegistrationList();
	
		event.setTotalEntry(eventRegstrationsList.size());
		response.setValues(event);

	}
	
	public void calculateAmountCollected(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		List<EventRegistration> eventRegistrationList = event.getEventRegistrationList();
		BigDecimal amount = BigDecimal.ZERO;
		if(eventRegistrationList != null) {
		for(EventRegistration eventRegistered : eventRegistrationList) {
			amount = amount.add(eventRegistered.getAmount());
		}
		}
		event.setAmountCollected(amount);
		response.setValues(event);
		
	}
	
	public void calculateTotalDiscount(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		BigDecimal totalDiscount = BigDecimal.ZERO;
		
		totalDiscount = (event.getEventFees().multiply(new BigDecimal(event.getTotalEntry()))).subtract(event.getAmountCollected());
		System.out.println(totalDiscount+"TD");
		
		event.setTotalDiscount(totalDiscount);
		response.setValues(event);
		
	}
	
	public void sendEmail(ActionRequest request, ActionResponse response) {
		
		Event event = request.getContext().asType(Event.class);
		List<EventRegistration> eventRegstrationsList = event.getEventRegistrationList();
		Set<EmailAddress> emailAddressSet = new HashSet<EmailAddress>();
		if(eventRegstrationsList != null) {
			
			for(EventRegistration eventRegistered : eventRegstrationsList) {
				
				if(eventRegistered.getEmail() != null && !eventRegistered.getIsEmailSent()) {
					EmailAddress emailAddress = new EmailAddress();
					emailAddress.setAddress(eventRegistered.getEmail());
					emailAddressSet.add(emailAddress);
					
					eventRegistered.setIsEmailSent(true);
					
				}
				
				
			}
			if(!emailAddressSet.isEmpty()) {
			    Message message = new Message();
		        message.setMailAccount(Beans.get(EmailAccountRepository.class).all().fetchOne());
		        message.setToEmailAddressSet(emailAddressSet);
		        message.setSubject("Regarding event Registration");
		        message.setContent("Your Event has been registered successfully ");
		        try {
					messageService.sendByEmail(message);
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (AxelorException e) {
					e.printStackTrace();
				}
		        
		        response.setFlash("Email Sent successfully");
			}
			else {
				response.setNotify("No recipents found!!");
			}
		}
		
		
		
	}
	
	

}
