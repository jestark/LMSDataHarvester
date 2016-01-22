/* Copyright (C) 2014, 2015, 2016 James E. Stark
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

package ca.uoguelph.socs.icc.edm;

import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.datastore.ConfigLoader;
import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;
import ca.uoguelph.socs.icc.edm.domain.datastore.jpa.JPADataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.memory.MemDataStore;
import ca.uoguelph.socs.icc.edm.moodle.Extractor;

public final class Harvester
{
	/** The log */
	private final Logger log;

	/** The config file loader */
	private final ConfigLoader loader;

	/** The <code>Profile</code> instances for the data-sets */
	private final Map<String, Profile> profiles;

	/** The registrations */
	private final Map<String, URL> registrations;

	/** The course ID number */
	private Long courseId;

	/**
	 * The main program.  This method confirms the existence of the config file,
	 * then creates the <code>Harvester</code> and has it extract the input data
	 * then write the data out to the destination data-store.
	 *
	 * @param  args  The program arguments.  It expects a single file name
	 */

    public static void main(final String[] args) throws Exception
    {
		Preconditions.checkArgument (args.length == 1, "Expected one argument");

		File input = new File (args[0]);

		Preconditions.checkArgument (input.canRead (), "Input file is not readable");

		Harvester harvester = new Harvester (input.toURI ().toURL ());
		harvester.extract ();
//		harvester.store ();
	}

	/**
	 * Create the <code>Harvester</code>.  
	 *
	 * @param  url The URL for the configuration file, not null
	 */

	private Harvester (final URL url)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		assert url != null : "url is NULL";

		this.courseId = Long.valueOf (0);
		this.profiles = new HashMap<> ();
		this.registrations = new HashMap<> ();

		this.loader = ConfigLoader.create (this.getClass ().getResource ("/Harvester.xsd"))
			.registerProcessor ("datastore", (n -> this.processDataStore (n)))
			.registerProcessor ("course", (n -> this.processCourse (n)))
			.registerProcessor ("registration", (n -> this.processRegistration (n)));

		this.loader.load (url);
	}

	/**
	 * Process a datastore configuration element.
	 *
	 * @param  node The DOM tree node for the datastore, not null
	 */

	private void processDataStore (final Node node)
	{
		this.log.trace ("processDataStore: node={}", node);

		assert node != null : "node is NULL";

		try
		{
			this.profiles.put (node.getAttributes ().getNamedItem ("type").getNodeValue (),
					Profile.load (new URL (node.getChildNodes ().item (0).getNodeValue ())));
		}
		catch (MalformedURLException ex)
		{
			throw new RuntimeException ("Failed to load profile:", ex);
		}
	}

	/**
	 * Process a course configuration element.
	 *
	 * @param  node The DOM tree node for the course, not null
	 */

	private void processCourse (final Node node)
	{
		this.log.trace ("processCourse: node={}", node);

		assert node != null : "node is NULL";

		this.courseId = Long.valueOf (node.getAttributes ().getNamedItem ("id").getNodeValue ());
	}

	/**
	 * Process a registration configuration element.
	 *
	 * @param  node The DOM tree node for the registration, not null
	 */

	private void processRegistration (final Node node)
	{
		this.log.trace ("processRegistration: node={}", node);

		assert node != null : "node is NULL";

		try
		{
			this.registrations.put (node.getAttributes ().getNamedItem ("role").getNodeValue (),
					new URL (node.getChildNodes ().item (0).getNodeValue ()));
		}
		catch (MalformedURLException ex)
		{
			throw new RuntimeException ("Failed to load registration data:", ex);
		}
	}

	/**
	 * Extract the course data from the input data-store.
	 *
	 * @return A <code>DomainModel</code> containing the course data
	 */

	public DomainModel extract ()
	{
		this.log.trace ("extract:");

		this.log.info ("Extracting data from the input data-store");

		DomainModel model = MemDataStore.create (this.profiles.get ("scratch"));

		try (Extractor extractor = Extractor.create (JPADataStore.create (this.profiles.get ("input"))))
		{
			this.registrations.entrySet ()
				.stream ()
				.forEach (r -> extractor.addRegistrations (r.getKey (), r.getValue ()));

			extractor.setCourse (this.courseId)
				.addRegistration ("admin", "admin")
				.extract (model);
		}

		this.log.info ("Data extraction complete");

		return model;
	}

	/**
	 * Write the contents of the specified <code>DomainModel</code> out to the
	 * output <code>DataStore</code>
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 */

	public void store (final DomainModel model)
	{
		this.log.trace ("store:");

		this.log.info ("Writing data to the output data-store");

		try (DomainModel coursedb = JPADataStore.create (this.profiles.get ("output")))
		{
			coursedb.getSynchronizer ()
				.addAll (model.getQuery (User.SELECTOR_ALL)
						.queryAll ())
				.synchronize ();
		}

		this.log.info ("Data output complete");
	}
}
