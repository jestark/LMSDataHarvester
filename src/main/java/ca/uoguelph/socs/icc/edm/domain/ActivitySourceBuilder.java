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

package ca.uoguelph.socs.icc.edm.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create new <code>ActivitySource</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required
 * to create <code>ActivitySource</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivitySource
 */

public final class ActivitySourceBuilder implements Builder<ActivitySource>
{
	/** The Logger */
	private final Logger log;

	/** Helper to operate on <code>ActivitySource</code> instances*/
	private final DataStoreProxy<ActivitySource> sourceProxy;

	/** The <code>DataStore</code> id number for the <code>ActivitySource</code> */
	private Long id;

	/** The name of the <code>ActivitySource</code> */
	private String name;

	/**
	 * Create the <code>ActivitySourceBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected ActivitySourceBuilder (final DataStore datastore)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.sourceProxy = DataStoreProxy.getInstance (ActivitySource.class, ActivitySource.SELECTOR_NAME, datastore);

		this.id = null;
		this.name = null;
	}

	/**
	 * Create an instance of the <code>ActivitySource</code>.
	 *
	 * @return                       The new <code>ActivitySource</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public ActivitySource build ()
	{
		this.log.trace ("build:");

		if (this.name == null)
		{
			this.log.error ("Attempting to create an ActivitySource without a name");
			throw new IllegalStateException ("name is NULL");
		}

		ActivitySource result = this.sourceProxy.create ();
		result.setId (this.id);
		result.setName (this.name);

		return this.sourceProxy.insert (null, result);
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Element</code> to be built to <code>null</code>.
	 *
	 * @return This <code>ActivitySourceBuilder</code>
	 */

	public ActivitySourceBuilder clear ()
	{
		this.log.trace ("clear:");

		this.id = null;
		this.name = null;

		return this;
	}

	/**
	 * Load a <code>ActivitySource</code> instance into the builder.  This
	 * method resets the builder and initializes all of its parameters from
	 * the specified <code>ActivitySource</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  source                   The <code>ActivitySource</code>, not
	 *                                  null
	 *
	 * @return                          This <code>ActivitySourceBuilder</code>
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>ActivitySource</code> instance to
	 *                                  be loaded are not valid
	 */

	public ActivitySourceBuilder load (final ActivitySource source)
	{
		this.log.trace ("load: source={}", source);

		if (source == null)
		{
			this.log.error ("Attempting to load a NULL ActivitySource");
			throw new NullPointerException ();
		}

		this.id = source.getId ();
		this.setName (source.getName ());

		return this;
	}

	/**
	 * Get the name of the <code>ActivitySource</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivitySource</code>
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>ActivitySource</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>ActivitySource</code>, not null
	 *
	 * @return                          This <code>ActivitySourceBuilder</code>
	 * @throws IllegalArgumentException If the name is empty
	 */

	public ActivitySourceBuilder setName (final String name)
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

		this.name = name;

		return this;
	}
}
