package org.opengis.cite.citygml20.module;

import org.apache.xerces.dom.DeferredElementNSImpl;
import org.opengis.cite.citygml20.CommonFixture;
import org.opengis.cite.citygml20.ETSAssert;
import org.opengis.cite.citygml20.util.XMLUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class VegetationModuleValidation extends CommonFixture {
    public ArrayList<String> docNameSpace;
    @BeforeClass
    public void collectNamespace() {
        docNameSpace = GetToValidateXsdPathArrayList(this.testSubject);
    }

 
    /**
     * Verify that the CityGML instance document follows the Vegetation module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Vegetation module. Conformance requirements on referential integrity of CityGML property elements defined within the Vegetation module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     */
    @Test(enabled = true, description = "B.2.12 Vegetation module")
    public void verifyVegetationModule() {
        String moduleName = "Vegetation";
        String SchemaPath = XSD_VEGETATION;
    	String[] moduleElementNameList = {"SolitaryVegetationObject","PlantCover"};
    	StringBuffer sb = new StringBuffer();
    	for(int s=0; s < moduleElementNameList.length; s++)
    	{
    		sb.append(moduleElementNameList[s]);
    		if(s < (moduleElementNameList.length-1)) {
    			sb.append(", ");
    		}
    		else {
    			sb.append(" ");
    		}
    	}
    	
    	
      	NodeList rootElementList = this.testSubject.getChildNodes();
		
    		boolean foundAtLeastOne = false;
    		
    		for(int a=0; a<rootElementList.getLength(); a++)
    		{
    			
    			if(rootElementList.item(a).getClass().toString().equals("class org.apache.xerces.dom.DeferredElementNSImpl"))
    			{
    				DeferredElementNSImpl element = (DeferredElementNSImpl) rootElementList.item(a);
    				
    				if( element.getLocalName().equals("CityModel") &&
    					element.getNamespaceURI().equals("http://www.opengis.net/citygml/2.0"))
    		    	{
    				  for(int b = 0 ; b< moduleElementNameList.length; b++) {
    					NodeList nodeList = element.getElementsByTagNameNS("http://www.opengis.net/citygml/"+moduleName.toLowerCase()+"/2.0", moduleElementNameList[b]);    				
    					if(nodeList.getLength()>0) {
    						foundAtLeastOne = true;
    				
    					}
    		    	 }
    		    	}
    				
    			}
    			
    		}
    		
    		Assert.assertTrue(foundAtLeastOne,"None of "+sb.toString()+" elements was found in the document.");
    }



}
