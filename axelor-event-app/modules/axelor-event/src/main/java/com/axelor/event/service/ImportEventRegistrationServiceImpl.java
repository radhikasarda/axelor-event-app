package com.axelor.event.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.data.ImportException;
import com.axelor.data.ImportTask;
import com.axelor.data.Importer;
import com.axelor.data.Listener;
import com.axelor.data.csv.CSVImporter;
import com.axelor.db.Model;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.google.common.io.Files;

public class ImportEventRegistrationServiceImpl implements ImportEventRegistrationService {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public void importCsv(MetaFile dataFile, Integer id) {
		File csvFile = null;
		File configFile = null;
		try {
			File tempDir = Files.createTempDir();
			csvFile = new File(tempDir, "registrations.csv");
			configFile = File.createTempFile("input-config", ".xml");
			InputStream bindFileInputStream = this.getClass().getResourceAsStream("/data/input-config.xml");
			FileOutputStream outputStream = new FileOutputStream(configFile);

			IOUtils.copy(bindFileInputStream, outputStream);

			Files.copy(MetaFiles.getPath(dataFile).toFile(), csvFile);

			System.out.println(csvFile);
			System.out.println(configFile.getAbsolutePath());
			
			 final Importer importer = new CSVImporter(configFile.getAbsolutePath()); 
			 
			 final List<Model> records = new ArrayList<>();
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
		                input("csvFile", new File("/tmp/1568699790885-0/registrations.csv")); 
		            }

		            @Override
		            public boolean handle(ImportException exception) { 
		                log.error("Import error : " + exception);
		                return false;
		            }

		            @Override
		            public boolean handle(IOException e) { 
		                log.error("IOException error : " + e);
		                return true;
		            }
		        });
			 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
