package org.opengis.cite.citygml20;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
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

import org.opengis.cite.citygml20.util.ClientUtils;
import org.opengis.cite.citygml20.util.ValidationUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.w3c.dom.Document;
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
	 * @param xmlDoc
	 * @throws Exception
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
	 * @param xmlDoc
	 * @throws Exception
	 */
	public void prettyPrint(Document xmlDoc) throws Exception {
		String str = TransformXMLDocumentToXMLString(xmlDoc);
		System.out.println(str);
	}
	
	/**
	 * Description: Identify that a XML document is valid with XSD Template or not
	 * 
	 * @param xmlString
	 * @param xsdPath
	 * @return
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

}
