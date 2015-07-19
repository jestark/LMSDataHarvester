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
	/**
	 * Get an instance of the <code>GenericActivityBuilder</code> for the specified
	 * <code>DataStore</code> and <code>ActivityType</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  type                  The <code>ActivityType</code>, not null
	 *
	 * @return                       The <code>GenericActivityBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 */

	public static GenericActivityBuilder getInstance (final DataStore datastore, ActivityType type)
	{
		assert datastore != null : "datastore is NULL";
		assert type != null : "type is NULL";
		assert datastore.contains (type) : "type is NULL";

		return AbstractActivityBuilder.getInstance (datastore, Activity.class, type, GenericActivityBuilder::new);
	}

	/**
	 * Get an instance of the <code>GenericActivityBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>Activity</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  activity              The <code>Activity</code>, not null
	 *
	 * @return                       The <code>GenericActivityBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 */

	public static GenericActivityBuilder getInstance (final DataStore datastore, Activity activity)
	{
		assert datastore != null : "datastore is NULL";
		assert activity != null : "activity is NULL";

		GenericActivityBuilder builder = GenericActivityBuilder.getInstance (datastore, activity.getType ());
		builder.load (activity);

		return builder;
	}

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

		if (! model.contains (type))
		{
			throw new IllegalArgumentException ("type is not in the datastore");
		}

		return GenericActivityBuilder.getInstance (AbstractBuilder.getDataStore (model), type);
	}

	/**
	 * Get an instance of the <code>GenericActivityBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>Activity</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  activity              The <code>Activity</code>, not null
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

	public static GenericActivityBuilder getInstance (final DomainModel model, Activity activity)
	{
		if (activity == null)
		{
			throw new NullPointerException ("activity is NULL");
		}

		GenericActivityBuilder builder = GenericActivityBuilder.getInstance (model, activity.getType ());
		builder.load (activity);

		return builder;
	}

	/**
	 * Create the <code>GenericActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  builder   The <code>Builder</code>, not null
	 */

	protected GenericActivityBuilder (final DataStore datastore, final Builder<Activity> builder)
	{
		super (datastore, builder);
	}
}
