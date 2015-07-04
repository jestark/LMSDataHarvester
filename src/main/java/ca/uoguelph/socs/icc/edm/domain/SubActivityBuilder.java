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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create <code>SubActivity</code> instances.  This class provides a default
 * implementation of the <code>AbstractSubActivityBuilder</code> without adding
 * any additional functionality.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     SubActivity
 */

public final class SubActivityBuilder extends AbstractSubActivityBuilder<SubActivity>
{
	/**
	 * Get an instance of the <code>SubActivityBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>SubActivityBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>SubActivity</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static SubActivityBuilder getInstance (final DataStore datastore, Activity parent)
	{
		assert datastore != null : "datastore is NULL";
		assert parent != null : "parent is NULL";
		assert datastore.contains (parent) : "parent is not in the datastore";

		return AbstractSubActivityBuilder.getInstance (datastore, parent, SubActivityBuilder::new);
	}

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>SubActivity</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  subactivity           The <code>SubActivity</code>, not null
	 *
	 * @return                       The <code>SubActivityBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>SubActivity</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static SubActivityBuilder getInstance (final DataStore datastore, SubActivity subactivity)
	{
		assert datastore != null : "datastore is NULL";
		assert subactivity != null : "subactivity is NULL";

		SubActivityBuilder builder = SubActivityBuilder.getInstance (datastore, subactivity.getParent ());
		builder.load (subactivity);

		return builder;
	}

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>SubActivityBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>SubActivity</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */


	public static SubActivityBuilder getInstance (final DomainModel model, Activity parent)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		if (parent == null)
		{
			throw new NullPointerException ("parent is NULL");
		}

		if (! model.contains (parent))
		{
			throw new IllegalArgumentException ("parent is not in the datastore");
		}

		return SubActivityBuilder.getInstance (AbstractBuilder.getDataStore (model), parent);
	}

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>SubActivity</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  subactivity           The <code>SubActivity</code>, not null
	 *
	 * @return                       The <code>SubActivityBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>SubActivity</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static SubActivityBuilder getInstance (final DomainModel model, SubActivity subactivity)
	{
		if (subactivity == null)
		{
			throw new NullPointerException ("subactivity is NULL");
		}

		SubActivityBuilder builder = SubActivityBuilder.getInstance (model, subactivity.getParent ());
		builder.load (subactivity);

		return builder;
	}

	/**
	 * Create the <code>SubActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  builder   The <code>Builder</code>, not null
	 */

	protected SubActivityBuilder (final DataStore datastore, final Builder<SubActivity> builder)
	{
		super (datastore, builder);
	}
}
