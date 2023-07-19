package org.opengis.cite.citygml20.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.citygml4j.model.citygml.cityobjectgroup.CityObjectGroup;
import org.citygml4j.model.module.citygml.AppearanceModule;
import org.citygml4j.model.module.citygml.BridgeModule;
import org.citygml4j.model.module.citygml.BuildingModule;
import org.citygml4j.model.module.citygml.CityFurnitureModule;
import org.citygml4j.model.module.citygml.CityObjectGroupModule;
import org.citygml4j.model.module.citygml.GenericsModule;
import org.citygml4j.model.module.citygml.LandUseModule;
import org.citygml4j.model.module.citygml.ReliefModule;
import org.citygml4j.model.module.citygml.TexturedSurfaceModule;
import org.citygml4j.model.module.citygml.TransportationModule;
import org.citygml4j.model.module.citygml.TunnelModule;
import org.citygml4j.model.module.citygml.VegetationModule;
import org.citygml4j.model.module.citygml.WaterBodyModule;
import org.opengis.cite.citygml20.CommonFixture;
import org.opengis.cite.citygml20.SuiteAttribute;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CoreValidation extends CommonFixture{
	private Document testSubject;

    /**
     * Obtains the test subject from the ISuite context. The suite attribute
     * {@link org.opengis.cite.citygml20.SuiteAttribute#TEST_SUBJECT} should
     * evaluate to a DOM Document node.
     * 
     * @param testContext
     *            The test (group) context.
     */
    @BeforeClass
    public void obtainTestSubject(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName());
        if ((null != obj) && Document.class.isAssignableFrom(obj.getClass())) {
            this.testSubject = Document.class.cast(obj);
        }
    }
    
    @Test(description = "B.1.1 Valid CityGML instance document")
    public void verifyCityGMLinstanceDoc() throws Exception {
    	ArrayList<String> arrayList = GetToValidateXsdPathArrayList();
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
     * 
     */
    @Test(description = "B.1.4 Spatial geometry objects")
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

     * 
     */
    @Test(description = "B.1.5 Spatial topology relations")
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
            	System.out.println("id:" + id);
            }

            if (geometryElement.hasAttributeNS("http://www.w3.org/1999/xlink", "href")) {
            	String hrefId = geometryElement.getAttributeNS("http://www.w3.org/1999/xlink", "href");
            	hrefArrayList.add(hrefId);
            	System.out.println("hrefId:" + hrefId);
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
     * 
     */
    @Test(description = "B.1.6 Valid Address objects")
    public void verifyAddressObject() throws Exception {
    	ArrayList<String> arrayList = GetToValidateXsdPathArrayList();
    	//xAL.xsd
    	arrayList.add("xsd/opengis/citygml/xAL/xAL.xsd");
    	String[] arrXsdPath = new String[arrayList.size()];
    	arrayList.toArray(arrXsdPath);
    	
    	String str = TransformXMLDocumentToXMLString(this.testSubject);
    	boolean result = isMultipleXMLSchemaValid(str, arrXsdPath);
    	
    	Assert.assertTrue(result, "Invalid CityGML Instance Document");
    }
    
    private ArrayList<String> getValidateSpatialGeometryArrayList() {
    	ArrayList<String> arrayList = GetToValidateXsdPathArrayList();
    	//gml.xsd
    	arrayList.add("xsd/opengis/gml/3.1.1/base/gml.xsd");
    	return arrayList;
    }
    
    private ArrayList<String> GetToValidateXsdPathArrayList(){
    	//
    	HashMap<String, String> hashMap = new LinkedHashMap<String, String>();
    	hashMap.put(AppearanceModule.v2_0_0.getNamespaceURI() , "xsd/opengis/citygml/appearance/2.0/appearance.xsd");
    	hashMap.put(BridgeModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/bridge/2.0/bridge.xsd");
    	hashMap.put(BuildingModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/building/2.0/building.xsd");
    	hashMap.put(CityFurnitureModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/cityfurniture/2.0/cityFurniture.xsd");
    	hashMap.put(CityObjectGroupModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/cityobjectgroup/2.0/cityObjectGroup.xsd");
    	hashMap.put(GenericsModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/generics/2.0/generics.xsd");
    	hashMap.put(LandUseModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/landuse/2.0/landUse.xsd");
    	hashMap.put(ReliefModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/relief/2.0/relief.xsd");
    	hashMap.put(TransportationModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/transportation/2.0/transportation.xsd");
    	hashMap.put(TunnelModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/tunnel/2.0/tunnel.xsd");
    	hashMap.put(VegetationModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/vegetation/2.0/vegetation.xsd");
    	hashMap.put(WaterBodyModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/waterbody/2.0/waterBody.xsd");
    	hashMap.put(TexturedSurfaceModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/texturedsurface/2.0/texturedSurface.xsd");
    	
    	//
    	Element rootElement = this.testSubject.getDocumentElement();
    	NamedNodeMap namedNodeMap = rootElement.getAttributes();
    	ArrayList<String> arrayList = new ArrayList<String>();
    	for (int i = 0; i < namedNodeMap.getLength(); i++) {
    		Node attr = namedNodeMap.item(i);
    		String attrName = attr.getNodeName();
    		String namespaceUri = attr.getNodeValue();
    		if (attrName.contains("xmlns")) {
    			if (hashMap.containsKey(namespaceUri)) {
    				arrayList.add(hashMap.get(namespaceUri));
    				//System.out.println(attr.getNodeName()+ " = \"" + attr.getNodeValue() + "\"");
    			}
    		}
		}
    	
    	return arrayList;
    }

}
