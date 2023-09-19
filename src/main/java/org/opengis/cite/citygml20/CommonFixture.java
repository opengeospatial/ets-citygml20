package org.opengis.cite.citygml20;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.citygml4j.model.module.citygml.*;
import org.opengis.cite.citygml20.util.ClientUtils;
import org.opengis.cite.citygml20.util.ValidationUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * A supporting base class that sets up a common test fixture. These
 * configuration methods are invoked before those defined in a subclass.
 */
public class CommonFixture {

    /**
     * Root test suite package (absolute path).
     */
    public static final String ROOT_PKG_PATH = "/org/opengis/cite/citygml20/";
    /**
     * HTTP client component (JAX-RS Client API).
     */
    protected Client client;
    /**
     * An HTTP request message.
     */
    protected ClientRequest request;
    /**
     * An HTTP response message.
     */
    protected ClientResponse response;

	protected Document testSubject;

	/**
	 * Define XSD path
	 */
	protected static final String REFINTERGRITY_SCH = "sch/referentialIntegrity.sch";
	protected static final String XSD_APPEARANCE = "xsd/opengis/citygml/appearance/2.0/appearance.xsd";
	protected static final String XSD_BRIDGE = "xsd/opengis/citygml/bridge/2.0/bridge.xsd";
	protected static final String XSD_BUILDING = "xsd/opengis/citygml/building/2.0/building.xsd";
	protected static final String XSD_CITYFURNITURE = "xsd/opengis/citygml/cityfurniture/2.0/cityFurniture.xsd";
	protected static final String XSD_CITYGMLCORE = "xsd/opengis/citygml/2.0/cityGMLBase.xsd";
	protected static final String XSD_CITYOBJECTGROUP = "xsd/opengis/citygml/cityobjectgroup/2.0/cityObjectGroup.xsd";

    /**
     * Initializes the common test fixture with a client component for 
     * interacting with HTTP endpoints.
     *
     * @param testContext The test context that contains all the information for
     * a test run, including suite attributes.
     */
    @BeforeClass
    public void initCommonFixture(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(SuiteAttribute.CLIENT.getName());
        if (null != obj) {
            this.client = Client.class.cast(obj);
        }
        obj = testContext.getSuite().getAttribute(SuiteAttribute.TEST_SUBJECT.getName());
        if (null == obj) {
            throw new SkipException("Test subject not found in ITestContext.");
        }
		if (Document.class.isAssignableFrom(obj.getClass())) {
			this.testSubject = Document.class.cast(obj);
		}
    }

    @BeforeMethod
    public void clearMessages() {
        this.request = null;
        this.response = null;
    }

    /**
     * Obtains the (XML) response entity as a DOM Document. This convenience
     * method wraps a static method call to facilitate unit testing (Mockito
     * workaround).
     *
     * @param response A representation of an HTTP response message.
     * @param targetURI The target URI from which the entity was retrieved (may
     * be null).
     * @return A Document representing the entity.
     *
     * @see ClientUtils#getResponseEntityAsDocument
     */
    public Document getResponseEntityAsDocument(ClientResponse response,
            String targetURI) {
        return ClientUtils.getResponseEntityAsDocument(response, targetURI);
    }

    /**
     * Builds an HTTP request message that uses the GET method. This convenience
     * method wraps a static method call to facilitate unit testing (Mockito
     * workaround).
     *
     * @param endpoint A URI indicating the target resource.
     * @param qryParams A Map containing query parameters (may be null);
     * @param mediaTypes A list of acceptable media types; if not specified,
     * generic XML ("application/xml") is preferred.
     * @return A ClientRequest object.
     *
     * @see ClientUtils#buildGetRequest
     */
    public ClientRequest buildGetRequest(URI endpoint,
            Map<String, String> qryParams, MediaType... mediaTypes) {
        return ClientUtils.buildGetRequest(endpoint, qryParams, mediaTypes);
    }

	/**
	 * Transform XML Document to UTF-8 String
	 * @param xmlDoc The XML Document
	 * @return A String data type of XML Document
	 * @throws Exception TransformerConfigurationException, TransformerException
	 */
	public String TransformXMLDocumentToXMLString(Document xmlDoc) throws Exception {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(xmlDoc), new StreamResult(out));
		return out.toString();
	}

	
	/**
	 * Description: Identify that a XML document is valid with XSD Template or not
	 * @param xmlString The XML String
	 * @param arrXsdPath A String Array of XSD Path
	 * @return A boolean value indicates whether the XML document is valid with XSD Template or not
	 */
	public boolean isMultipleXMLSchemaValid(String xmlString, String[] arrXsdPath) {
		try {
			Schema schema = ValidationUtils.createMultipleSchema(arrXsdPath);
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new StringReader(xmlString)));
			
		} catch (IOException | SAXException e) {
			System.out.println("Exception: " + e.getMessage());
			return false;
		}
		return true;
	}

	protected ArrayList<String> GetToValidateXsdPathArrayList(Document doc){
		//
		HashMap<String, String> hashMap = new LinkedHashMap<String, String>();
		hashMap.put(CoreModule.v2_0_0.getNamespaceURI(), "xsd/opengis/citygml/2.0/cityGMLBase.xsd");
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
		Element rootElement = doc.getDocumentElement();
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
