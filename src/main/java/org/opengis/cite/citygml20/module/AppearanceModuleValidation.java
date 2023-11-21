package org.opengis.cite.citygml20.module;

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


}
