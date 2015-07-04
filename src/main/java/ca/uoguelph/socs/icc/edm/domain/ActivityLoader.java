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
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

import ca.uoguelph.socs.icc.edm.domain.element.AbstractActivity;

/**
 * Load <code>Activity</code> instances from the <code>DataStore</code>.  This
 * class extends <code>AbstractLoader</code>, adding the functionality
 * required to handle <code>Activity</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class ActivityLoader extends AbstractLoader<Activity>
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

	public static ActivityLoader getInstance (final DomainModel model)
	{
		return AbstractLoader.getInstance (model, Activity.class, ActivityLoader::new);
	}

	/**
	 * Create the <code>ActivityLoader</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActivityLoader</code> will operate, not
	 *                   null
	 */

	protected ActivityLoader (final DataStore datastore)
	{
		super (Activity.class, datastore);
	}

	/**
	 * Get a <code>List</code> of all of the <code>Activity</code> instances
	 * which are associated with a particular <code>ActivityType</code>.
	 *
	 * @param  type                  The <code>ActivityType</code>, not null
	 *
	 * @return                       The <code>List</code> of
	 *                               <code>ActivityType</code> instances
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public List<Activity> fetchAllForType (final ActivityType type)
	{
		this.log.trace ("fetchAllForType: type={}", type);

		if (type == null)
		{
			this.log.error ("The specified ActivityType is NULL");
			throw new NullPointerException ();
		}

		if (! this.datastore.isOpen ())
		{
			this.log.error ("Attempting to Query a closed datastore");
			throw new IllegalStateException ("datastore is closed");
		}

		Query<Activity> query = this.fetchQuery (Activity.SELECTOR_ALL, AbstractActivity.getActivityClass (type));

		return query.queryAll ();
	}
}
