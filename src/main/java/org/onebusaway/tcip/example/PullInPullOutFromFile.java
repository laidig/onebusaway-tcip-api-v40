package example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

import javax.swing.JFileChooser;
import javax.xml.bind.JAXBContext;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.validation.Schema;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseChar;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.cellprocessor.joda.*;

import tcip_final_4_0_0.CPTOperatorIden;
import tcip_final_4_0_0.CPTSubscriptionHeader;
import tcip_final_4_0_0.CPTTransitFacilityIden;
import tcip_final_4_0_0.CPTVehicleIden;
import tcip_final_4_0_0.ObaSchPullOutList;
import tcip_final_4_0_0.SCHBlockIden;
import tcip_final_4_0_0.SCHOperatorAssignment.DayTypes;
import tcip_final_4_0_0.SCHPullInOutInfo;
import tcip_final_4_0_0.SCHRunIden;


public class PullInPullOutFromFile {
	JAXBContext jc;
	DatatypeFactory df = null;
	Schema schema = null;

	public static void main(String[] args) throws IOException {

		UTSPullOutRecord po = new UTSPullOutRecord();
		parseUTSFileToBeanList();

		//		ObaSchPullOutList pullOuts = makePullOuts();
		//
		//		ObjectFactory f = new ObjectFactory();
		//		JAXBElement<ObaSchPullOutList> pullOutListElement = f
		//				.createObaSchPullOutList(pullOuts);
		//		Marshaller m = JAXBContext.newInstance(ObjectFactory.class)
		//				.createMarshaller();
		//		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		//
		//		StringWriter writer = new StringWriter();
		//
		//		m.marshal(pullOutListElement, writer);
		//		String xml = writer.toString();
		//
		//		JaxbAnnotationModule module = new JaxbAnnotationModule();
		//		ObjectMapper om = new ObjectMapper();
		//		om.registerModule(module);
		//		om.setSerializationInclusion(Include.NON_NULL);
		//		String s = om.writeValueAsString(pullOuts);
		//
		//		System.out.println(s);
	}




	// CcReportPullOuts meets the description, but is trip and not
	// block-centric. D'oh!
	public static  ObaSchPullOutList makePullOuts() {
		//load file,

		//iterate over records


		LocalTime beginTime = new LocalTime("04:59");
		DateTime beginDateTime = new DateTime("2011-06-01T04:59");
		DateTime deactivationTime = new DateTime("2040-01-04T23:59");
		LocalTime endTime = new LocalTime("00:49");
		DateTime endDateTime = new DateTime("2011-06-02T00:49");

		LocalTime aDifferentTime = new LocalTime("19:00:00");
		LocalDate aDay = new LocalDate("2011-06-01");
		// LocalDate anotherDay = new LocalDate("2011-06-02");

		CPTSubscriptionHeader subHeader = new CPTSubscriptionHeader();
		subHeader.setRequestedType("Event");
		subHeader.setRequestIdentifier(BigInteger.ONE);
		subHeader.setPublisherIdentifier(BigInteger.ZERO);
		subHeader.setSubscriberIdentifier(BigInteger.ZERO);
		subHeader.setExpirationTime(aDifferentTime);

		BigInteger mtaAgencyID = BigInteger.valueOf(2008);

		CPTTransitFacilityIden depot = new CPTTransitFacilityIden();
		depot.setId("CSLT");
		depot.setAgdesig("MTA NYCT");

		CPTTransitFacilityIden depot2 = new CPTTransitFacilityIden();
		depot2.setName("YUKN");

		DayTypes dt = new DayTypes();
		dt.getDayType().add("255");

		CPTVehicleIden bus1 = new CPTVehicleIden();
		bus1.setAg(mtaAgencyID);
		bus1.setId("7777");

		CPTOperatorIden firstOp = new CPTOperatorIden();
		firstOp.setId("859007");
		firstOp.setAg(mtaAgencyID);
		firstOp.setDesig("TA859007");

		SCHBlockIden block1 = new SCHBlockIden();
		// block designator: depot_rt_run_date_time
		block1.setId("MTA NYCT_20120206_CSLT_S56_001");

		SCHRunIden run0 = new SCHRunIden();
		run0.setId("S56-001");

		SCHPullInOutInfo po1 = new SCHPullInOutInfo();
		po1.setVehicle(bus1);
		po1.setTime(beginDateTime);
		po1.setPullIn(false);
		po1.setGarage(depot);
		po1.setDate(aDay);
		po1.setOperator(firstOp);
		po1.setBlock(block1);
		po1.setRun(run0);

		SCHPullInOutInfo pi1 = new SCHPullInOutInfo();
		pi1.setVehicle(bus1);
		pi1.setTime(endDateTime);
		pi1.setPullIn(true);
		pi1.setGarage(depot);
		pi1.setDate(aDay);
		pi1.setBlock(block1);
		pi1.setRun(run0);

		ObaSchPullOutList.PullOuts pullOuts = new ObaSchPullOutList.PullOuts();
		pullOuts.getPullOut().add(po1);
		pullOuts.getPullOut().add(pi1);

		ObaSchPullOutList pullOutsList = new ObaSchPullOutList();
		pullOutsList.setSubscriptionInfo(subHeader);
		pullOutsList.setBeginDate(aDay);
		pullOutsList.setBeginTime(beginTime);
		pullOutsList.setPullOuts(pullOuts);
		pullOutsList.setEndTime(endTime);
		pullOutsList.setEndDate(aDay);
		pullOutsList.setCreated(beginDateTime);
		pullOutsList.setSchVersion("TCIP 4.0");
		pullOutsList.setSourceapp("YardBoss");
		pullOutsList.setErrorCode("1");
		pullOutsList.setErrorDescription("Sample error description");
		pullOutsList
		.setNoNameSpaceSchemaLocation("http://www.aptatcip.com/APTA-TCIP-S-01%204.0_files/Schema%20Set.zip");
		pullOutsList.setDeactivation(deactivationTime);

		return pullOutsList;
	}

	private static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[] { 
				new NotNull(),
				new NotNull(),
				new NotNull(),
				new ParseLocalDate(),
				new ParseTimeWithMod(), //sched
				new Optional(ParseTimeWithMod()),
				new ParseTimeWithMod(),
				new Optional(ParseTimeWithMod()),
				new Optional(),
				new NotNull(),
				new Optional(),
				new NotNull(),

		};
		return processors;
	}





	private static void parseUTSFileToBeanList() throws IOException {

		ICsvBeanReader beanReader = null;
		try {

			JFileChooser chooser = new JFileChooser();

			int returnVal = chooser.showOpenDialog(null);
			
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println(chooser.getSelectedFile());
				File selectedFile = chooser.getSelectedFile();
				beanReader = new CsvBeanReader(new FileReader(selectedFile), CsvPreference.STANDARD_PREFERENCE);

				// the header elements are used to map the values to the bean (names must match)
				final String[] header = beanReader.getHeader(true);
				System.out.print(header);
				final CellProcessor[] processors = getProcessors();

				UTSPullOutRecord record;
				while( (record = beanReader.read(UTSPullOutRecord.class, header, processors)) != null ) {
					System.out.println(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(),
							beanReader.getRowNumber(), record));
				}

			}

		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}

	}
}
