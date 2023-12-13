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

public class CityObjectGroupModuleValidation extends CommonFixture {
    public ArrayList<String> docNameSpace;
    @BeforeClass
    public void collectNamespace() {
        docNameSpace = GetToValidateXsdPathArrayList(this.testSubject);
    }

 
    /**
     * Verify that the CityGML instance document follows the CityObjectGroup module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the CityObjectGroup module. Conformance requirements on referential integrity of CityGML property elements defined within the CityObjectGroup module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     * @throws Exception XPathExpressionException
     */
    @Test(enabled = true, description = "B.2.6 CityObjectGroup module")
    public void verifyCityObjectGroupModule() throws Exception {
        String SchemaPath = XSD_CITYOBJECTGROUP;
        String moduleName = "CityObjectGroup";
        String moduleElementName = moduleName;
       
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
   				
   					NodeList nodeList = element.getElementsByTagNameNS("http://www.opengis.net/citygml/"+moduleName.toLowerCase()+"/2.0", moduleElementName);
   					if(nodeList.getLength()>0) {
   						foundAtLeastOne = true;
   				
   					}
   		    	}
   				
   			}
   			
   		}
   		
   		Assert.assertTrue(foundAtLeastOne,"No "+moduleElementName+" element was found in the document.");

    }   

    /**
     * Verify that the CityGML instance document follows the CityObjectGroup module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the CityObjectGroup module. Conformance requirements on referential integrity of CityGML property elements defined within the CityObjectGroup module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     * Base Requirement 1. No cyclic groupings shall be included within a CityGML instance document.
     * @throws Exception XPathExpressionException
     */
    @Test(enabled = true, description = "B.2.6 CityObjectGroup module - Clause 10.11.3 Base requirement 1")
    public void verifyCityObjectGroupCheckCyclicState() throws Exception {
        String moduleName = "CityObjectGroup";
        String SchemaPath = XSD_CITYOBJECTGROUP;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");

        boolean cyclingState = true;
        String cityObjectGroupExpression = "//grp:CityObjectGroup[@gml:id]/grp:groupMember";
        XPath xpath = XMLUtils.getXPathWithNS(this.testSubject);

        NodeList nodes = (NodeList) xpath.evaluate(cityObjectGroupExpression, this.testSubject, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element nodeEl = (Element) nodes.item(i);
            Element parentEl = (Element)nodeEl.getParentNode();
            String parentId = parentEl.getAttribute("gml:id");

            String expr = "grp:CityObjectGroup";
            NodeList groupList = (NodeList) xpath.evaluate(expr, nodeEl, XPathConstants.NODESET);
            int a = groupList.getLength();
            for (int j = 0; j < groupList.getLength(); j++) {
                Element node = (Element) groupList.item(i);
                String childId = node.getAttribute("gml:id");
                if (childId.equals(parentId))
                    cyclingState = false;
            }
        }
        Assert.assertTrue(cyclingState, "No cyclic groupings shall be included within a CityGML instance document.");
    }


}
