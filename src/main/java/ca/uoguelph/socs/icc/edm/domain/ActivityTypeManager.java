/* Copyright (C) 2014 James E. Stark
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.datastore.DataStoreQuery;

/**
 *
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityType
 */

public final class ActivityTypeManager extends DomainModelManager<ActivityType>
{
	/** The logger */
	private final Log log;

	/**
	 * Get an instance of the <code>ActivityTypeManager</code> for the specified
	 * domain model.
	 *
	 * @param  model The instance of the <code>DomainModel</code> for which the
	 *               <code>ActivityTypeManager</code>is to be retrieved, not null
	 * @return       The <code>ActivityTypeManager</code> instance for the 
	 *               specified domain model
	 * @see    DomainModel#getManager
	 */

	public static ActivityTypeManager getInstance (DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ();
		}

		return model.getManager (ActivityTypeManager.class);
	}

	/**
	 * Create the <code>ActivityType</code> manager.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>ActivityTypeManager</code> is to be created, not null
	 * @param  query The <code>DataStoreQuery</code> to be used to access the
	 *               data-store, not null
	 */

	protected ActivityTypeManager (DomainModel model, DataStoreQuery<ActivityType> query)
	{
		super (model, query);

		this.log = LogFactory.getLog (UserManager.class);
	}

	/**
	 * Get an instance of the builder.
	 *
	 * @return An instance of the <code>ActivityTypeBuilder</code>
	 */

	public ActivityTypeBuilder getBuilder ()
	{
		return (ActivityTypeBuilder) this.builder;
	}

	/**
	 * Retrieve the <code>ActivityType</code> object from the underlying 
	 * data-store which has the specified <code>ActivitySource</code> and name.
	 *
	 * @param  source The <code>ActivitySource</code> containing the 
	 *                <code>ActivityType</code>, not null
	 * @param  name   The name of the <code>ActivityType</code>, not null
	 * @return        The <code>ActivityType</code> object which is associated
	 *                with the specified source and name
	 */

	public ActivityType fetchByName (ActivitySource source, String name)
	{
		return null;
	}

	/**
	 * Create an association between a <code>ActivityType</code> and a 
	 * <code>Action</code>.  Both the <code>ActivityType</code> and the action
	 * must already exist in the domain model associated with the
	 * <code>ActivityTypeManager</code> that to create the association between
	 * them.
	 *
	 * @param  type   The <code>ActivityType</code> to modify, not null
	 * @param  action The <code>Action</code> to be associated with the
	 *                <code>ActivityType</code>, not null
	 */

	public void addAction (ActivityType type, Action action)
	{
	}

	/**
	 * Break an association between a <code>ActivityType</code> and a
	 * <code>Action</code>.  To break an association between the specified
	 * <code>ActivityType</code> and <code>Action</code>, both the
	 * <code>ActivityType</code> and <code>Action</code> must be exist in the
	 * domain model associated with the <code>ActivityTypeManager</code> that is
	 * to break the association.  Furthermore, there must be an existing 
	 * association between the <code>ActivityType</code> and the 
	 * <code>Action</code>, and there must not exist any log entries containing
	 * the specified <code>Action</code> and an <code>Activity</code> with the
	 * specified <code>ActivityType</code>.
	 *
	 * @param  type   The <code>ActivityType</code> to modify, not null
	 * @param  action The <code>Action</code> to remove from the
	 *                <code>ActivityType</code>, not null
	 */

	public void removeAction (ActivityType type, Action action)
	{
	}
}
