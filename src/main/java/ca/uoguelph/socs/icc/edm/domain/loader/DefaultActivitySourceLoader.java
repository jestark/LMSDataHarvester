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

import java.util.Map;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceLoader;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>ActivitySourceLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActivitySourceLoader extends AbstractLoader<ActivitySource> implements ActivitySourceLoader
{
	/**
	 * Static initializer to register the <code>ActivitySourceLoader</code>
	 * with the <code>LoaderFactory</code>.
	 */

	static
	{
		AbstractLoader.registerLoader (ActivitySource.class, DefaultActivitySourceLoader::new);
	}

	/**
	 * Create the <code>ActivitySourceLoader</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActivitySourceLoader</code> will operate,
	 *                   not null
	 */

	public DefaultActivitySourceLoader (final DataStore datastore)
	{
		super (ActivitySource.class, datastore);
	}

	/**
	 * Retrieve the <code>ActivitySource</code> object associated with the
	 * specified name from the <code>DataStore</code>.
	 *
	 * @param  name The name of the <code>ActivitySource</code> to retrieve,
	 *              not null
	 *
	 * @return      The <code>ActivitySource</code> instance associated with
	 *              the specified name
	 */

	@Override
	public ActivitySource fetchByName (final String name)
	{
		this.log.trace ("fetchByName: name={}", name);

		if (name == null)
		{
			this.log.error ("The specified ActivitySource name is NULL");
			throw new NullPointerException ();
		}

		return ((this.fetchQuery ("name")).setParameter ("name", name)).query ();
	}
}
