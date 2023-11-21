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

public class MandatoryRequirementsValidation extends CommonFixture {
    public ArrayList<String> docNameSpace;
    @BeforeClass
    public void collectNamespace() {
        docNameSpace = GetToValidateXsdPathArrayList(this.testSubject);
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
