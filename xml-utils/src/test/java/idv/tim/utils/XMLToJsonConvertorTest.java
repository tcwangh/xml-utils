package idv.tim.utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XMLToJsonConvertorTest {
	
	private String inputXml = "";
	
	@Before
	public void loadTestXMLString() {
		//inputXml = FileUtils.readFileAsString("D:\\soapInhibition.xml");
		inputXml = FileUtils.readFileAsString("resources\\soapInhibition.xml");
		inputXml = inputXml.replaceAll("\t", "");
		inputXml = inputXml.replaceAll("[\n\r]", "");
		System.out.println(inputXml);
	}
	@Test
	public void testExpandToMap() {
		ArrayList<HashMap> attributeNodeList = new ArrayList<HashMap> ();
		ArrayList<HashMap> resultList = XMLToJsonConvertor.convertXMLNodeToMapList(inputXml,
				"/Envelope/Body/CreateAutoSpeedByTemplateConfirmation/BizObjDoc/manufactureRuleList",
				"UTF-8",0,3,attributeNodeList);
		System.out.println("<====Result====>");
		for (int i=0;i<resultList.size();i++) {
			System.out.println(resultList.get(i));
			assertTrue("ERROR, Map doesn't contain [uuID]", resultList.get(i).containsKey("uuID"));
		}
		Assert.assertEquals("ERROE, result size not match", 3, resultList.size());
	}
	
	@Test
	public void testAttributeNode() {
		HashMap<String,HashMap> attributeNodeMap = new HashMap<String,HashMap>();
		HashMap<String,String> attributeNameValueMap = new HashMap<String,String> ();
		attributeNameValueMap.put("attributeName", "extendAttributeName");
		attributeNameValueMap.put("attributeValue", "extendAttributeValue");
		attributeNodeMap.put("extendAttribute", attributeNameValueMap);
		ArrayList<HashMap> attributeNodeList = new ArrayList<HashMap> ();
		attributeNodeList.add(attributeNodeMap);
		HashMap<String,String> resultMap = new HashMap<String,String> ();
		ArrayList<HashMap> resultList = XMLToJsonConvertor.convertXMLNodeToMapList(inputXml,
				"/Envelope/Body/CreateAutoSpeedByTemplateConfirmation/BizObjDoc/manufactureRuleList",
				"UTF-8",0,4,attributeNodeList);
		System.out.println("<====Result====>");
		for (int i=0;i<resultList.size();i++) {
			System.out.println(resultList.get(i));
		}
		Assert.assertEquals("ERROE, result size not match", 3, resultList.size());
	}

}
