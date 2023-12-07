package org.opengis.cite.citygml20.core;

import org.opengis.cite.citygml20.CommonFixture;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class CoreValidation extends CommonFixture{
	/**
	 * B.1.1 Verify the validity of the CityGML instance document against the XML Schema definition of each CityGML module that is part of the CityGML profile employed by the instance document. This may be any combination of CityGML extension modules in conjunction with the CityGML core module.
	 * @throws Exception TransformerConfigurationException
	 */
	@Test(enabled = true, description = "B.1.1 Valid CityGML instance document")
    public void verifyCityGMLinstanceDoc() throws Exception {
    	ArrayList<String> arrayList = GetToValidateXsdPathArrayList(this.testSubject);
    	//cityGMLBase.xsd
    	arrayList.add("xsd/opengis/citygml/2.0/cityGMLBase.xsd");
    	String[] arrXsdPath = new String[arrayList.size()];
    	arrayList.toArray(arrXsdPath);
    	
    	String str = TransformXMLDocumentToXMLString(this.testSubject);
    	boolean result = isMultipleXMLSchemaValid(str, arrXsdPath);
    	
    	Assert.assertTrue(result, "Invalid CityGML Instance Document");
    }
    
    /**
     * B.1.4 Verify that all spatial geometry objects within a CityGML instance document adhere to the
XML Schema definition of the Geography Markup Language version 3.1.1 and to the
CityGML spatial model.
     * @throws Exception TransformerConfigurationException
     */
    @Test(enabled = true, description = "B.1.4 Spatial geometry objects")
    public void verifySpatialGeometryObjects() throws Exception {
    	ArrayList<String> arrayList = getValidateSpatialGeometryArrayList();
    	String[] arrXsdPath = new String[arrayList.size()];
    	arrayList.toArray(arrXsdPath);
    	
    	String str = TransformXMLDocumentToXMLString(this.testSubject);
    	boolean result = isMultipleXMLSchemaValid(str, arrXsdPath);
        Assert.assertTrue(result, "Spatial geometry objects are invalid.");
    }
    
    /**
     * B.1.5 Verify that all spatial topology relations between spatial geometry objects are expressed
using the XML concept of XLinks provided by GML version 3.1.1.
	 * @throws Exception TransformerConfigurationException
     * 
     */
    @Test(enabled = true, description = "B.1.5 Spatial topology relations")
    public void verifySpatialtopologyRelations() throws Exception {
    	ArrayList<String> arrayList = getValidateSpatialGeometryArrayList();
    	arrayList.add("xsd/opengis/xlink/1.0/xlink-1.0.xsd");
    	String[] arrXsdPath = new String[arrayList.size()];
    	arrayList.toArray(arrXsdPath);
    	
    	String str = TransformXMLDocumentToXMLString(this.testSubject);
    	boolean result = isMultipleXMLSchemaValid(str, arrXsdPath);
        Assert.assertTrue(result, "Spatial topology relations are invalid.(xml)");
        
        NodeList geometryList = this.testSubject.getElementsByTagName("*");
        ArrayList<String> idArrayList = new ArrayList<String>();
        ArrayList<String> hrefArrayList = new ArrayList<String>();
        
        for (int i = 0; i < geometryList.getLength(); i++) {
            Element geometryElement = (Element) geometryList.item(i);
            
            if (geometryElement.hasAttributeNS("http://www.opengis.net/gml", "id")) {
            	String id = geometryElement.getAttributeNS("http://www.opengis.net/gml", "id");
            	idArrayList.add(id);
            
            }

            if (geometryElement.hasAttributeNS("http://www.w3.org/1999/xlink", "href")) {
            	String hrefId = geometryElement.getAttributeNS("http://www.w3.org/1999/xlink", "href");
            	hrefArrayList.add(hrefId);
            	
            }
            
        }
        
        for (String hrefId : hrefArrayList) {
        	String id = hrefId.replace("#", "");
			if (!idArrayList.contains(id)) {
				Assert.assertTrue(result, "Spatial topology relations are invalid.(xlink)");
			}
		}

    }
    
    /**
     * B.1.6 Verify that all thematic objects representing address information within a CityGML instance 
document adhere to the XML Schema definition of the Extensible Address Language (xAL) 
issued by OASIS and to the rules for representing address information in CityGML.
     * @throws Exception TransformerConfigurationException
     */
    @Test(enabled = true, description = "B.1.6 Valid Address objects")
    public void verifyAddressObject() throws Exception {
    	ArrayList<String> arrayList = GetToValidateXsdPathArrayList(this.testSubject);
    	//xAL.xsd
    	arrayList.add("xsd/opengis/citygml/xAL/xAL.xsd");
    	String[] arrXsdPath = new String[arrayList.size()];
    	arrayList.toArray(arrXsdPath);
    	
    	String str = TransformXMLDocumentToXMLString(this.testSubject);
    	boolean result = isMultipleXMLSchemaValid(str, arrXsdPath);
    	
    	Assert.assertTrue(result, "Invalid CityGML Instance Document");
    }
    
    private ArrayList<String> getValidateSpatialGeometryArrayList() {
    	ArrayList<String> arrayList = GetToValidateXsdPathArrayList(this.testSubject);
    	//gml.xsd
    	arrayList.add("xsd/opengis/gml/3.1.1/base/gml.xsd");
    	return arrayList;
    }


}
