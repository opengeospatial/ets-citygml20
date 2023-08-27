package org.opengis.cite.citygml20.citygmlmodule;

import org.opengis.cite.citygml20.CommonFixture;
import org.opengis.cite.citygml20.ETSAssert;
import org.opengis.cite.citygml20.util.XMLUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ModuleValidation extends CommonFixture {
    /**
     * Verify that the CityGML instance document follows the CityGML Core module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents.
     * <br><br>
     * Verify the validity of the CityGML instance document against the XML Schema definition of the CityGML Core module. This test case is mandatory for all CityGML instance documents.
     * @throws Exception throws by assertContainNamespaceNSchemaValid
     */
    @Test(enabled = false, description = "B.2.1 CityGML Core module")
    public void verifyCityGMLCoreModule() throws Exception {
        //----- Assert [namespace], [xsd]
        assertContainNamespaceNSchemaValid(this.testSubject,"CityGML Core", XSD_CITYGMLCORE);

        //----- Assert [Schematron]
        assertSchematronValidWith_RefIntegrity(this.testSubject);
    }

    /**
     * Verify that the CityGML instance document follows the CityGML Core module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents.
     * <br><br>
     * Verify the validity of the CityGML instance document against the XML Schema definition of the CityGML Core module. This test case is mandatory for all CityGML instance documents.
     * @throws Exception throws by assertContainNamespaceNSchemaValid
     */
    @Test(enabled = false, description = "B.2.2 Appearance module") //groups = "B.2 Conformance classes related to CityGML modules"
    public void verifyAppearanceModule() throws Exception {
        //----- Assert [namespace], [xsd]
        assertContainNamespaceNSchemaValid(this.testSubject,"Appearance", XSD_APPEARANCE);

        String expressionPath;
        NodeList nodes;
        boolean state;
        Set<String> idSet = new LinkedHashSet<>();
        Set<String> targetSet = new LinkedHashSet<>();

        /**
         * Collect all attribute(gml:id) of the Appearance module
         **/
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

        //----- Assert [Schematron]
        assertSchematronValidWith_RefIntegrity(this.testSubject);
    }

    /**
     * Verify that the CityGML instance document follows the Bridge module’s rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Bridge module. Conformance requirements on referential integrity of CityGML property elements defined within the Bridge module may be additionally validated using the con- straints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.15.
     * <br><br>
     * Verify the validity of the CityGML instance document against the XML Schema definition of the Bridge module. This test case is mandatory for all CityGML instance documents employing elements defined within the Bridge module.
     * @throws Exception throws by assertContainNamespaceNSchemaValid
     */
    @Test(enabled = false, description = "B.2.3 Building module")
    public void verifyBridgeModule() throws Exception {
        //----- Assert [namespace], [xsd]
        assertContainNamespaceNSchemaValid(this.testSubject,"Bridge", XSD_BRIDGE);

        //----- Assert [Schematron]
        assertSchematronValidWith_RefIntegrity(this.testSubject);
    }

    /**
     * Verify that the CityGML instance document follows the Building module's rules for encoding of objects and properties and adheres to all its conformance requirements. This test case is mandatory for all CityGML instance documents employing elements defined within the Building module. Conformance requirements on referential integrity of CityGML property elements defined within the Building module may be additionally validated using the constraints provided by the Schematron schema referentialIntegrity.sch in accordance with the rules and guidelines stated in annex A.13.
     * <br><br>
     * Verify the validity of the CityGML instance document against the XML Schema definition of the Building module. This test case is mandatory for all CityGML instance documents employing elements defined within the Building module.
     * @throws Exception throws by assertContainNamespaceNSchemaValid
     */
    @Test(enabled = false, description = "B.2.4 Building module")
    public void verifyBuildingModule() throws Exception {
        //----- Assert [namespace], [xsd]
        assertContainNamespaceNSchemaValid(this.testSubject,"Building", XSD_BUILDING);

        //----- Assert [Schematron]
        assertSchematronValidWith_RefIntegrity(this.testSubject);
    }

    protected void assertSchematronValidWith_RefIntegrity(Document testSubject) {
        String defaultPhase = "";
        URL schemaRef = this.getClass().getResource(ROOT_PKG_PATH + REFINTERGRITY_SCH);
        Source source = new DOMSource(testSubject);
        ETSAssert.assertSchematronValid(schemaRef, source, defaultPhase);
    }

    /**
     * Assert that the given XML Document contain the expected namespace reference,
     * and Assert tha the given XML Document are valid from multiple schema.
     *
     * @param testSubject
     * @param moduleName
     * @param xsdPath
     * @throws Exception TransformerConfigurationException, TransformerException
     */
    protected void assertContainNamespaceNSchemaValid(Document testSubject, String moduleName, String xsdPath) throws Exception {
        ArrayList<String> arrayList = GetToValidateXsdPathArrayList(testSubject);
        boolean nsState = arrayList.contains(xsdPath);
        //----- Assert [namespace]
        Assert.assertTrue(nsState, String.format("Namespace Invalid. (%s)", moduleName));

        String[] arrXsdPath = new String[arrayList.size()];
        arrayList.toArray(arrXsdPath);
        String str = TransformXMLDocumentToXMLString(testSubject);
        boolean result = isMultipleXMLSchemaValid(str, arrXsdPath);
        //----- Assert [xsd]
        Assert.assertTrue(result, String.format("Invalid %s Instance Document", moduleName));
        
    }

    

}
