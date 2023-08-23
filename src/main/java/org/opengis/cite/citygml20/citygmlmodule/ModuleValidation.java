package org.opengis.cite.citygml20.citygmlmodule;

import org.citygml4j.model.module.citygml.AppearanceModule;
import org.citygml4j.model.module.gml.GMLCoreModule;
import org.opengis.cite.citygml20.CommonFixture;
import org.opengis.cite.citygml20.ETSAssert;
import org.opengis.cite.citygml20.util.XMLUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
public class ModuleValidation extends CommonFixture {
    @Test(enabled = false, description = "B.2.1 CityGML Core module")
    public void verifyCityGMLCoreModule() throws Exception {
        //Assert [namespace], [xsd]
        assertContainNamespaceNSchemaValid(this.testSubject,"CityGML Core", XSD_CITYGMLCORE);

        //Assert [Schematron]
        assertSchematronValidWith_RefIntegrity(this.testSubject);
    }

    @Test(enabled = true, description = "B.2.2 Appearance module") //groups = "B.2 Conformance classes related to CityGML modules"
    public void verifyAppearanceModule() throws Exception {
        //Assert [namespace], [xsd]
        assertContainNamespaceNSchemaValid(this.testSubject,"Appearance", XSD_APPEARANCE);
        Set<String> refsSet = new LinkedHashSet<>(
				XMLUtils.getAttributeValue(
						this.testSubject, AppearanceModule.v2_0_0.getNamespaceURI(),"textureCoordinates", "ring"));
		Set<String> sourcesSet = new LinkedHashSet<>(
				XMLUtils.getAttributeValue(
						this.testSubject, GMLCoreModule.v3_1_1.getNamespaceURI(),"LinearRing", "gml:id"));

		boolean state = true;
		for (String value : refsSet)
            if (!sourcesSet.contains(value.substring(1))) {
                state = false; break;
            }

        //Assert [reference
		Assert.assertTrue(state, "Reference invalid");

        //Assert [Schematron]
        assertSchematronValidWith_RefIntegrity(this.testSubject);
    }

    @Test(enabled = false, description = "B.2.4 Building module")
    public void verifyBuildingModule() throws Exception {
        //Assert [namespace], [xsd]
        assertContainNamespaceNSchemaValid(this.testSubject,"Building", XSD_BUILDING);

        //Assert [Schematron]
        assertSchematronValidWith_RefIntegrity(this.testSubject);
    }


    protected void assertSchematronValidWith_RefIntegrity(Document testSubject) {
        String defaultPhase = "";
        URL schemaRef = this.getClass().getResource(REFINTERGRITY_SCH);
        Source source = new DOMSource(testSubject);
        ETSAssert.assertSchematronValid(schemaRef, source, defaultPhase);
    }

    protected void assertContainNamespaceNSchemaValid(Document testSubject, String moduleName, String xsdPath) throws Exception {
        ArrayList<String> arrayList = GetToValidateXsdPathArrayList(this.testSubject);
        boolean nsState = arrayList.contains(xsdPath);
        //Assert [namespace]
        Assert.assertTrue(nsState, String.format("Namespace Invalid. (%s)", moduleName));

        String[] arrXsdPath = new String[arrayList.size()];
        arrayList.toArray(arrXsdPath);
        String str = TransformXMLDocumentToXMLString(testSubject);
        boolean result = isMultipleXMLSchemaValid(str, arrXsdPath);
        //Assert [xsd]
        Assert.assertTrue(result, String.format("Invalid %s Instance Document", moduleName));
    }

}
