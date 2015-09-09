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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

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

	/** XPath Query to get the base address of the network */
	private static final XPathExpression startAddress;

	/** XPath Query to get the length of the netmask */
	private static final XPathExpression cidrLength;

	/** XPath Query to get the name of the organization which owned the network */
	private static final XPathExpression orgName;

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

			startAddress = xpath.compile ("/net/netBlocks/netBlock/startAddress");
			cidrLength = xpath.compile ("/net/netBlocks/netBlock/cidrLength");
			orgName = xpath.compile ("/net/orgRef/@name");
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
	 * Create the <code>ARINQuery</code>.
	 *
	 * @param  cache The cache, not null
	 */

	public ARINQuery (final AddressCache cache)
	{
		super (cache);
	}

	/**
	 * Execute a "whois" query for the specified IP address.
	 *
	 * @param  address The IP address, not null
	 *
	 * @return         The name of the organization which owns the IP Address
	 */

	@Override
	public String getOrg (final NetAddress address)
	{
		this.log.trace ("getOrg: address={}", address);

		AddressBlock.Builder blockBuilder = new AddressBlock.Builder ();
		String orgName = null;

		try
		{
			URL url = new URL ("http://whois.arin.net/rest/ip/" + address.getHostAddress ());
			this.log.debug ("Executing Query: {}", url);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Accept", "application/xml");

			Document ipData = ARINQuery.parser.parse (conn.getInputStream ());

			blockBuilder.setAddress (ARINQuery.startAddress.evaluate (ipData));
			blockBuilder.setLength (Short.valueOf (ARINQuery.cidrLength.evaluate (ipData)).shortValue ());
			orgName = ARINQuery.orgName.evaluate (ipData);

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

		this.cache.addOrg (blockBuilder.build (), orgName);

		return orgName;
	}
}
