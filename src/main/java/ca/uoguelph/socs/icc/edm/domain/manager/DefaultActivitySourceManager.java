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
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceManager;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

import ca.uoguelph.socs.icc.edm.domain.factory.ActivitySourceFactory;

/**
 * 
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivitySource
 */

public final class DefaultActivitySourceManager extends AbstractManager<ActivitySource> implements ActivitySourceManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>DefaultActivitySourceManager</code>.
	 */

	private static final class DefaultActivitySourceManagerFactory implements ManagerFactory<ActivitySourceManager>
	{
		/**
		 * Create an instance of the <code>DefaultActivitySourceManager</code>.
		 *
		 * @param  model The <code>DomainModel</code> to be associated with the
		 *               <code>DefaultActivitySourceManager</code>
		 * @return       The <code>DefaultActivitySourceManager</code>
		 */

		@Override
		public ActivitySourceManager create (DomainModel model)
		{
			return new DefaultActivitySourceManager (model);
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
		(ActivitySourceFactory.getInstance ()).registerManager (DefaultActivitySourceManager.class, new DefaultActivitySourceManagerFactory ());
	}

	/**
	 * Create the <code>ActivitySourceManager</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>ActivitySourceManager</code> is to be created, not null
	 */

	public DefaultActivitySourceManager (DomainModel model)
	{
		super (ActivitySource.class, model);

		this.log = LoggerFactory.getLogger (ActivitySourceManager.class);
	}

	/**
	 * Retrieve the <code>ActivitySource</code> object associated with the
	 * specified name from the underlying data-store.
	 *
	 * @param  name The name of the <code>ActivitySource</code> to retrieve, not
	 *              null
	 * @return      The <code>ActivitySource</code> object associated with the
	 *              specified name
	 */

	public ActivitySource fetchByName (String name)
	{
		this.log.trace ("Fetching ActvitySource {}", name);

		if (name == null)
		{
			this.log.error ("The specified ActivitySource name is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("name", name);

		return (this.fetchQuery ()).query ("name", params);
	}
}
