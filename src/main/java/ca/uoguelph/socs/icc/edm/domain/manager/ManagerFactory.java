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

import ca.uoguelph.socs.icc.edm.domain.ElementManager;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create an <code>ElementManager</code> to operate upon the specified
 * <code>DataStore</code>.  Implementations of this interface are intended to
 * be used with the <code>registerFactory</code> and <code>getInstance</code>
 * methods in the <code>AbstractManager</code> to create a specific
 * <code>ElementManager</code>.
 *
 * @author  James E. Stark
 * @version 1.2
 * @param   <T> The type of <code>ElementManager</code> returned by the factory
 * @see     AbstractManager
 */

public interface ManagerFactory<T>
{
	/**
	 * Create the <code>ElementManager</code>.
	 *
	 * @param  datastore The <code>DataStore</code> upon which the
	 *                   <code>ElementManager</code> will operate, not null
	 * @return           The <code>ElementManager</code>
	 */

	public abstract T create (DataStore datastore);
}
