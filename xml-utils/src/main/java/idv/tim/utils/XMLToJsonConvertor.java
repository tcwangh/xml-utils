package idv.tim.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLToJsonConvertor {
	
	public static void main(String[] args) {
		String inputXml = "";
		inputXml = readFileAsString("D:\\soapInhibition.xml");
		
		inputXml = inputXml.replaceAll("\t", "");
		inputXml = inputXml.replaceAll("[\n\r]", "");
		System.out.println(inputXml);
		HashMap<String,HashMap> attributeNodeMap = new HashMap<String,HashMap>();
		HashMap<String,String> attributeNameValueMap = new HashMap<String,String> ();
		attributeNameValueMap.put("attributeName", "extendAttributeName");
		attributeNameValueMap.put("attributeValue", "extendAttributeValue");
		attributeNodeMap.put("extendAttribute", attributeNameValueMap);
		ArrayList<HashMap> attributeNodeList = new ArrayList<HashMap> ();
		attributeNodeList.add(attributeNodeMap);
		HashMap<String,String> resultMap = new HashMap<String,String> ();
		ArrayList<HashMap> resultList = convertXMLNodeToMapList(inputXml,
				"/Envelope/Body/CreateAutoInhibitionByTemplateConfirmation/BizObjDoc/inhibitionRuleList",
				"UTF-8",0,4,attributeNodeList);
		System.out.println("<====Result====>");
		for (int i=0;i<resultList.size();i++) {
			System.out.println(resultList.get(i));
		}
	}
	
	public static String readFileAsString(String filePath) {
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader =
				new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf,0,numRead);
				fileData.append(readData);
			}
		}catch(Exception e) {
			System.out.println(e.toString());
		}finally {
			try{
				reader.close();
			}catch(Exception e) {
				System.out.println(e.toString());
			}
		}
		return fileData.toString();
	}
	
	public static Document getXMLDocument(String xmlString,String encoding) {
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			if (encoding != null && encoding.length() > 0) {
				InputSource is = new InputSource(new ByteArrayInputStream(xmlString.getBytes()));
				is.setEncoding(encoding);
				doc = db.parse(is);
			}else {
				doc = db.parse(new ByteArrayInputStream(xmlString.getBytes())); 
			}
			doc.getDocumentElement().normalize();
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		return doc;
	}
	
	public static ArrayList <HashMap> convertXMLNodeToMapList (String xmlString,String objPath, String encoding,int currentDeep,
			int deepLength,ArrayList<HashMap> attributeNodeList) {
		ArrayList <HashMap> resultList = new ArrayList<HashMap>();
		Document doc = getXMLDocument(xmlString, encoding);
		XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		try {
			int numberOfBodies = Integer.parseInt((String) xPath.evaluate("count(" + objPath + ")", doc));
			if (numberOfBodies > 0) {
				XPathExpression xPathExpressionHeaderAttribute = xPath.compile(objPath);
				NodeList nodeListHeaderAttribute = 
						(NodeList) xPathExpressionHeaderAttribute.evaluate(doc,XPathConstants.NODESET);
				for (int i = 0; i<nodeListHeaderAttribute.getLength();i++) {
					HashMap<String,String> resultMap = new HashMap<String,String> ();
					HashMap<String,Integer> entityNameCountMap = new HashMap<String,Integer>();
					Node nodeHeaderAttributeI = nodeListHeaderAttribute.item(i);
					System.out.println("====Destination Node====");
					System.out.println("[" + i + "] node name :" + nodeHeaderAttributeI.getNodeName());
					System.out.println("[" + i + "] node type :" + nodeHeaderAttributeI.getNodeType());
					System.out.println("[" + i + "] node value :" + nodeHeaderAttributeI.getNodeValue());
					System.out.println("[" + i + "] child node count :" + nodeHeaderAttributeI.getChildNodes().getLength());
					resultList = theLevelXMLNodeToMapList(resultMap, currentDeep, deepLength, nodeHeaderAttributeI,
							entityNameCountMap,objPath,encoding,xmlString,attributeNodeList);
				}
			}
		}catch(Exception e) {
			
		}
		return resultList;
	}
	
	public static ArrayList <HashMap> theLevelXMLNodeToMapList(HashMap<String,String> theNodeMap,
			int currentDeepLength,int maxDeepLength,Node theNode,HashMap<String,Integer> entityNameCountMap,
			String objPath,String encoding,String xmlString,ArrayList<HashMap> attributeNodeList) {
		int theDeep = currentDeepLength + 1;
		ArrayList<HashMap> objMapList = new ArrayList<HashMap>();
		if (theDeep > maxDeepLength) {
			System.out.println( theDeep + ">" + maxDeepLength + "=> break");
			objMapList.add(theNodeMap);
			return objMapList;
		}else {
			System.out.println( theDeep + " vs " + maxDeepLength + "=> go");
		}
		if (theNode.hasChildNodes()) {
			NodeList theNodeList = theNode.getChildNodes();
			for (int j=0;j<theNodeList.getLength();j++) {
				Node theNodeJ = theNodeList.item(j);
				System.out.println("[" + theDeep + "][" + j + "] node name :" + theNodeJ.getNodeName());
				System.out.println("[" + theDeep + "][" + j + "] node type :" + theNodeJ.getNodeType());
				System.out.println("[" + theDeep + "][" + j + "] node value :" + theNodeJ.getNodeValue());
				System.out.println("[" + theDeep + "][" + j + "] has child nodes :" + theNodeJ.hasChildNodes());
				System.out.println("[" + theDeep + "][" + j + "] child node count :" + theNodeJ.getChildNodes().getLength());
				if (theNodeJ.getNodeType() == Node.ELEMENT_NODE) {
					if (theNodeJ.getChildNodes().getLength() == 1) {
						String nodeName = theNodeJ.getNodeName();
						String nodeValue = theNodeJ.getFirstChild().getNodeValue();
						theNodeMap.put(nodeName,nodeValue);
					}else if (theNodeJ.getChildNodes().getLength() > 1 ){
						if (entityNameCountMap.containsKey(theNodeJ.getNodeName())){
							Integer tmpNo = entityNameCountMap.get(theNodeJ.getNodeName());
							entityNameCountMap.put(theNodeJ.getNodeName(), tmpNo+1);
						}else {
							entityNameCountMap.put(theNodeJ.getNodeName(), new Integer(1));
						}
					}
				}
			}
			System.out.println("The level[" + theDeep + "] Map is " + theNodeMap);
			if (theDeep+1 > maxDeepLength || entityNameCountMap.isEmpty()) {
				System.out.println("Need not to expand the next level.");
				objMapList.add(theNodeMap);
				return objMapList;
			}
			System.out.println(entityNameCountMap);
			ArrayList<String> entityNameList = new ArrayList<String> ();
			for (Map.Entry<String,Integer> entry:entityNameCountMap.entrySet()) {
				String key = entry.getKey();
				Integer value = entry.getValue();
				HashMap theAttributeNode = null;
				for (int i=0;i<attributeNodeList.size();i++) {
					if (attributeNodeList.get(i).containsKey(key)) {
						theAttributeNode = (HashMap) attributeNodeList.get(i).get(key);
						break;
					}
				}
				if (theAttributeNode != null) {
					System.out.println("The node was defined as attributes of the level-" + key + ";" + theAttributeNode);
					for (int i=0;i<value.intValue();i++){
						int objNo = i+1;
						String tmpObjPath = objPath + "/" + key + "[" + objNo + "]";
						System.out.println(tmpObjPath);
						ArrayList<HashMap> tmpList = convertXMLNodeToMapList(xmlString,tmpObjPath,encoding,
								theDeep,maxDeepLength,attributeNodeList);
						for (int j=0;j<tmpList.size();j++) {
							HashMap<String,String> tmpResult = tmpList.get(j);
							String attrName = "";
							String attrValue = "";
							if (tmpResult.containsKey(theAttributeNode.get("attributeName"))) {
								attrName = tmpResult.get(theAttributeNode.get("attributeName"));
							}
							if (tmpResult.containsKey(theAttributeNode.get("attributeValue"))) {
								attrValue = tmpResult.get(theAttributeNode.get("attributeValue"));
							}
							if (!"".equals(attrName) && !"".equals(attrValue)) {
								System.out.println("Attribute Name is " + attrName + "; Attribute Value is " + attrValue);
								theNodeMap.put(attrName, attrValue);
							}
						}
					}
				}else {
					entityNameList.add(key);
				}
				if (entityNameList.size() ==0) {
					objMapList.add(theNodeMap);
					return objMapList;
				}
				//System.out.println("entityNameList size is " + entityNameList.size());
				for (int i=0;i<entityNameList.size();i++) {
					int objCount = entityNameCountMap.get(entityNameList.get(i)).intValue();
					for (int j=0;j<objCount;j++) {
						int objNo = j + 1;
						String tmpObjPath = objPath + "/" + entityNameList.get(i) + "[" + objNo + "]";
						System.out.println(tmpObjPath);
						ArrayList<HashMap> tmpList = convertXMLNodeToMapList(xmlString,tmpObjPath,encoding,
								theDeep,maxDeepLength,attributeNodeList);
						System.out.println("tmpList size is " + tmpList.size());
						System.out.println("Merge to level " + theDeep + " result " + theNodeMap);
						for (int k=0;k<tmpList.size();k++) {
							HashMap tmpMap = (HashMap) theNodeMap.clone();
							HashMap<String,String> tmpResult = tmpList.get(k);
							for (Map.Entry<String, String> entry2:tmpResult.entrySet()) {
								String key2 = entry2.getKey();
								String value2 = entry2.getValue();
								tmpMap.put(key2, value2);
							}
							objMapList.add(tmpMap);
						}
					}
				}
			}
		}
		return objMapList;
	}
	
	
}
