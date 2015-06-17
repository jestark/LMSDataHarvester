/* Copyright (C) 2014, 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.Property;

/**
 * Default implementation of the <code>ActivitySourceBuilder</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActivitySourceBuilder extends AbstractBuilder<ActivitySource> implements ActivitySourceBuilder
{
	/** The "id" <code>Property</code> */
	private final Property<Long> ID;

	/** The "name" <code>Property</code> */
	private final Property<String> NAME;

	/**
	 * static initializer to register the
	 * <code>DefaultActivitySourceBuilder</code> with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (DefaultActivitySourceBuilder.class, DefaultActivitySourceBuilder::new);
	}

	/**
	 * Create the <code>DefaultActivitySourceBuilder</code>.
	 *
	 * @param  impl      The implementation class of the <code>Element</code>
	 *                   to be built
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>ActivitySource</code> instance will be
	 *                   inserted
	 */

	protected DefaultActivitySourceBuilder (final Class<?> impl, final DataStore datastore)
	{
		super (impl, datastore);

		this.ID = this.builder.getProperty ("id", Long.class);
		this.NAME = this.builder.getProperty ("name", String.class);
	}

	/**
	 * Load a <code>ActivitySource</code> instance into the
	 * <code>ActivitySourceBuilder</code>.  This method resets the
	 * <code>ActivitySourceBuilder</code> and initializes all of its parameters
	 * from the specified <code>ActivitySource</code> instance.  The parameters
	 * are validated as they are set.
	 *
	 * @param  source                   The <code>ActivitySource</code> to load
	 *                                  into the
	 *                                  <code>ActivitySourceBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>ActivitySource</code> instance to
	 *                                  be loaded are not valid
	 */

	@Override
	public void load (final ActivitySource source)
	{
		this.log.trace ("load: source={}", source);

		if (source == null)
		{
			this.log.error ("Attempting to load a NULL ActivitySource");
			throw new NullPointerException ();
		}

		super.load (source);
		this.setName (source.getName ());

		this.builder.setProperty (this.ID, source.getId ());
	}

	/**
	 * Get the name of the <code>ActivitySource</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivitySource</code>
	 */

	@Override
	public String getName ()
	{
		return this.builder.getPropertyValue (this.NAME);
	}

	/**
	 * Set the name of the <code>ActivitySource</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>ActivitySource</code>, not null
	 *
	 * @throws IllegalArgumentException If the name is empty
	 */

	@Override
	public void setName (final String name)
	{
		this.log.trace ("setName: name={}", name);

		if (name == null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("name is NULL");
		}

		if (name.length () == 0)
		{
			this.log.error ("name is an empty string");
			throw new IllegalArgumentException ("name is empty");
		}

		this.builder.setProperty (this.NAME, name);
	}
}
