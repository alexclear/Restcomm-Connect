package org.mobicents.servlet.restcomm.http;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.ShrinkWrapMaven;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mobicents.servlet.restcomm.entities.RestCommResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author <a href="mailto:gvagenas@gmail.com">gvagenas</a>
 */

@RunWith(Arquillian.class)
public class CallsEndpointTest {

	private static final String version = "6.1.2-TelScale-SNAPSHOT";

	@ArquillianResource
	private Deployer deployer;
	@ArquillianResource
	URL deploymentUrl;

	private String adminAccountSid = "ACae6e420f425248d6a26948c17a9e2acf";
	private String adminAuthToken = "77f8c12cc7b8f8423e5c38b035249166";

	@Test
	public void getCallsList(){
		JsonObject firstPage = RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken);
		int totalSize = firstPage.get("total").getAsInt();
		JsonArray firstPageCallsArray = firstPage.get("calls").getAsJsonArray();
		int firstPageCallsArraySize = firstPageCallsArray.size();
		assertTrue(firstPageCallsArraySize == 50 );
		assertTrue(firstPage.get("start").getAsInt() == 0);
		assertTrue(firstPage.get("end").getAsInt() == 49);

		
		JsonObject secondPage = (JsonObject)RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken, 2, null, true);
		JsonArray secondPageCallsArray = secondPage.get("calls").getAsJsonArray();
		assertTrue(secondPageCallsArray.size() == 50);
		assertTrue(secondPage.get("start").getAsInt() == 100);
		assertTrue(secondPage.get("end").getAsInt() == 149);
		
		JsonObject lastPage = (JsonObject)RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken,firstPage.get("num_pages").getAsInt(), null, true); 
		JsonArray lastPageCallsArray = lastPage.get("calls").getAsJsonArray();
		assertTrue(lastPageCallsArray.get(lastPageCallsArray.size()-1).getAsJsonObject().get("sid").getAsString().equals("CAfe9ce46f104f4beeb10c83a5dad2be66"));
		assertTrue(lastPageCallsArray.size() == 42);
		assertTrue(lastPage.get("start").getAsInt() == 400);
		assertTrue(lastPage.get("end").getAsInt() == 442);
		
		assertTrue(totalSize == 442);		
	}

	@Test
	public void getCallsListUsingPageSize(){
		JsonObject firstPage = (JsonObject)RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken,null, 100, true);
		int totalSize = firstPage.get("total").getAsInt();
		JsonArray firstPageCallsArray = firstPage.get("calls").getAsJsonArray();
		int firstPageCallsArraySize = firstPageCallsArray.size();
		assertTrue(firstPageCallsArraySize == 100 );
		assertTrue(firstPage.get("start").getAsInt() == 0);
		assertTrue(firstPage.get("end").getAsInt() == 99);

		
		JsonObject secondPage = (JsonObject)RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken, 2, 100, true);
		JsonArray secondPageCallsArray = secondPage.get("calls").getAsJsonArray();
		assertTrue(secondPageCallsArray.size() == 100);
		assertTrue(secondPage.get("start").getAsInt() == 200);
		assertTrue(secondPage.get("end").getAsInt() == 299);
		
		JsonObject lastPage = (JsonObject)RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken,firstPage.get("num_pages").getAsInt(), 100, true); 
		JsonArray lastPageCallsArray = lastPage.get("calls").getAsJsonArray();
		assertTrue(lastPageCallsArray.get(lastPageCallsArray.size()-1).getAsJsonObject().get("sid").getAsString().equals("CAfe9ce46f104f4beeb10c83a5dad2be66"));
		assertTrue(lastPageCallsArray.size() == 42);
		assertTrue(lastPage.get("start").getAsInt() == 400);
		assertTrue(lastPage.get("end").getAsInt() == 442);
		
		assertTrue(totalSize == 442);		
	}
	
	@Test
	public void getCallsListUsingPageSizeXML(){
		JsonObject firstPage = RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken,null, 100, false);
		
//		System.out.println(firstPageResp.getObject().toString());
		
//		int totalSize = firstPage.get("total").getAsInt();
//		JsonArray firstPageCallsArray = firstPage.get("calls").getAsJsonArray();
//		int firstPageCallsArraySize = firstPageCallsArray.size();
//		assertTrue(firstPageCallsArraySize == 100 );
//		assertTrue(firstPage.get("start").getAsInt() == 0);
//		assertTrue(firstPage.get("end").getAsInt() == 99);
//
//		
//		JsonObject secondPage = RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken, 2, 100, false);
//		JsonArray secondPageCallsArray = secondPage.get("calls").getAsJsonArray();
//		assertTrue(secondPageCallsArray.size() == 100);
//		assertTrue(secondPage.get("start").getAsInt() == 200);
//		assertTrue(secondPage.get("end").getAsInt() == 299);
//		
//		JsonObject lastPage = RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken,firstPage.get("num_pages").getAsInt(), 100, true); 
//		JsonArray lastPageCallsArray = lastPage.get("calls").getAsJsonArray();
//		assertTrue(lastPageCallsArray.get(lastPageCallsArray.size()-1).getAsJsonObject().get("sid").getAsString().equals("CAfe9ce46f104f4beeb10c83a5dad2be66"));
//		assertTrue(lastPageCallsArray.size() == 42);
//		assertTrue(lastPage.get("start").getAsInt() == 400);
//		assertTrue(lastPage.get("end").getAsInt() == 442);
//		
//		assertTrue(totalSize == 442);		
	}
	
	@Test
	public void getCallsListFilteredByStatus(){
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("Status", "in-progress");
		
		JsonObject allCallsObject = RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken);
		
		JsonObject filteredCallsByStatusObject = RestcommCallsTool.getInstance().getCallsUsingFilter(deploymentUrl.toString(), 
				adminAccountSid, adminAuthToken, filters);
		
		assertTrue(filteredCallsByStatusObject.get("calls").getAsJsonArray().size() > 0);
		assertTrue(allCallsObject.get("calls").getAsJsonArray().size() == filteredCallsByStatusObject.get("calls").getAsJsonArray().size());
	}

//	@Test
//	public void getCallsListFilteredBySender(){
//		Map<String, String> filters = new HashMap<String, String>();
//		filters.put("From", "3021097%");
//		JsonArray allCalls = RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken);
//		
//		JsonArray filteredCallsBySender = RestcommCallsTool.getInstance().getCallsUsingFilter(deploymentUrl.toString(), 
//				adminAccountSid, adminAuthToken, filters);
//		
//		assertTrue(filteredCallsBySender.size() > 0);
//		assertTrue(allCalls.size() > filteredCallsBySender.size());
//	}
//
//	@Test
//	public void getCallsListFilteredByRecipient(){
//		Map<String, String> filters = new HashMap<String, String>();
//		filters.put("To", "1512600%");
//		JsonArray allCalls = RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken);
//		
//		JsonArray filteredCallsByRecipient = RestcommCallsTool.getInstance().getCallsUsingFilter(deploymentUrl.toString(), 
//				adminAccountSid, adminAuthToken, filters);
//		
//		assertTrue(filteredCallsByRecipient.size() > 0);
//		assertTrue(allCalls.size() > filteredCallsByRecipient.size());
//	}
//	
//	@Test
//	public void getCallsListFilteredByStartTime(){
//		Map<String, String> filters = new HashMap<String, String>();
//		filters.put("StartTime", "2013-09-10");
//		JsonArray allCalls = RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken);
//		
//		JsonArray filteredCallsByStartTime = RestcommCallsTool.getInstance().getCallsUsingFilter(deploymentUrl.toString(), 
//				adminAccountSid, adminAuthToken, filters);
//		
//		assertTrue(filteredCallsByStartTime.size() > 0);
//		assertTrue(allCalls.size() > filteredCallsByStartTime.size());
//	}
//	
//	@Test
//	public void getCallsListFilteredByParentCallSid(){
//		Map<String, String> filters = new HashMap<String, String>();
//		filters.put("ParentCallSid", "CA01a09068a1f348269b6670ef599a6e57");
//		
//		JsonArray filteredCallsByParentCallSid = RestcommCallsTool.getInstance().getCallsUsingFilter(deploymentUrl.toString(), 
//				adminAccountSid, adminAuthToken, filters);
//		
//		assertTrue(filteredCallsByParentCallSid.size() == 0);
//	}
//	
//	@Test
//	public void getCallsListFilteredUsingMultipleFilters(){
//		Map<String, String> filters = new HashMap<String, String>();
//		filters.put("StartTime", "2013-09-10");
//		filters.put("To", "1512600%");
//		filters.put("From", "3021097%");
//		filters.put("Status", "in-progress");
//		
//		JsonArray allCalls = RestcommCallsTool.getInstance().getCalls(deploymentUrl.toString(), adminAccountSid, adminAuthToken);
//		
//		JsonArray filteredCallsUsingMultipleFilters = RestcommCallsTool.getInstance().getCallsUsingFilter(deploymentUrl.toString(), 
//				adminAccountSid, adminAuthToken, filters);
//		
//		assertTrue(filteredCallsUsingMultipleFilters.size() > 0);
//		assertTrue(allCalls.size() > filteredCallsUsingMultipleFilters.size());
//	}	
	
	@Deployment(name="ClientsEndpointTest", managed=true, testable=false)
	public static WebArchive createWebArchiveNoGw() {
		final WebArchive archive = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.application:war:" + version)
				.withoutTransitivity().asSingle(WebArchive.class);
		JavaArchive dependency = ShrinkWrapMaven.resolver()
				.resolve("commons-configuration:commons-configuration:jar:1.7")
				.offline().withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("jain:jain-mgcp-ri:jar:1.0")
				.offline().withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("org.mobicents.media.client:mgcp-driver:jar:3.0.0.Final")
				.offline().withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("joda-time:joda-time:jar:2.0")
				.offline().withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.iSpeech:iSpeech:jar:1.0.1")
				.offline().withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.commons:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.dao:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.asr:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.fax:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.tts.acapela:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.tts.api:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.mgcp:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.http:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.interpreter:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.sms.api:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.sms:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.telephony.api:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		dependency = ShrinkWrapMaven.resolver()
				.resolve("com.telestax.servlet:restcomm.telephony:jar:" + version)
				.withoutTransitivity().asSingle(JavaArchive.class);
		archive.addAsLibrary(dependency);
		archive.delete("/WEB-INF/sip.xml");
		archive.delete("/WEB-INF/conf/restcomm.xml");
		archive.delete("/WEB-INF/data/hsql/restcomm.script");
		archive.addAsWebInfResource("sip.xml");
		archive.addAsWebInfResource("restcomm.xml", "conf/restcomm.xml");
		archive.addAsWebInfResource("restcomm_with_Data.script", "data/hsql/restcomm.script");
		return archive;
	}
	
}
