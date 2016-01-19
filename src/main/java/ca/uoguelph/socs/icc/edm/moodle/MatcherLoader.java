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

package ca.uoguelph.socs.icc.edm.moodle;

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

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

/**
 * Loader <code>Matcher</code> instances from a configuration file.  This class
 * is used by the <code>SubActivityConverter</code> to load the
 * <code>Matcher</code> instances from a configuration file stored in the
 * resources.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class MatcherLoader
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
			MatcherLoader.this.log.error ("Error loading XML: {}", exception.getMessage ());

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
			MatcherLoader.this.log.error ("Error loading XML: {}", exception.getMessage ());

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
			MatcherLoader.this.log.error ("Error loading XML: {}", exception.getMessage ());

			throw exception;
		}
	}

	/** The location of the XML Schema to use to validate the matcher data */
	private static final String LOCATION_SCHEMA = "/Matchers.xsd";

	/** The location of the XML file containing the matcher data */
	private static final String LOCATION_DATA = "/Matchers.xml";

	/** The name of the element containing the matchers for a Activity to SubActivity mapping in the XML */
	private static final String ELEMENT_MATCHER = "matcher";

	/** The name of the element containing the matchers for a package in the XML */
	private static final String ELEMENT_MATCHERS = "matchers";

	/** The name of the element describing an action in the XML */
	private static final String ELEMENT_ACTION = "action";

	/** The name of the element describing a URL in the XML */
	private static final String ELEMENT_URL = "url";

	/** The name of the (optional) attribute containing the package name in the XML */
	private static final String ATTRIBUTE_PACKAGE = "package";

	/** The name of the attribute containing the Activity class in the XML */
	private static final String ATTRIBUTE_ACTIVITY = "activity";

	/** The name of the attribute containing the SubActivity class in the XML */
	private static final String ATTRIBUTE_SUBACTIVITY = "subActivity";

	/** The log */
	private final Logger log;

	/** The <code>List</code> of loaded <code>Matcher</code> instances */
	private final List<Matcher> matchers;

	/** The name of the package containing the <code>Activity</code> and <code>SubActivity</code> classes */
	private String packageName;

	/** The <code>Activity</code> class */
	private Class<? extends Activity> activityClass;

	/** The <code>SubActivity</code> class */
	private Class<? extends SubActivity> subActivityClass;

	/**
	 * Create the <code>MatcherLoader</code>.
	 */

	MatcherLoader ()
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.matchers = new ArrayList<> ();

		this.packageName = "";
		this.activityClass = null;
		this.subActivityClass = null;
	}

	/**
	 * Load an <code>Activity</code> class.  The name of the package will be
	 * pre-pended to the name of the class if the class name is unqualified.
	 *
	 * @param  name The name of the <code>Activity</code> class, not null
	 *
	 * @throws SAXException If the specified class was not found, or is not a
	 *                      subclass of <code>Activity</code>
	 */

	@SuppressWarnings ("unchecked")
	private void processActivityClass (final String name) throws SAXException
	{
		this.log.trace ("processActivityClass: name={}", name);

		assert name != null : "name is NULL";

		try
		{
			Class<?> activity = Class.forName ((name.indexOf ('.') == -1)
					? this.packageName + "." + name
					: name);

			if (Activity.class.isAssignableFrom (activity))
			{
				this.activityClass = (Class<? extends Activity>) activity;
			}
			else
			{
				throw new SAXException ("Activity class is not a subclass of Activity");
			}
		}
		catch (ClassNotFoundException ex)
		{
			throw new SAXException ("Activity class does not exist", ex);
		}
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
	private void processSubActivityClass (final String name) throws SAXException
	{
		this.log.trace ("processSubActivityClass: name={}", name);

		assert name != null : "name is NULL";

		try
		{
			Class<?> subactivity = Class.forName ((name.indexOf ('.') == -1)
					? this.packageName + "." + name
					: name);

			if (SubActivity.class.isAssignableFrom (subactivity))
			{
				this.subActivityClass = (Class<? extends SubActivity>) subactivity;
			}
			else
			{
				throw new SAXException ("SubActivity class is not a subclass of SubActivity");
			}
		}
		catch (ClassNotFoundException ex)
		{
			throw new SAXException ("SubActivity class does not exist", ex);
		}
	}

	/**
	 * Create an <code>ActionMatcher</code> for the specified action name.  This
	 * method uses the specified action name to create an
	 * <code>ActionMatcher</code> for the previously loaded
	 * <code>Activity</code> and <code>SubActivity</code> classes.
	 *
	 * @param  action The name of the <code>Action</code>, not null
	 *
	 * @see ActionMatcher
	 */

	private void processAction (final String action)
	{
		this.log.trace ("processAction: action={}", action);

		assert action != null : "action is NULL";

		this.matchers.add (ActionMatcher.create (this.activityClass, this.subActivityClass, action));
	}

	/**
	 * Create a <code>URLMatcher</code> for the specified pattern.  This
	 * method uses the specified pattern to create a <code>URLMatcher</code> for
	 * the previously loaded <code>Activity</code> and <code>SubActivity</code>
	 * classes.
	 *
	 * @param  pattern The pattern to match in the URL, not null
	 *
	 * @see URLMatcher
	 */

	private void processURL (final String pattern)
	{
		this.log.trace ("processURL: pattern={}", pattern);

		assert pattern != null : "pattern is NULL";

		this.matchers.add (URLMatcher.create (this.activityClass, this.subActivityClass, pattern));
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

		if (MatcherLoader.ELEMENT_MATCHERS.equals (node.getNodeName ()))
		{
			if (node.hasAttributes ())
			{
				this.packageName = node.getAttributes ().getNamedItem (MatcherLoader.ATTRIBUTE_PACKAGE).getNodeValue ();
			}
		}
		else if (MatcherLoader.ELEMENT_MATCHER.equals (node.getNodeName ()))
		{
			this.processActivityClass (node.getAttributes ()
					.getNamedItem (MatcherLoader.ATTRIBUTE_ACTIVITY)
					.getNodeValue ());

			this.processSubActivityClass (node.getAttributes ()
					.getNamedItem (MatcherLoader.ATTRIBUTE_SUBACTIVITY)
					.getNodeValue ());
		}
		else if (MatcherLoader.ELEMENT_ACTION.equals (node.getNodeName ()))
		{
			this.processAction (nodeList.item (0).getNodeValue ());
		}
		else if (MatcherLoader.ELEMENT_URL.equals (node.getNodeName ()))
		{
			this.processURL (nodeList.item (0).getNodeValue ());
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

	public List<Matcher> load ()
	{
		this.log.trace ("load");

		try (InputStream data = this.getClass ().getResourceAsStream (MatcherLoader.LOCATION_DATA))
		{
			Schema schema = SchemaFactory.newInstance (XMLConstants.W3C_XML_SCHEMA_NS_URI)
				.newSchema (this.getClass ().getResource (MatcherLoader.LOCATION_SCHEMA));

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
			factory.setCoalescing (true);
			factory.setIgnoringElementContentWhitespace (true);
			factory.setNamespaceAware (true);
			factory.setSchema (schema);

			DocumentBuilder parser = factory.newDocumentBuilder ();
			parser.setErrorHandler (new EH ());

			Document document = parser.parse (data);

			traverse (document.getDocumentElement ());

			return this.matchers;
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
