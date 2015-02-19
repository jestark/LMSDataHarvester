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

package ca.uoguelph.socs.icc.edm.domain.manager;

import java.util.Map;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeManager;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 *
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityType
 */

public final class DefaultActivityTypeManager extends AbstractManager<ActivityType> implements ActivityTypeManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>DefaultActivityTypeManager</code>.
	 */

	private static final class Factory implements ManagerFactory<ActivityTypeManager>
	{
		/**
		 * Create an instance of the <code>DefaultActivityTypeManager</code>.
		 *
		 * @param  model The <code>DomainModel</code> to be associated with the
		 *               <code>DefaultActivityTypeManager</code>
		 * @return       The <code>DefaultActivityTypeManager</code>
		 */

		@Override
		public ActivityTypeManager create (DomainModel model)
		{
			return new DefaultActivityTypeManager (model);
		}
	}

	/** The logger */
	private final Logger log;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (ActivityType.class, ActivityTypeManager.class, DefaultActivityTypeManager.class, new Factory ());
	}

	/**
	 * Create the <code>ActivityType</code> manager.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>ActivityTypeManager</code> is to be created, not null
	 */

	public DefaultActivityTypeManager (DomainModel model)
	{
		super (ActivityType.class, model);

		this.log = LoggerFactory.getLogger (ActivityTypeManager.class);
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
		this.log.trace ("Fetching ActivityType {}, with ActivitySource {}", name, source);

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

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("source", source);
		params.put ("name", name);

		return (this.fetchQuery ()).query ("name", params);
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
