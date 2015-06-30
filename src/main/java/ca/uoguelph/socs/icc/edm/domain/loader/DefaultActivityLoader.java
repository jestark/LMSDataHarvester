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

import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityLoader;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

import ca.uoguelph.socs.icc.edm.domain.element.AbstractActivity;

/**
 * Default implementation of the <code>ActivityLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActivityLoader extends AbstractLoader<Activity> implements ActivityLoader
{
	/**
	 * Static initializer to register the <code>ActivityLoader</code> with the
	 * <code>LoaderFactory</code>.
	 */

	static
	{
		AbstractLoader.registerLoader (Activity.class, DefaultActivityLoader::new);
	}

	/**
	 * Create the <code>ActivityLoader</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActivityLoader</code> will operate, not
	 *                   null
	 */

	public DefaultActivityLoader (final DataStore datastore)
	{
		super (Activity.class, datastore);
	}

	@Override
	public List<Activity> fetchAllForType (final ActivityType type)
	{
		this.log.trace ("fetchAllForType: type={}", type);

		if (type == null)
		{
			this.log.error ("The specified ActivityType is NULL");
			throw new NullPointerException ();
		}

		Query<Activity> query = this.fetchQuery (Activity.Selectors.ALL, AbstractActivity.getActivityClass (type));

		return query.queryAll ();
	}
}
