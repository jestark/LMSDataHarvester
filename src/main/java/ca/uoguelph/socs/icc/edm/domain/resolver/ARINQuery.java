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

package ca.uoguelph.socs.icc.edm.domain.resolver;

import java.io.InputStream;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

import java.util.Set;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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

final class ARINQuery extends WhoisQuery
{
	/** The XML DOM parser */
	private static final DocumentBuilder parser;

	/** XPath to get the netblocks */
	private static final XPathExpression netBlocks;

	/** XPath Query to get the base address of the network */
	private static final XPathExpression startAddress;

	/** XPath Query to get the length of the netmask */
	private static final XPathExpression cidrLength;

	/** XPath Query to get the name of the organization which owned the network */
	private static final XPathExpression orgName;

	/** XPath Query to get the parent network */
	private static final XPathExpression parent;

	/**
	 * Static initilaiizer to setup the parser, sice it is constance across all
	 * <code>ARINQuery</code> instances.
	 */

	static
	{
		XPath xpath = XPathFactory.newInstance ().newXPath ();

		try
		{
			parser = DocumentBuilderFactory.newInstance ()
				.newDocumentBuilder ();

			netBlocks = xpath.compile ("/net/netBlocks/netBlock");
			startAddress = xpath.compile ("./startAddress");
			cidrLength = xpath.compile ("./cidrLength");
			orgName = xpath.compile ("/net/orgRef/@name");
			parent = xpath.compile ("/net/parentNetRef");
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

	/**
	 * Extract the <code>AddressBlock</code> instances from the document.
	 *
	 * @param  ipData The document to process, not null
	 *
	 * @return        The <code>Set</code> of <code>AddressBlock</code>
	 *                instances
	 */

	private Set<AddressBlock> processNetBlocks (final Document ipData)
	{
		this.log.trace ("processNetBlocks: ipdata={}", ipData);

		assert ipData != null : "ipData is NULL";

		Set<AddressBlock> result = new HashSet<> ();

		AddressBlock.Builder blockBuilder = new AddressBlock.Builder ();

		try
		{
			NodeList nl = (NodeList) ARINQuery.netBlocks.evaluate (ipData, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength (); i ++)
			{
				blockBuilder.setAddress (ARINQuery.startAddress.evaluate (nl.item (i)));
				blockBuilder.setLength (Short.valueOf (ARINQuery.cidrLength.evaluate (nl.item (i))).shortValue ());

				result.add (blockBuilder.build ());
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
	 *
	 * @return     The name of the organization which owns the IP Address
	 */

	private QueryResult executeQuery (final URL url)
	{
		this.log.trace ("getOrg: url={}", url);

		assert url != null : "url is NULL";

		QueryResult result = null;

		try
		{
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Accept", "application/xml");

			Document ipData = ARINQuery.parser.parse (conn.getInputStream ());

			String parentRef = ARINQuery.parent.evaluate (ipData);

			if (parentRef.length () > 0)
			{
				result = this.executeQuery (new URL (parentRef));

				String orgName = ARINQuery.orgName.evaluate (ipData);

				if ((orgName.length () > 0) && (! orgName.equals (result.getName ())))
				{
					result = new QueryResult (orgName, this.processNetBlocks (ipData));
				}
			}
			else
			{
				result = new QueryResult (ARINQuery.orgName.evaluate (ipData), this.processNetBlocks (ipData));
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
	 *
	 * @return         The name of the organization which owns the IP Address
	 */

	@Override
	public QueryResult getOrg (final NetAddress address)
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
