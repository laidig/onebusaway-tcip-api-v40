package org.onebusaway.tcip.adapters;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;

/*
 * NOT CURRENTLY USED BY THIS PROJECT.  Here for reference purposes.
 */

@SuppressWarnings("unused")
public class Utility {
	JAXBContext jc;
	DatatypeFactory df = null;
	Schema schema = null;
	// Fill in these with the relevant information. It will change based upon
	// the version of the spec used
	String xmlPath = "src/TCIP_4_0_0_Final.xsd";
	String schemaName = "tcip_final_4_0_0";

	// public static void main(String[] args) {
	//
	// }
	public String getJSONFromObject(Object o, boolean format) {
		String json = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			mapper.setDateFormat(df);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			// mapper.enable(SerializationFeature.USE_ANNOTATIONS, true);
			// mapper.setAnnotationIntrospector(new );
			mapper.setSerializationInclusion(Include.NON_NULL);
			// mapper.getSerializationConfig().enable(SerializationConfig.Feature.WRAP_ROOT_VALUE);
			json = mapper.writeValueAsString(o);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getXMLFromObject(Object o, boolean format) {
		StringWriter wrtr = new StringWriter();
		String xml = null;

		try {
			jc = JAXBContext.newInstance(schemaName);
			Marshaller m = jc.createMarshaller();
			m.setProperty("jaxb.formatted.output", new Boolean(format));
			m.marshal(o, wrtr);
			xml = wrtr.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return xml;
	}

	public void validateXML(String xml) {
		// System.out.println("VALIDATING:------------\n" + xml);

		String schemaFile = xmlPath;
		File file = new File(schemaFile);

		URL schemaUrl = null;
		try {
			schemaUrl = file.toURI().toURL();
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}

		System.out.println(xmlPath);
		System.out.println("SCHEMA FILE URL: " + schemaUrl);

		SchemaFactory sf = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			schema = sf.newSchema(schemaUrl);
		} catch (SAXException saxe) {
			schema = null;
			saxe.printStackTrace();
		}

		try {
			jc = JAXBContext.newInstance(schemaName);
			System.out.println("loaded xml");
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e1) {
			e1.printStackTrace();
		}

		Validator v = schema.newValidator();
		v.setErrorHandler(new ForgivingErrorHandler());
		try {
			v.validate(new StreamSource(
					new ByteArrayInputStream(xml.getBytes())));
			System.out.println("Done");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public class ForgivingErrorHandler implements ErrorHandler {

		public void warning(SAXParseException ex) {
			System.err.println(ex.getMessage());
		}

		public void error(SAXParseException ex) {
			System.err.println(ex.getMessage());
		}

		public void fatalError(SAXParseException ex) throws SAXException {
			throw ex;
		}
	}

	public String readFileAsString(String filePath) throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(6553400);
		System.out.println(filePath);
		InputStream is = this.getClass().getResourceAsStream(filePath);

		System.out.println(is);
		InputStreamReader isr = new InputStreamReader(is);

		BufferedReader reader = new BufferedReader(isr);
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}
}
