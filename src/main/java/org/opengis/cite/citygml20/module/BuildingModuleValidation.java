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

public class BuildingModuleValidation extends CommonFixture {
    public ArrayList<String> docNameSpace;
    @BeforeClass
    public void collectNamespace() {
        docNameSpace = GetToValidateXsdPathArrayList(this.testSubject);
    }

 


    /**
     * Verify that the CityGML instance document follows the Building module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Building module. Conformance requirements on referential integrity of CityGML property elements defined within the Building module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.13.
     * Clause 10.3.9 - Base Requirement 1. If a building only consists of one (homogeneous) part, it shall be represented by the element Building. However, if a building is composed of individual structural segments, it shall be modelled as a Building element having one or more additional BuildingPart elements.
     */
    @Test(enabled = true, description = "B.2.4 Building module - Clause 10.3.9 Base Requirement 1")
    public void verifyBuildingModule() {
      	NodeList rootElementList = this.testSubject.getChildNodes();
		
    		boolean foundAtLeastOneBuilding = false;
    		
    		for(int a=0; a<rootElementList.getLength(); a++)
    		{
    			
    			if(rootElementList.item(a).getClass().toString().equals("class org.apache.xerces.dom.DeferredElementNSImpl"))
    			{
    				DeferredElementNSImpl element = (DeferredElementNSImpl) rootElementList.item(a);
    				
    				if( element.getNodeName().equals("CityModel") &&
    					element.getNamespaceURI().equals("http://www.opengis.net/citygml/2.0"))
    		    	{
    				
    					NodeList buildingList = element.getElementsByTagNameNS("http://www.opengis.net/citygml/building/2.0","Building");
    					if(buildingList.getLength()>0) {
    						foundAtLeastOneBuilding = true;
    				
    					}
    		    	}
    				
    			}
    			
    		}
    		
    		Assert.assertTrue(foundAtLeastOneBuilding,"Expected to find at least one Building element in the document but none was found.");

            
    }    
    

}
