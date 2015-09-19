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
 * Create new <code>ActivityType</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required to
 * create <code>ActivityType</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityType
 */

public final class ActivityTypeBuilder implements Builder<ActivityType>
{
	/** The Logger */
	private final Logger log;

	/** Helper to substitute <code>ActivitySource</code> instances */
	private final DataStoreProxy<ActivitySource> sourceProxy;

	/** Helper to operate on <code>ActivityType</code> instances*/
	private final DataStoreProxy<ActivityType> typeProxy;

	/** The <code>DataStore</code> id number for the <code>ActivityType</code> */
	private Long id;

	/** The name of the <code>ActivityType</code> */
	private String name;

	/** The <code>ActivitySource</code> */
	private ActivitySource source;

	/**
	 * Create the <code>ActivityTypeBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected ActivityTypeBuilder (final DataStore datastore)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.sourceProxy = DataStoreProxy.getInstance (ActivitySource.class, ActivitySource.SELECTOR_NAME, datastore);
		this.typeProxy = DataStoreProxy.getInstance (ActivityType.class, ActivityType.SELECTOR_NAME, datastore);

		this.id = null;
		this.name = null;
		this.source = null;
	}

	/**
	 * Create an instance of the <code>ActivityType</code>.
	 *
	 * @return                       The new <code>ActivityType</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public ActivityType build ()
	{
		this.log.trace ("build:");

		if (this.name == null)
		{
			this.log.error ("Attempting to create an ActivityType without a name");
			throw new IllegalStateException ("name is NULL");
		}

		if (this.source == null)
		{
			this.log.error ("Attempting to create an ActivityType without an ActivitySource");
			throw new IllegalStateException ("source is NULL");
		}

		ActivityType result = this.typeProxy.create ();
		result.setId (this.id);
		result.setName (this.name);
		result.setSource (this.source);

		return this.typeProxy.insert (result);
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Element</code> to be built to <code>null</code>.
	 *
	 * @return This <code>ActivityTypeBuilder</code>
	 */

	public ActivityTypeBuilder clear ()
	{
		this.log.trace ("clear:");

		this.id = null;
		this.name = null;
		this.source = null;

		return this;
	}

	/**
	 * Load a <code>ActivityType</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>ActivityType</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  type                     The <code>ActivityType</code>, not null
	 *
	 * @return                          This <code>ActivityTypeBuilder</code>
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>ActivityType</code> instance to be
	 *                                  loaded are not valid
	 */

	public ActivityTypeBuilder load (final ActivityType type)
	{
		this.log.trace ("load: type={}", type);

		if (type == null)
		{
			this.log.error ("Attempting to load a NULL ActivityType");
			throw new NullPointerException ();
		}

		this.id = type.getId ();
		this.setName (type.getName ());
		this.setActivitySource (type.getSource ());

		return this;
	}

	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>ActivityType</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>ActivityType</code>, not null
	 *
	 * @return                          This <code>ActivityTypeBuilder</code>
	 * @throws IllegalArgumentException If the name is empty
	 */

	public ActivityTypeBuilder setName (final String name)
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

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	public ActivitySource getActivitySource ()
	{
		return this.source;
	}

	/**
	 * Set the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @param  source                   The <code>ActivitySource</code> for the
	 *                                  <code>ActivityType</code>
	 *
	 * @return                          This <code>ActivityTypeBuilder</code>
	 * @throws IllegalArgumentException If the <code>AcivitySourse</code> does
	 *                                  not exist in the <code>DataStore</code>
	 */

	public ActivityTypeBuilder setActivitySource (final ActivitySource source)
	{
		this.log.trace ("setSource: source={}", source);

		if (source == null)
		{
			this.log.error ("source is NULL");
			throw new NullPointerException ("source is NULL");
		}

		this.source = this.sourceProxy.fetch (source);

		if (this.source == null)
		{
			this.log.error ("The specified ActivitySource does not exist in the DataStore: {}", source);
			throw new IllegalArgumentException ("ActivitySource is not in the DataStore");
		}

		return this;
	}
}
