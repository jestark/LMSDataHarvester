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

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.datastore.DataStoreProfile;
import ca.uoguelph.socs.icc.edm.datastore.DataStoreQuery;
import ca.uoguelph.socs.icc.edm.datastore.DataStoreTransaction;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DomainModel
{
	/** The data store which contains all of the data */
	private final DataStore datastore;

	/** The profile */
	private final DataStoreProfile profile;

	/** The log */
	private final Log log;

	/**
	 * Create the <code>DomainModel</code>
	 *
	 * @param  datastore The <code>DataStore</code> which contains all of the data
	 *                   represented by this <code>DomainModel</code>, not null
	 * @param  profile   The configuration profile, not null
	 */

	public DomainModel (DataStore datastore, DataStoreProfile profile)
	{
		this.datastore = datastore;
		this.profile = profile;

		this.log = LogFactory.getLog (DomainModel.class);
	}

	protected <T extends Element> DataStoreQuery<T> createQuery (QueryFactory<T> factory)
	{
		return factory.create (this.datastore);
	}

	/**
	 * Determine if the <code>DomainModel</code> is mutable.
	 *
	 * @return <code>true</code> if the <code>DomainModel</code> is mutable,
	 *         <code>false</code> otherwise
	 */

	public Boolean isMutable ()
	{
		return this.profile.isMutable ();
	}

	/**
	 * Determine if the underlying <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the underlying <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 * @see    ca.uoguelph.socs.icc.edm.datastore.DataStore#isOpen
	 */

	public Boolean isOpen ()
	{
		return this.datastore.isOpen ();
	}

	/**
	 * Close the connections to any underlying data sources.
	 */

	public void close ()
	{
		this.datastore.close ();
	}

	public DataStoreTransaction getTransaction ()
	{
		return this.datastore.getTransaction ();
	}

	protected DataStoreProfile getProfile ()
	{
		return this.profile;
	}

	/**
	 * Get the requested <code>ElementManager</code>.
	 *
	 * @param <T>                 The type of the manager
	 * @param type                The manager type class, not null
	 * @return                    An instance of the requested manager.
	 * @throws ClassCastException if the stored instance of the manager doesn't
	 *                            match the requested type.
	 *                            if the specified manager type does not match
	 *                            that of a previously stored manager instance
	 *                            (should never happen).
	 */

	public <T extends ElementManager<? extends Element>> T getManager (Class<T> type)
	{
		if (type == null)
		{
			this.log.error ("The specified for the manager is NULL");
			throw new NullPointerException ("Manager type is NULL");
		}
		
		/* Uhmmm.... */
		
		return null;

/*		switch (type)
		{
			case ACTION:
				result = new ActionManager (this);
				break;

			case ACTIVITY:
				result = new ActivityManager (this);
				break;

			case ACTIVITYSOURCE:
				result = new ActivitySourceManager (this);
				break;

			case ACTIVITYTYPE:
				result = new ActivityTypeManager (this);
				break;

			case COURSE:
				result = new CourseManager (this);
				break;

			case ENROLMENT:
				result = new EnrolmentManager (this);
				break;

			case LOGENTRY:
				result = new LogEntryManager (this);
				break;

			case ROLE:
				result = new RoleManager (this);
				break;

			case USER:
				result = new UserManager (this);
				break;

			default:
				throw new IllegalArgumentException ();
		}*/
	}
}
