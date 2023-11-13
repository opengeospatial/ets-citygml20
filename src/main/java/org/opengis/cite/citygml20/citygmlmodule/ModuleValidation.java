package org.opengis.cite.citygml20.citygmlmodule;

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

public class ModuleValidation extends CommonFixture {
    public ArrayList<String> docNameSpace;
    @BeforeClass
    public void collectNamespace() {
        docNameSpace = GetToValidateXsdPathArrayList(this.testSubject);
    }

    /**
     * Verify that the CityGML instance document follows the CityGML Core module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents.
     */
    @Test(enabled = true, description = "B.2.1 CityGML Core module")
    public void verifyCityGMLCoreModule() {
        String moduleName = "CityGML Core";
        String SchemaPath = XSD_CITYGMLCORE;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");
    }

    /**
     * Verify that the CityGML instance document follows the CityGML Core module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents.
     */
    @Test(enabled = true, description = "B.2.2 Appearance module") //groups = "B.2 Conformance classes related to CityGML modules"
    public void verifyAppearanceModule() {
        String moduleName = "Appearance";
        String SchemaPath = XSD_APPEARANCE;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");

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
     * Verify that the CityGML instance document follows the Bridge module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Bridge module. Conformance requirements on referential integrity of CityGML property elements defined within the Bridge module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     */
    @Test(enabled = true, description = "B.2.3 Bridge module")
    public void verifyBridgeModule() {
        String moduleName = "Bridge";
        String SchemaPath = XSD_BRIDGE;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");

    }

    /**
     * Verify that the CityGML instance document follows the Building module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Building module. Conformance requirements on referential integrity of CityGML property elements defined within the Building module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.13.
     */
    @Test(enabled = true, description = "B.2.4 Building module")
    public void verifyBuildingModule() {
        String moduleName = "Building";
        String SchemaPath = XSD_BUILDING;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");
    }

    /**
     * Verify that the CityGML instance document follows the CityFurniture module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the CityFurniture module. Conformance requirements on referential integrity of CityGML property elements defined within the CityFurniture module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     */
    @Test(enabled = true, description = "B.2.5 CityFurniture module")
    public void verifyCityFurnitureModule() {
        String moduleName = "CityFurniture";
        String SchemaPath = XSD_CITYFURNITURE;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");
    }

    /**
     * Verify that the CityGML instance document follows the CityObjectGroup module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the CityObjectGroup module. Conformance requirements on referential integrity of CityGML property elements defined within the CityObjectGroup module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     * @throws Exception XPathExpressionException
     */
    @Test(enabled = true, description = "B.2.6 CityObjectGroup module")
    public void verifyCityObjectGroupModule() throws Exception {
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

    /**
     * Verify that the CityGML instance document follows the Generics module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Generics module. Conformance requirements on referential integrity of CityGML property elements defined within the Generics module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     */
    @Test(enabled = true, description = "B.2.7 Generics module")
    public void verifyGenericsModule() {
        String moduleName = "Generics";
        String SchemaPath = XSD_GENERICS;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");
    }

    /**
     * Verify that the CityGML instance document follows the LandUse module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the LandUse module. Conformance requirements on referential integrity of CityGML property elements defined within the LandUse module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     */
    @Test(enabled = true, description = "B.2.8 LandUse module")
    public void verifyLandUseModule() {
        String moduleName = "LandUse";
        String SchemaPath = XSD_LANDUSE;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");
    }

    /**
     * Verify that the CityGML instance document follows the Relief module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Relief module. Conformance requirements on referential integrity of CityGML property elements defined within the Relief module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     */
    @Test(enabled = true, description = "B.2.9 Relief module")
    public void verifyReliefModule() {
        String moduleName = "Relief";
        String SchemaPath = XSD_RELIF;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");
    }

    /**
     * Verify that the CityGML instance document follows the Transportation module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Transportation module. Conformance requirements on referential integrity of CityGML property elements defined within the Transportation module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     */
    @Test(enabled = true, description = "B.2.10 Transportation module")
    public void verifyTransportationModule() {
        String moduleName = "Transportation";
        String SchemaPath = XSD_TRANSPORTATION;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");
    }

    /**
     * Verify that the CityGML instance document follows the Tunnel module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Tunnel module. Conformance requirements on referential integrity of CityGML property elements defined within the Tunnel module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     */
    @Test(enabled = true, description = "B.2.11 Tunnel module")
    public void verifyTunnelModule() {
        String moduleName = "Tunnel";
        String SchemaPath = XSD_TUNNEL;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");
    }

    /**
     * Verify that the CityGML instance document follows the Vegetation module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Vegetation module. Conformance requirements on referential integrity of CityGML property elements defined within the Vegetation module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     */
    @Test(enabled = true, description = "B.2.12 Vegetation module")
    public void verifyVegetationModule() {
        String moduleName = "Vegetation";
        String SchemaPath = XSD_VEGETATION;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");
    }

    /**
     * Verify that the CityGML instance document follows the WaterBody module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the WaterBody module. Conformance requirements on referential integrity of CityGML property elements defined within the WaterBody module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     * @throws Exception
     */
    @Test(enabled = true, description = "B.2.13 WaterBody module")
    public void verifyWaterBodyModule() throws Exception {
        String moduleName = "WaterBody";
        String SchemaPath = XSD_WATERBODY;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");

        boolean validParent = true;

        String[] WaterBoundarySurfaceTypes = {"WaterSurface","WaterGroundSurface","WaterClosureSurface"};
        String prefix = "//*/wtr:";
        for (int indexType = 0; indexType < WaterBoundarySurfaceTypes.length; indexType++) {
            String expressionPath = prefix + WaterBoundarySurfaceTypes[indexType];
            NodeList nodes = XMLUtils.getNodeListByXPath(this.testSubject, expressionPath);
            for (int i = 0; i < nodes.getLength(); i++) {
                Element n = (Element) nodes.item(i);
                Node parentNode = n.getParentNode();
                String parentNodeName = parentNode.getNodeName();
                if (!parentNodeName.equals("wtr:WaterBody")) {
                    validParent = false;
                    break;
                }
            }
        }
        Assert.assertTrue(validParent,"_WaterBoundarySurface elements shall only be included as parts of corresponding WaterBody elements.");
    }

    /**
     * Verify that the CityGML instance document are follow the Mandatory conformance requirements
     * by the XML Schema Definition that contain in namespace reference itself
     * <br><br>
     * Verify that the CityGML instance document are valid using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.14.
     * @throws Exception – throws by TransformXMLDocumentToXMLString
     **/
    @Test(enabled = true, description = "Schematron and Schema Validation")
    public void VerifyMandatoryConformanceRequirements() throws Exception {
        String[] arrXsdPath = new String[docNameSpace.size()];
        docNameSpace.toArray(arrXsdPath);
        String str = TransformXMLDocumentToXMLString(this.testSubject);
        boolean result = isMultipleXMLSchemaValid(str, arrXsdPath);
        //----- Assert [xsd]
        Assert.assertTrue(result, "Invalid Instance Document");

        //----- Assert [Schematron]
        String defaultPhase = "";
        URL schemaRef = this.getClass().getResource(ROOT_PKG_PATH + REFINTERGRITY_SCH);
        Source source = new DOMSource(this.testSubject);
        ETSAssert.assertSchematronValid(schemaRef, source, defaultPhase);
    }

}
