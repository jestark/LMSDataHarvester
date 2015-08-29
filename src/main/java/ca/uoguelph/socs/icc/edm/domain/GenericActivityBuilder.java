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

package ca.uoguelph.socs.icc.edm.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.element.GenericActivity;

/**
 * Create <code>Activity</code> instances.  This class provides a default
 * implementation of the <code>AbstractActivityBuilder</code>, without adding
 * any additional functionality.  It is only suitable for creating instances of
 * the <code>GenericActivity</code> class.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Activity
 */

public final class GenericActivityBuilder extends AbstractActivityBuilder<Activity>
{
	/** Helper to operate on <code>SubActivity</code> instances*/
	private final DataStoreRWProxy<Activity> activityProxy;

	/**
	 * Get an instance of the <code>GenericActivityBuilder</code> for the specified
	 * <code>DomainModel</code> and <code>ActivityType</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  type                  The <code>ActivityType</code>, not null
	 *
	 * @return                       The <code>GenericActivityBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static GenericActivityBuilder getInstance (final DomainModel model, ActivityType type)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		if (type == null)
		{
			throw new NullPointerException ("type is NULL");
		}

		return new GenericActivityBuilder (model.getDataStore (), type);
	}

	/**
	 * Create the <code>GenericActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected GenericActivityBuilder (final DataStore datastore, final ActivityType type)
	{
		super (datastore, type);

		if (NamedActivity.getActivityClass (this.type) != null)
		{
			this.log.error ("WrongBuilder: There is a NamedActivity implementation for {}", this.type);
			throw new IllegalStateException ("Wrong Builder: The specified ActivityType an associated Activity class.");
		}

		this.activityProxy = DataStoreRWProxy.getInstance (datastore.getProfile ().getCreator (Activity.class, GenericActivity.class), Activity.SELECTOR_ID, datastore);
	}

	/**
	 * Create an instance of the <code>Activity</code>.
	 *
	 * @return                       The new <code>Activity</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public Activity build ()
	{
		this.log.trace ("build:");

		if (this.course == null)
		{
			this.log.error ("course is NULL");
			throw new IllegalStateException ("course is NULL");
		}

		Activity result = this.activityProxy.create ();
		result.setId (this.id);
		result.setType (this.type);
		result.setCourse (this.course);

		this.oldActivity = this.activityProxy.insert (this.oldActivity, result);

		return this.oldActivity;
	}
}
