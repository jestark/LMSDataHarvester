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

package ca.uoguelph.socs.icc.edm.loader;

import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Load <code>ActivitySource</code> instances from the <code>DataStore</code>.
 * This class extends <code>AbstractLoader</code>, adding the functionality
 * required to handle <code>ActivitySource</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class ActivitySourceLoader extends AbstractLoader<ActivitySource>
{
	/**
	 * Create the <code>ActivitySourceLoader</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public ActivitySourceLoader (final DomainModel model)
	{
		super (ActivitySource.class, model);
	}

	/**
	 * Retrieve an <code>Element</code> instance from the
	 * <code>DataStore</code> based on its <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Element</code> to retrieve, not null
	 *
	 * @return    The requested <code>Element</code>
	 */

	public ActivitySource fetchById (final Long id)
	{
		this.log.trace ("fetchById: id={}", id);

		if (id == null)
		{
			this.log.error ("Attempting to fetch an element with a NULL id");
			throw new NullPointerException ();
		}

		return this.getQuery (ActivitySource.SELECTOR_ID)
			.setValue (ActivitySource.ID, id)
			.query ();
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	public List<ActivitySource> fetchAll ()
	{
		this.log.trace ("fetchAll:");

		return this.getQuery (ActivitySource.SELECTOR_ALL)
			.queryAll ();
	}

	/**
	 * Retrieve the <code>ActivitySource</code> object associated with the
	 * specified name from the <code>DataStore</code>.
	 *
	 * @param  name                  The name of the
	 *                               <code>ActivitySource</code> to retrieve,
	 *                               not null
	 *
	 * @return                       The <code>ActivitySource</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public ActivitySource fetchByName (final String name)
	{
		this.log.trace ("fetchByName: name={}", name);

		if (name == null)
		{
			this.log.error ("The specified ActivitySource name is NULL");
			throw new NullPointerException ();
		}

		return this.getQuery (ActivitySource.SELECTOR_NAME)
			.setValue (ActivitySource.NAME, name)
			.query ();
	}
}
