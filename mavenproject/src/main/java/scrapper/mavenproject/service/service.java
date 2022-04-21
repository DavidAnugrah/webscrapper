package scrapper.mavenproject.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import scrapper.mavenproject.model.Product;
import scrapper.mavenproject.scrapper.scrapper;
import scrapper.mavenproject.scrapper.scrapper.Category;

public class service {
	
	 private scrapper scrapper;

	    public service() {
	        scrapper = new scrapper();
	    }

	
	  public String downloadlist(Category category, int count) throws JsonGenerationException, JsonMappingException, IOException{
	        String filename = category.getName()
	                + "_" + System.currentTimeMillis() + ".csv";
	        List<Product> products = scrapper.extractProductList(category, count);

	        CsvMapper csvMapper = new CsvMapper();
	        csvMapper.enable(Feature.IGNORE_UNKNOWN);
	        csvMapper.addMixIn(Product.class, Product.ProductFormat.class);
	        CsvSchema schema = csvMapper.schemaFor(Product.class).withHeader();

	      
	            File file = new File(filename);
	            csvMapper.writer(schema).writeValue(file, products);
	            return filename;
	       
			
	    }
}
