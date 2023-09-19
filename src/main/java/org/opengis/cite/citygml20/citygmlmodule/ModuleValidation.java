package org.opengis.cite.citygml20.citygmlmodule;

import org.opengis.cite.citygml20.CommonFixture;
import org.opengis.cite.citygml20.ETSAssert;
import org.opengis.cite.citygml20.util.XMLUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import java.net.URL;

import java.util.*;

public class ModuleValidation extends CommonFixture {
    public ArrayList<String> docNameSpace;
    @BeforeClass
    public void collectNamespace() {
        docNameSpace = GetToValidateXsdPathArrayList(this.testSubject);
    }

    /**
     * Verify that the CityGML instance document follows the CityGML Core module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents.
     * @throws Exception throws by assertContainNamespaceNSchemaValid
     */
    @Test(enabled = true, description = "B.2.1 CityGML Core module")
    public void verifyCityGMLCoreModule() throws Exception {
        String moduleName = "CityGML Core";
        String SchemaPath = XSD_CITYGMLCORE;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");

    }

    /**
     * Verify that the CityGML instance document follows the CityGML Core module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents.
     * @throws Exception throws by assertContainNamespaceNSchemaValid
     */
    @Test(enabled = true, description = "B.2.2 Appearance module") //groups = "B.2 Conformance classes related to CityGML modules"
    public void verifyAppearanceModule() throws Exception {
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
     * Verify that the CityGML instance document follows the Bridge module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Bridge module. Conformance requirements on referential integrity of CityGML property elements defined within the Bridge module may be additionally validated using the con- straints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     * @throws Exception throws by assertContainNamespaceNSchemaValid
     */
    @Test(enabled = false, description = "B.2.3 Bridge module")
    public void verifyBridgeModule() throws Exception {
        String moduleName = "Bridge";
        String SchemaPath = XSD_BRIDGE;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");

    }

    /**
     * Verify that the CityGML instance document follows the Building module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Building module. Conformance requirements on referential integrity of CityGML property elements defined within the Building module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.13.
     * @throws Exception throws by assertContainNamespaceNSchemaValid
     */
    @Test(enabled = true, description = "B.2.4 Building module")
    public void verifyBuildingModule() throws Exception {
        String moduleName = "Building";
        String SchemaPath = XSD_BUILDING;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");
    }

    /**
     * Verify that the CityGML instance document follows the CityFurniture module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the CityFurniture module. Conformance requirements on referential integrity of CityGML property elements defined within the CityFurniture module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accord- ance with the rules and guidelines stated in annex A.15.
     * @throws Exception
     */
    @Test(enabled = true, description = "B.2.5 CityFurniture module")
    public void verifyCityFurnitureModule() throws Exception {
        String moduleName = "CityFurniture";
        String SchemaPath = XSD_CITYFURNITURE;

        if (!docNameSpace.contains(SchemaPath))
            throw new SkipException("Not " + moduleName + " module.");
    }

    /**
     * Verify that the CityGML instance document are follow the Mandatory conformance requirements
     * by the XML Schema Definition that contain in namespace reference itself
     * <br><br>
     * Verify that the CityGML instance document are valid using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.14.
     * @throws Exception – throws by TransformXMLDocumentToXMLString
     **/
    @Test(enabled = true)
    public void VerifyMandatoryConformanceRequirements() throws Exception {
        String[] arrXsdPath = new String[docNameSpace.size()];
        docNameSpace.toArray(arrXsdPath);
        String str = TransformXMLDocumentToXMLString(this.testSubject);
        boolean result = isMultipleXMLSchemaValid(str, arrXsdPath);
        //----- Assert [xsd]
        Assert.assertTrue(result, String.format("Invalid Instance Document"));

        //----- Assert [Schematron]
        String defaultPhase = "";
        URL schemaRef = this.getClass().getResource(ROOT_PKG_PATH + REFINTERGRITY_SCH);
        Source source = new DOMSource(this.testSubject);
        ETSAssert.assertSchematronValid(schemaRef, source, defaultPhase);

    }



}
