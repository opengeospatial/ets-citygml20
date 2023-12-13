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
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Set;

public class AppearanceModuleValidation extends CommonFixture {
	
	
    public ArrayList<String> docNameSpace;
    @BeforeClass
    public void collectNamespace() {
        docNameSpace = GetToValidateXsdPathArrayList(this.testSubject);
    }

    /**
     * Verify that the CityGML instance document follows the CityGML Core module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents.
     */
    @Test(enabled = true, description = "B.2.2 Appearance module") //groups = "B.2 Conformance classes related to CityGML modules"
    public void verifyAppearanceModule() throws Exception{
        String SchemaPath = XSD_APPEARANCE;        
    	String moduleName = "Appearance";
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
     * Verify that the CityGML instance document follows the CityGML Core module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents.
     */
    @Test(enabled = true, description = "B.2.2 Appearance module - Clause 9.7 - Referential integrity 11", dependsOnMethods = { "verifyAppearanceModule" }) //groups = "B.2 Conformance classes related to CityGML modules"
    public void verifyAppearanceTextureCoordinatesToLinearRingId() throws Exception{

        String expressionPath;
        NodeList nodes;
        boolean state;
        Set<String> idSet = new LinkedHashSet<>();
        Set<String> targetSet = new LinkedHashSet<>();

        // Collect all attribute(gml:id) of the Appearance module
        expressionPath = "//*[@gml:id]";
        nodes = XMLUtils.getNodeListByXPath(this.testSubject, expressionPath);
        
      
        for (int i = 0; i < nodes.getLength(); i++) {
            Element n = (Element) nodes.item(i);
            String value = n.getAttribute("gml:id");   
            idSet.add(value);
        }
     
      // Clause 9.7 - Referential integrity (11): The ring attribute of the textureCoordinates property 
      // of the element TexCoordList shall specify the gml:id of the target surface geometry object which may 
      // only be of type gml:LinearRing.

        expressionPath = "//app:textureCoordinates[@ring]";
        nodes = XMLUtils.getNodeListByXPath(this.testSubject, expressionPath);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element n = (Element) nodes.item(i);
            String value = n.getAttribute("ring").substring(1);
            targetSet.add(value);
        }
        state = idSet.containsAll(targetSet);

      
        //----- Assert [TextureCoordinates reference]
        Assert.assertTrue(state, "TextureCoordinates's attribute(ring) reference invalid.");

        targetSet.clear();
        expressionPath = "//app:GeoreferencedTexture/app:target/text()";
        nodes = XMLUtils.getNodeListByXPath(this.testSubject, expressionPath);
        for (int i = 0; i < nodes.getLength(); i++) {
            String value = nodes.item(i).getNodeValue().substring(1);
            targetSet.add(value);
        }
        state = idSet.containsAll(targetSet); 
        
     
        //----- Assert [GeoreferencedTexture reference]
        Assert.assertTrue(state, "GeoreferencedTexture's node(app:target) reference invalid.");
        
        

    }


    /**
     * Clause 9.7. Base requirement 3. Each boundary point of the surface must receive a corresponding coordinate pair in texture space. 
     */
    @Test(enabled = true, description = "B.2.2 Appearance module - Clause 9.7. Base requirement 3", dependsOnMethods = { "verifyAppearanceModule" }) 
    public void verifyAppearanceTextureCoordinatesToLinearRingPosList() throws Exception{
      
    	
    	Hashtable<String, String> textureCoordinatesTable = new Hashtable<String, String>();
    	
        String expressionPath;
        NodeList nodes;
        
        
        expressionPath = "//app:textureCoordinates[@ring]";
        nodes = XMLUtils.getNodeListByXPath(this.testSubject, expressionPath);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element n = (Element) nodes.item(i);
            String value = n.getAttribute("ring");
              
            textureCoordinatesTable.put(value, n.getTextContent().trim());
        }
        
            
        expressionPath = "//gml:LinearRing";
        nodes = XMLUtils.getNodeListByXPath(this.testSubject, expressionPath);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element n = (Element) nodes.item(i);
            if(!n.hasAttributeNS("http://www.opengis.net/gml","id"))
            {
            	continue; //only check those linearings that are associated with texturecoordinates
            }
            

            
            String value = n.getAttributeNS("http://www.opengis.net/gml","id");
            
            if(!textureCoordinatesTable.containsKey("#"+value))
            {
            	continue; //only check those linearings that are associated with texturecoordinates
            }
            
            String linearRingCoordinates = ((Element) n.getElementsByTagNameNS("http://www.opengis.net/gml","posList").item(0)).getTextContent().trim();
            int numberOfLinearRingCoordinates = linearRingCoordinates.split("\\s+").length;
            int numberOfTextureCoordinates = textureCoordinatesTable.get("#"+value).split("\\s+").length;
            String srsDimensionString = ((Element) n.getElementsByTagNameNS("http://www.opengis.net/gml","posList").item(0)).getAttribute("srsDimension");
            
            Assert.assertTrue((numberOfTextureCoordinates/2)==(numberOfLinearRingCoordinates/Integer.parseInt(srsDimensionString)),"Problem with "+value+". Each boundary point of the surface must receive a corresponding coordinate pair in texture space.");
        }        
        
        

        

    }    
    
}
