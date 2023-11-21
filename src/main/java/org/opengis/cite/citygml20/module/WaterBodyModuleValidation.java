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

public class WaterBodyModuleValidation extends CommonFixture {
    public ArrayList<String> docNameSpace;
    @BeforeClass
    public void collectNamespace() {
        docNameSpace = GetToValidateXsdPathArrayList(this.testSubject);
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

 

}
