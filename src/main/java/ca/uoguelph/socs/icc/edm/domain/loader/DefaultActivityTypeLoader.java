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

package ca.uoguelph.socs.icc.edm.domain.loader;

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeLoader;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * Default implementation of the <code>ActivityTypeLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActivityTypeLoader extends AbstractLoader<ActivityType> implements ActivityTypeLoader
{
	/**
	 * Static initializer to register the <code>ActivityTypeLoader</code> with
	 * the <code>LoaderFactory</code>.
	 */

	static
	{
		AbstractLoader.registerLoader (ActivityType.class, DefaultActivityTypeLoader::new);
	}

	/**
	 * Create the <code>ActivityType</code> Loader.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActivityTypeLoader</code> will operate, not
	 *                   null
	 */

	public DefaultActivityTypeLoader (final DataStore datastore)
	{
		super (ActivityType.class, datastore);
	}

	/**
	 * Retrieve the <code>ActivityType</code> object from the
	 * <code>DataStore</code> which has the specified
	 * <code>ActivitySource</code> and name.
	 *
	 * @param  source The <code>ActivitySource</code> containing the
	 *                <code>ActivityType</code>, not null
	 * @param  name   The name of the <code>ActivityType</code>, not null
	 *
	 * @return        The <code>ActivityType</code> instance which is
	 *                associated with the specified source and name
	 */

	@Override
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

		Query<ActivityType> query = this.fetchQuery (ActivityType.Selectors.NAME);
		query.setProperty (ActivityType.Properties.SOURCE, source);
		query.setProperty (ActivityType.Properties.NAME, name);

		return query.query ();
	}
}
