/* Copyright (C) 2016 James E. Stark
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.uoguelph.socs.icc.edm.domain.datastore;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Loader for Configuration files.  This class contains the common components
 * used for loading configuration data from XML files.  The configuration data
 * is loaded using a validating DOM parser.  Once the data is loaded is
 * processed via a series of callbacks that are registered prior to calling the
 * <code>load</code> method.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class ConfigLoader
{
	/**
	 * <code>ErrorHandler</code> implementation for parsing the XML documents.
	 * This error handler is used to make sure that the document is loaded
	 * without any errors or warnings.  If the parser encounters an error (or
	 * warning) an exception is thrown.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	private class EH implements ErrorHandler
	{
		/**
		 * Process a recoverable SAX parser error.
		 *
		 * @param exception The error information, not null
		 *
		 * @throws SAXException unconditionally
		 */

		@Override
		public void error (final SAXParseException exception) throws SAXException
		{
			ConfigLoader.this.log.error ("Error loading XML: {}", exception.getMessage ());

			throw exception;
		}

		/**
		 * Process a non-recoverable SAX parser error.
		 *
		 * @param exception The error information, not null
		 *
		 * @throws SAXException unconditionally
		 */

		@Override
		public void fatalError (final SAXParseException exception) throws SAXException
		{
			ConfigLoader.this.log.error ("Error loading XML: {}", exception.getMessage ());

			throw exception;
		}

		/**
		 * Process a SAX parser warning.
		 *
		 * @param exception The warning information, not null
		 *
		 * @throws SAXException unconditionally
		 */

		@Override
		public void warning (final SAXParseException exception) throws SAXException
		{
			ConfigLoader.this.log.error ("Error loading XML: {}", exception.getMessage ());

			throw exception;
		}
	}

	/** The log */
	private final Logger log;

	/** Node name to processing function mapping */
	private final Map<String, Consumer<Node>> elementProcessors;

	/** The parser */
	private final DocumentBuilder parser;

	/**
	 * Create a new <code>ConfigLoader</code> instance.
	 *
	 * @param  schema The schema URL for validation, not null
	 * @return        The <code>ConfigLoader</code>
	 */

	public static ConfigLoader create (final URL schema)
	{
		assert schema != null : "schema is NULL";

		return new ConfigLoader (schema);
	}

	/**
	 * Create the <code>ConfigLoader</code>.
	 *
	 * @param  schema The schema URL for validation, not null
	 */

	private ConfigLoader (final URL schemaURL)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		assert schemaURL != null : "schemaURL is NULL";

		try
		{
			this.elementProcessors = new HashMap<> ();

			Schema schema = SchemaFactory.newInstance (XMLConstants.W3C_XML_SCHEMA_NS_URI)
				.newSchema (schemaURL);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
			factory.setCoalescing (true);
			factory.setIgnoringElementContentWhitespace (true);
			factory.setNamespaceAware (true);
			factory.setSchema (schema);

			this.parser = factory.newDocumentBuilder ();
		}
		catch (SAXException | ParserConfigurationException ex)
		{
			throw new RuntimeException ("Failed to create the parser:", ex);
		}
	}

	/**
	 * Traverse the DOM tree calling the registered handlers for the document
	 * elements.
	 *
	 * @param  node The node to process, not null
	 */

	private void traverse (final Node node)
	{
		this.log.trace ("traverse: node={}", node);

		assert node != null : "node is NULL";

		if (this.elementProcessors.containsKey (node.getNodeName ()))
		{
			this.elementProcessors.get (node.getNodeName ()).accept (node);
		}

		final NodeList nodeList = node.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			if (nodeList.item (i).getNodeType () == Node.ELEMENT_NODE)
			{
				traverse (nodeList.item(i));
			}
		}
	}

	/**
	 * Register a processing callback with the <code>ConfigLoader</code>.
	 *
	 * @param  element   The name of the document element to process, not null
	 * @param  processor The processing method, not null
	 * @return           This <code>ConfigLoader</code>
	 */

	public ConfigLoader registerProcessor (final String element, final Consumer<Node> processor)
	{
		this.log.trace ("registerProcessor: element={}, processor={}", element, processor);

		assert element != null : "element is NULL";
		assert processor != null : "processor is NULL";

		this.elementProcessors.put (element, processor);

		return this;
	}

	/**
	 * Parse the specified configuration file.
	 *
	 * @param  url The URL of the config file to load, not null
	 * @return     The root node of the DOM tree for the config file
	 */

	public void load (final URL url)
	{
		this.log.trace ("load: url={}", url);

		assert url != null : "url is NULL";

		try (InputStream data = url.openStream ())
		{
			this.traverse (this.parser.parse (data).getDocumentElement ());
		}
		catch (SAXException | IOException ex)
		{
			throw new RuntimeException ("Failed to parse the document:", ex);
		}
	}
}
