package org.opengis.cite.citygml20.module;

import java.util.ArrayList;

import org.apache.xerces.dom.DeferredElementNSImpl;
import org.opengis.cite.citygml20.CommonFixture;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.NodeList;

public class LandUseModuleValidation extends CommonFixture {

	public ArrayList<String> docNameSpace;

	@BeforeClass
	public void collectNamespace() {
		docNameSpace = GetToValidateXsdPathArrayList(this.testSubject);
	}

	/**
	 * Verify that the CityGML instance document follows the LandUse module’s rules for
	 * encoding of objects and properties and adheres to all its conformance requirements.
	 * This test case is mandatory for all CityGML instance documents employing elements
	 * defined within the LandUse module. Conformance requirements on referential
	 * integrity of CityGML property elements defined within the LandUse module may be
	 * additionally validated using the constraints provided by the Schematron schema
	 * referentialIntegrity.sch in accordance with the rules and guidelines stated in
	 * annex A.15.
	 */
	@Test(enabled = true, description = "B.2.8 LandUse module")
	public void verifyLandUseModule() {
		String SchemaPath = XSD_LANDUSE;
		String moduleName = "LandUse";
		String moduleElementName = moduleName;

		NodeList rootElementList = this.testSubject.getChildNodes();

		boolean foundAtLeastOne = false;

		for (int a = 0; a < rootElementList.getLength(); a++) {

			if (rootElementList.item(a)
				.getClass()
				.toString()
				.equals("class org.apache.xerces.dom.DeferredElementNSImpl")) {
				DeferredElementNSImpl element = (DeferredElementNSImpl) rootElementList.item(a);

				if (element.getLocalName().equals("CityModel")
						&& element.getNamespaceURI().equals("http://www.opengis.net/citygml/2.0")) {

					NodeList nodeList = element.getElementsByTagNameNS(
							"http://www.opengis.net/citygml/" + moduleName.toLowerCase() + "/2.0", moduleElementName);
					if (nodeList.getLength() > 0) {
						foundAtLeastOne = true;

					}
				}

			}

		}

		Assert.assertTrue(foundAtLeastOne, "No " + moduleElementName + " element was found in the document.");

	}

}
