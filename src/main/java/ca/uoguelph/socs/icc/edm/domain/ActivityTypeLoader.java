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

import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Load <code>ActivityType</code> instances from the <code>DataStore</code>.
 * This class extends <code>AbstractLoader</code>, adding the functionality
 * required to handle <code>ActivityType</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class ActivityTypeLoader extends AbstractLoader<ActivityType>
{
	/**
	 * Get an instance of the <code>ActivityLoader</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>ActivityLoader</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Element</code> queried by the
	 *                               loader
	 */

	public static ActivityTypeLoader getInstance (final DomainModel model)
	{
		return AbstractLoader.getInstance (model, ActivityType.class, ActivityTypeLoader::new);
	}

	/**
	 * Create the <code>ActivityTypeLoader</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	public ActivityTypeLoader (final DataStore datastore)
	{
		super (ActivityType.class, datastore);
	}

	/**
	 * Retrieve an <code>Element</code> instance from the
	 * <code>DataStore</code> based on its <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Element</code> to retrieve, not null
	 *
	 * @return    The requested <code>Element</code>
	 */

	public ActivityType fetchById (final Long id)
	{
		this.log.trace ("fetchById: id={}", id);

		if (id == null)
		{
			this.log.error ("Attempting to fetch an element with a NULL id");
			throw new NullPointerException ();
		}

		return this.getQuery (ActivityType.SELECTOR_ID)
			.setValue (ActivityType.ID, id)
			.query ();
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	public List<ActivityType> fetchAll ()
	{
		this.log.trace ("fetchAll:");

		return this.getQuery (ActivityType.SELECTOR_ALL)
			.queryAll ();
	}

	/**
	 * Retrieve the <code>ActivityType</code> object from the
	 * <code>DataStore</code> which has the specified
	 * <code>ActivitySource</code> and name.
	 *
	 * @param  source                The <code>ActivitySource</code>, not null
	 * @param  name                  The name of the <code>ActivityType</code>,
	 *                               not null
	 *
	 * @return                       The <code>ActivityType</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public ActivityType fetchByName (final ActivitySource source, final String name)
	{
		this.log.trace ("fetchByName source={}, name={}", source, name);

		if (source == null)
		{
			this.log.error ("The specified ActivitySource is NULL");
			throw new NullPointerException ();
		}

		if (name == null)
		{
			this.log.error ("The specified ActivityType name is NULL");
			throw new NullPointerException ();
		}

		return this.getQuery (ActivityType.SELECTOR_NAME)
			.setValue (ActivityType.SOURCE, source)
			.setValue (ActivityType.NAME, name)
			.query ();
	}
}
