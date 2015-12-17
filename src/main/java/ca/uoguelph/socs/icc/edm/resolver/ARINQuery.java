/* Copyright (C) 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.resolver;

import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import dagger.Module;
import dagger.Provides;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Retrieve "whois" data from ARIN.  This class retrieves "whois" data using
 * ARIN's RESTful Web-Service API and parses the XML to retrieve the network
 * and organization information.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class ARINQuery implements WhoisQuery
{
	/**
	 * Dagger module to specify the dependency for a <code>ARINQuery</code>
	 * instance.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module
	public static final class ARINQueryModule
	{
		/**
		 * Create a new <code>ARINQuery</code> instance.
		 *
		 * @param  query The <code>ARINQuery</code> instance, not null
		 * @return       The <code>ARINQuery</code> instance
		 */

		@Provides
		public WhoisQuery getQuery (final ARINQuery query)
		{
			return query;
		}
	}

	/** The XML DOM parser */
	private static final DocumentBuilder PARSER;

	/** XPath to get the netblocks */
	private static final XPathExpression NETBLOCKS;

	/** XPath Query to get the base address of the network */
	private static final XPathExpression STARTADDRESS;

	/** XPath Query to get the length of the netmask */
	private static final XPathExpression CIDRLENGTH;

	/** XPath Query to get the name of the organization which owned the network */
	private static final XPathExpression ORGNAME;

	/** XPath Query to get the parent network */
	private static final XPathExpression PARENT;

	/** The Log */
	private final Logger log;

	/**
	 * Static initilaiizer to setup the parser, sice it is constance across all
	 * <code>ARINQuery</code> instances.
	 */

	static
	{
		XPath xpath = XPathFactory.newInstance ().newXPath ();

		try
		{
			PARSER = DocumentBuilderFactory.newInstance ()
				.newDocumentBuilder ();

			NETBLOCKS = xpath.compile ("/net/netBlocks/netBlock");
			STARTADDRESS = xpath.compile ("./startAddress");
			CIDRLENGTH = xpath.compile ("./cidrLength");
			ORGNAME = xpath.compile ("/net/orgRef/@name");
			PARENT = xpath.compile ("/net/parentNetRef");
		}
		catch (ParserConfigurationException ex)
		{
			throw new RuntimeException (ex);
		}
		catch (XPathExpressionException ex)
		{
			throw new RuntimeException (ex);
		}
	}

	@Inject
	ARINQuery ()
	{
		this.log = LoggerFactory.getLogger (this.getClass ());
	}

	/**
	 * Extract the <code>CIDRAddress</code> instances from the document.
	 *
	 * @param  ipData The document to process, not null
	 * @return        The <code>Set</code> of <code>CIDRAddress</code>
	 *                instances
	 */

	private List<CIDRAddress> processNetBlocks (final Document ipData)
	{
		this.log.trace ("processNetBlocks: ipdata={}", ipData);

		assert ipData != null : "ipData is NULL";

		List<CIDRAddress> result = new ArrayList<> ();

		try
		{
			NodeList nl = (NodeList) ARINQuery.NETBLOCKS.evaluate (ipData, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength (); i ++)
			{
				result.add (CIDRAddress.builder ()
						.setAddress (ARINQuery.STARTADDRESS.evaluate (nl.item (i)))
						.setLength (Short.valueOf (ARINQuery.CIDRLENGTH.evaluate (nl.item (i))).shortValue ())
						.build ());
			}
		}
		catch (IOException ex)
		{
			throw new RuntimeException (ex);
		}
		catch (XPathExpressionException ex)
		{
			throw new RuntimeException (ex);
		}

		return result;
	}

	/**
	 * Execute a "whois" query for the specified IP address.
	 *
	 * @param  url The <code>URL</code> for the whois query, not null
	 * @return     The name of the organization which owns the IP Address
	 */

	private List<NetBlock> executeQuery (final URL url)
	{
		this.log.trace ("getOrg: url={}", url);

		assert url != null : "url is NULL";

		List<NetBlock> result = null;

		try
		{
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Accept", "application/xml");

			Document ipData = ARINQuery.PARSER.parse (conn.getInputStream ());

			String parentRef = ARINQuery.PARENT.evaluate (ipData);
			String orgName = ARINQuery.ORGNAME.evaluate (ipData).trim ();

			if (parentRef.length () > 0)
			{
				result = this.executeQuery (new URL (parentRef));
			}

			if ((result == null) || ((orgName.length () > 0) && (! orgName.equals (result.get (0).getOwner ()))))
			{
				result = this.processNetBlocks (ipData)
					.stream ()
					.map (x -> new NetBlock (orgName, x))
					.collect (Collectors.toList ());
			}

			conn.disconnect();
		}
		catch (IOException ex)
		{
			throw new RuntimeException (ex);
		}
		catch (SAXException ex)
		{
			throw new RuntimeException (ex);
		}
		catch (XPathExpressionException ex)
		{
			throw new RuntimeException (ex);
		}

		return result;
	}

	/**
	 * Execute a "whois" query for the specified IP address.
	 *
	 * @param  address The IP address, not null
	 * @return         The name of the organization which owns the IP Address
	 */

	@Override
	public List<NetBlock> getNetBlocks (final CIDRAddress address)
	{
		this.log.trace ("getOrg: address={}", address);

		try
		{
			return this.executeQuery (new URL ("http://whois.arin.net/rest/ip/" + address.getHostAddress ()));
		}
		catch (IOException ex)
		{
			throw new RuntimeException (ex);
		}
	}
}
