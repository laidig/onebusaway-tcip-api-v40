package org.onebusaway.tcip.adapters;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.xml.sax.SAXException;

import tcip_final_4_0_0.CPTOperatorIden;
import tcip_final_4_0_0.CPTSubscriptionHeader;
import tcip_final_4_0_0.CPTTransitFacilityIden;
import tcip_final_4_0_0.CPTVehicleIden;
import tcip_final_4_0_0.ObjectFactory;
import tcip_final_4_0_0.SCHBlockIden;
import tcip_final_4_0_0.SCHOperatorAssignment.DayTypes;
import tcip_final_4_0_0.SCHPullInOutInfo;
import tcip_final_4_0_0.SCHRunIden;
import tcip_final_4_0_0.SchPullOutList;

/*
 * This is here to test the XML schema and bindings file, not really to test
 * SchPullOutList per se; that's just a sample class.  
 */

/*
 * Note this test is timezone-sensitive; needs to run like "mvn clean test -Duser.timezone=America/New_York".
 */

public class PullInPullOutTest {

	@Test
	public void test() throws JAXBException, IOException, SAXException {

		SchPullOutList pullOuts = makePullOuts();

		ObjectFactory f = new ObjectFactory();
		JAXBElement<SchPullOutList> pullOutListElement = f.createSchPullOutList(pullOuts);
		Marshaller m = JAXBContext.newInstance(ObjectFactory.class).createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		StringWriter writer = new StringWriter();

		m.marshal(pullOutListElement, writer);
		String xml = writer.toString();
		
		URL resource = this.getClass().getResource("SchPullOutListSample.xml");
		String expected = FileUtils.readFileToString(new File(resource.getPath()));
		
		assertXMLEqual(expected, xml);

	}

	// CcReportPullOuts meets the description, but is trip and not
	// block-centric. D'oh!
	public SchPullOutList makePullOuts() {

		LocalTime beginTime = new LocalTime("04:59");
		DateTime beginDateTime = new DateTime("2011-06-01T04:59");
		DateTime deactivationTime = new DateTime("2040-01-04T23:59");
		LocalTime endTime = new LocalTime("00:49");
		DateTime endDateTime = new DateTime("2011-06-02T00:49");

		LocalTime aDifferentTime = new LocalTime("19:00:00");
		LocalDate aDay = new LocalDate("2011-06-01");
//		LocalDate anotherDay = new LocalDate("2011-06-02");

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

		SchPullOutList.PullOuts pullOuts = new SchPullOutList.PullOuts();
		pullOuts.getPullOut().add(po1);
		pullOuts.getPullOut().add(pi1);

		SchPullOutList pullOutsList = new SchPullOutList();
		pullOutsList.setSubscriptionInfo(subHeader);
		pullOutsList.setBeginDate(aDay);
		pullOutsList.setBeginTime(beginTime);
		pullOutsList.setPullOuts(pullOuts);
		pullOutsList.setEndTime(endTime);
		pullOutsList.setEndDate(aDay);
		pullOutsList.setCreated(beginDateTime);
		pullOutsList.setSchVersion("TCIP 4.0");
		pullOutsList.setSourceapp("YardBoss");
		pullOutsList
				.setNoNameSpaceSchemaLocation("http://www.aptatcip.com/APTA-TCIP-S-01%204.0_files/Schema%20Set.zip");
		pullOutsList.setDeactivation(deactivationTime);

		return pullOutsList;
	}
}