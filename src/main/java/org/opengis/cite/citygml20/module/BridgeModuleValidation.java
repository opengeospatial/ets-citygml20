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

public class BridgeModuleValidation extends CommonFixture {
    public ArrayList<String> docNameSpace;
    @BeforeClass
    public void collectNamespace() {
        docNameSpace = GetToValidateXsdPathArrayList(this.testSubject);
    }

 


    /**
     * Verify that the CityGML instance document follows the Bridge module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Bridge module. Conformance requirements on referential integrity of CityGML property elements defined within the Bridge module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     * Clause 10.5.8 Base Requirement 1 - If a bridge only consists of one (homogeneous) part, it shall be represented by the element Bridge. However, if a bridge is composed of individual structural segments, it shall be modelled as a Bridge el- ement having one or more additional BridgePart elements.
     */
    @Test(enabled = true, description = "B.2.3 Bridge module - Clause 10.5.8 Base Requirement 1")
    public void verifyBridgeModule() {
    	NodeList rootElementList = this.testSubject.getChildNodes();
		
		boolean foundAtLeastOneBridge = false;
		
		for(int a=0; a<rootElementList.getLength(); a++)
		{
			
			if(rootElementList.item(a).getClass().toString().equals("class org.apache.xerces.dom.DeferredElementNSImpl"))
			{
				DeferredElementNSImpl element = (DeferredElementNSImpl) rootElementList.item(a);
				
				if( element.getNodeName().equals("CityModel") &&
					element.getNamespaceURI().equals("http://www.opengis.net/citygml/2.0"))
		    	{
				
					NodeList bridgeList = element.getElementsByTagNameNS("http://www.opengis.net/citygml/bridge/2.0","Bridge");
					if(bridgeList.getLength()>0) {
						foundAtLeastOneBridge = true;
				
					}
		    	}
				
			}
			
		}
		
		Assert.assertTrue(foundAtLeastOneBridge,"Expected to find at least one Bridge element in the document but none was found.");

        

    }


}
