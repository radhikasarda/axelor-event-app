package com.axelor.event.web;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

import wslite.json.JSONObject;


public class ImportRegistrationController {
	
	public void importRegistration(ActionRequest request, ActionResponse response) {
		
	
		 BufferedReader br = null;
	        String line = "";
	        String cvsSplitBy = ";";
	        LinkedHashMap<String, Object> map =
	                (LinkedHashMap<String, Object>) request.getContext().get("metaFile");
	        String jsonString = new JSONObject(map).toString();
		 try {

	            br = new BufferedReader(new FileReader(jsonString));
	            while ((line = br.readLine()) != null) {

	                // use comma as separator
	                String[] eventRegistration = line.split(cvsSplitBy);
	                System.out.println(eventRegistration);

	            }

	        }
		 
		 catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }

		    
		
		
	}
	
	

}
