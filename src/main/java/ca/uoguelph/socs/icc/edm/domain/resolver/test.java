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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class test
{
	public static final String ADDRESS = "/net/netBlocks/netBlock/startAddress";

	public static final String CIDRLEN = "/net/netBlocks/netBlock/cidrLength";

	public static final String ORGNAME = "/net/orgRef/@name";

	private final Logger log;

	private final DocumentBuilder parser;

	private final XPathExpression startAddress;

	private final XPathExpression cidrLength;

	private final XPathExpression orgName;

	public test ()
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		XPath xpath = XPathFactory.newInstance ().newXPath ();

		try
		{
			this.parser = DocumentBuilderFactory.newInstance ()
				.newDocumentBuilder ();

			this.startAddress = xpath.compile (test.ADDRESS);
			this.cidrLength = xpath.compile (test.CIDRLEN);
			this.orgName = xpath.compile (test.ORGNAME);
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

	public void parse (final InputStream is)
	{
		try
		{
			Document ipData = this.parser.parse (is);

			System.out.println (this.startAddress.evaluate (ipData));
			System.out.println (this.cidrLength.evaluate (ipData));
			System.out.println (this.orgName.evaluate (ipData));
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
	}

	public static void main (final String[] args) throws Exception
	{
		test processor = new test ();

		URL url = new URL ("http://whois.arin.net/rest/ip/131.104.49.23");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Accept", "application/xml");

		processor.parse (conn.getInputStream ());

		conn.disconnect();
	}
}
