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
	 * @throws IllegalStateException if there is no <code>SubActivity</code>
	 *                               class registered for the specified parent
	 *                               <code>Activity</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static SubActivityBuilder getInstance (final DomainModel model, ParentActivity parent)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		if (parent == null)
		{
			throw new NullPointerException ("parent is NULL");
		}

		return new SubActivityBuilder (model.getDataStore (), parent);
	}

	/**
	 * Create the <code>SubActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  parent    The <code>ParentActivity</code>, not null
	 */

	protected SubActivityBuilder (final DataStore datastore, final ParentActivity parent)
	{
		super (datastore, parent);
	}

	/**
	 * Create an instance of the <code>SubActivity</code>.
	 *
	 * @return                       The new <code>SubActivity</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public SubActivity build ()
	{
		this.log.trace ("build:");

		if (this.name == null)
		{
			this.log.error ("name is NULL");
			throw new IllegalStateException ("name is NULL");
		}

		SubActivity result = this.subActivityProxy.create ();
		result.setId (this.id);
		result.setParent (this.parent);
		result.setName (this.name);

		this.oldSubActivity = this.subActivityProxy.insert (this.oldSubActivity, result);

		return this.oldSubActivity;
	}
}
