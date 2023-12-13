package org.opengis.cite.citygml20.module;

import org.apache.xerces.dom.DeferredElementNSImpl;
import org.opengis.cite.citygml20.CommonFixture;
import org.opengis.cite.citygml20.ETSAssert;
import org.opengis.cite.citygml20.util.XMLUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class CoreModuleValidation extends CommonFixture {
    public ArrayList<String> docNameSpace;
    @BeforeClass
    public void collectNamespace() {
        docNameSpace = GetToValidateXsdPathArrayList(this.testSubject);
    }

    /**
     * Verify that the CityGML instance document follows the CityGML Core module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents.
     */
    @Test(enabled = true, description = "B.2.1 CityGML Core module")
    public void verifyCityGMLCoreModule() throws Exception{
        String moduleName = "CityGML Core";
        String SchemaPath = XSD_CITYGMLCORE;
  
    	ArrayList<String> arrayList = GetToValidateXsdPathArrayList(this.testSubject);
    	String[] arrXsdPath = new String[arrayList.size()];
    	arrayList.toArray(arrXsdPath);

    	String str = TransformXMLDocumentToXMLString(this.testSubject);
    	boolean result = isMultipleXMLSchemaValid(str, arrXsdPath);

    	// CityModelType is a type describing the "root" element of any city model file.
    	// Therefore we expect the iut to contain a CityModel element as the root
    	
    	if(result==true)
    	{
 
    		NodeList rootElementList = this.testSubject.getChildNodes();
    		
    		boolean foundCityModel = false;
    		
    		for(int a=0; a<rootElementList.getLength(); a++)
    		{
   
    			if(rootElementList.item(a).getClass().toString().equals("class org.apache.xerces.dom.DeferredElementNSImpl"))
    			{
    				DeferredElementNSImpl element = (DeferredElementNSImpl) rootElementList.item(a);
    			
    				if( element.getLocalName().equals("CityModel") &&
    					element.getNamespaceURI().equals("http://www.opengis.net/citygml/2.0"))
    		    	{
    					foundCityModel = true;
    		
    		    	}
    				
    			}
    			
    		}

    		Assert.assertTrue(foundCityModel,"Expected the root element to be a single CityModel element but was."+this.testSubject.getChildNodes().item(0).getNamespaceURI()+" "+this.testSubject.getChildNodes().item(0).getNodeName());

    	}
    	else {
    		Assert.fail("The document failed schema validation.");
    	}
    	


    }

}
