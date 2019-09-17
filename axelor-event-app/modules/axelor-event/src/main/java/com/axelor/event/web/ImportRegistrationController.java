package com.axelor.event.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.data.ImportException;
import com.axelor.data.ImportTask;
import com.axelor.data.Listener;
import com.axelor.data.csv.CSVImporter;
import com.axelor.db.Model;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Injector;


public class ImportRegistrationController {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
    private Injector injector;
	public void importRegistration(ActionRequest request, ActionResponse response) {
		
		Map<String, Object> metaFileMap = (Map<String, Object>) request.getContext().get("metaFile");
		System.out.println(metaFileMap);
		String fileName = metaFileMap.get("fileName").toString();
		System.out.println(fileName);
		

		CSVImporter importer = new CSVImporter("/axelor-event/src/main/resources/data/input-config.xml"); 
		

		final List<Model> records = new ArrayList<>();
        final Map<String, Object> context = new HashMap<>();
        
        importer.addListener(new Listener() { 
            @Override
            public void imported(Model bean) {
                log.info("Bean saved : {}(id={})",
                        bean.getClass().getSimpleName(),
                        bean.getId());
                records.add(bean);
            }

            @Override
            public void imported(Integer total, Integer success) {
                log.info("Record (total): " + total);
                log.info("Record (success): " + success);
            }

            @Override
            public void handle(Model bean, Exception e) {

            }


	
        });

        importer.run(new ImportTask() { 

            @Override
            public void configure() throws IOException {
                // provide input data
                input("EventRegistrations", new File("data/input/"+fileName)); 
            }

            @Override
            public boolean handle(ImportException exception) { 
                log.error("Import error : " + exception);
                return false;
            }

            public boolean handle(IOException e) { 
                log.error("IOException error : " + e);
                return true;
            }
        });
		
	}
	
	

}
