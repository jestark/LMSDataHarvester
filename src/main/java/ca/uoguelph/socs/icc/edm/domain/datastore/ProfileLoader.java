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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;

/**
 * Loader <code>Matcher</code> instances from a configuration file.  This class
 * is used by the <code>SubActivityConverter</code> to load the
 * <code>Matcher</code> instances from a configuration file stored in the
 * resources.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class ProfileLoader
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
			ProfileLoader.this.log.error ("Error loading XML: {}", exception.getMessage ());

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
			ProfileLoader.this.log.error ("Error loading XML: {}", exception.getMessage ());

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
			ProfileLoader.this.log.error ("Error loading XML: {}", exception.getMessage ());

			throw exception;
		}
	}

	/** The location of the XML Schema to use to validate the matcher data */
	private static final String LOCATION_SCHEMA = "/Profile.xsd";

	/** The log */
	private final Logger log;

	/** The <code>Activity</code> class */
	private Class<? extends Element> elementClass;

	private final Profile.Builder builder;

	/**
	 * Create the <code>MatcherLoader</code>.
	 */

	ProfileLoader ()
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.elementClass = null;

		this.builder = Profile.builder ();
	}

	/**
	 * Load an <code>SubActivity</code> class.  The name of the package will be
	 * pre-pended to the name of the class if the class name is unqualified.
	 *
	 * @param  name The name of the <code>SubActivity</code> class, not null
	 *
	 * @throws SAXException If the specified class was not found, or is not a
	 *                      subclass of <code>SubActivity</code>
	 */

	@SuppressWarnings ("unchecked")
	private Class<? extends Element> processElementClass (
			final Class<? extends Element> superclass,
			final String name) throws SAXException
	{
		this.log.trace ("processElementClass: superclass={}, name={}", superclass, name);

		assert superclass != null : "superclass is NULL";
		assert name != null : "name is NULL";

		try
		{
			Class<?> element = Class.forName (name);

			if (! superclass.isAssignableFrom (element))
			{
				throw new SAXException ("SubActivity class is not a subclass of SubActivity");
			}

			return (Class<? extends Element>) element;
		}
		catch (ClassNotFoundException ex)
		{
			throw new SAXException ("SubActivity class does not exist", ex);
		}
	}

	@SuppressWarnings ("unchecked")
	private Class<? extends IdGenerator> processGeneratorClass (
			final String name) throws SAXException
	{
		this.log.trace ("processGeneratorClass: name={}", name);

		assert name != null : "name is NULL";

		try
		{
			Class<?> element = Class.forName (name);

			if (! IdGenerator.class.isAssignableFrom (element))
			{
				throw new SAXException ("SubActivity class is not a subclass of SubActivity");
			}

			return (Class<? extends IdGenerator>) element;
		}
		catch (ClassNotFoundException ex)
		{
			throw new SAXException ("SubActivity class does not exist", ex);
		}
	}


	/**
	 * Perform a depth-first traversal of the DOM tree creating the
	 * <code>Matcher</code> instances.
	 *
	 * @param  node The root node of the DOM tree, not null
	 *
	 * @throws SAXException if there are any errors processing the elements in
	 *                      the DOM tree
	 */

	private void traverse (final Node node) throws SAXException
	{
		this.log.trace ("traverse: node={}", node);

		final NodeList nodeList = node.getChildNodes();

		if (node.getNodeName ().equals ("name"))
		{
			this.builder.setName (nodeList.item (0).getNodeValue ());
		}
		else if (node.getNodeName ().equals ("mutable"))
		{
			this.builder.setMutable ((nodeList.item (0).getNodeValue ().equalsIgnoreCase ("true")) ? true : false);
		}
		else if (node.getNodeName ().equals ("parameter"))
		{
			this.builder.setParameter (node.getAttributes ().getNamedItem ("name").getNodeValue (),
					nodeList.item (0).getNodeValue ());
		}
		else if (node.getNodeName ().equals ("element"))
		{
			this.elementClass = this.processElementClass (Element.class, node.getAttributes ().getNamedItem ("class").getNodeValue ());
		}
		else if (node.getNodeName ().equals ("generator"))
		{
			this.builder.setGenerator (this.elementClass, this.processGeneratorClass (nodeList.item (0).getNodeValue ()));
		}
		else if (node.getNodeName ().equals ("implementation"))
		{
			this.builder.setElement (Profile.ELEMENT_DEFINITIONS.get (this.processElementClass (this.elementClass, nodeList.item (0).getNodeValue ())));
		}

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			traverse (nodeList.item(i));
		}
	}

	/**
	 * Load the <code>Matcher</code> instances.
	 *
	 * @return A <code>List</code> of <code>Matcher</code> instances
	 *
	 * @throws IllegalStateException if any problems are encountered loading or
	 *                               processing the <code>matcher</code> data
	 */

	public Profile load (final File file)
	{
		this.log.trace ("load");

		try (InputStream data = new FileInputStream (file))
		{
			Schema schema = SchemaFactory.newInstance (XMLConstants.W3C_XML_SCHEMA_NS_URI)
				.newSchema (this.getClass ().getResource (ProfileLoader.LOCATION_SCHEMA));

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
			factory.setCoalescing (true);
			factory.setIgnoringElementContentWhitespace (true);
			factory.setNamespaceAware (true);
			factory.setSchema (schema);

			DocumentBuilder parser = factory.newDocumentBuilder ();
			parser.setErrorHandler (new EH ());

			Document document = parser.parse (data);

			traverse (document.getDocumentElement ());

			return this.builder.build ();
		}
		catch (SAXException ex)
		{
			throw new IllegalStateException (ex);
		}
		catch (IOException ex)
		{
			throw new IllegalStateException (ex);
		}
		catch (ParserConfigurationException ex)
		{
			throw new IllegalStateException (ex);
		}
	}
}
