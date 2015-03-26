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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;

/**
 * Retrieve and manage instances of <code>Element</code> implementations in the
 * <code>DataStore</code>.  Implementations of this interface are responsible
 * for fetching, adding and removing <code>Element</code> instances from the
 * <code>DataStore</code>.  Instances of the appropriate
 * <code>ElementBuilder</code> interface are used along with the
 * <code>ElementManager</code> to create new <code>Element</code> instances and
 * add them to the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code> to be processed
 */

public interface ElementManager<T extends Element>
{
	/**
	 * Test an instance of an <code>Element</code> to determine if a reference
	 * to that <code>Element</code> instance exists in the
	 * <code>DataStore</code>.  The exact behaviour of this method is
	 * determined by the implementation of the <code>DataStore</code>.
	 * <p>
	 * If the <code>Element</code> instance was created by the current instance
	 * of the <code>DataStore</code> then this method, should return
	 * <code>True</code>.  Otherwise, this method should return
	 * <code>False</code>, even if an identical <code>Element</code> instance
	 * exists in the <code>DataStore</code>.  Use the <code>fetch</code> method
	 * to retrieve an instance from the <code>DataStore</code>, if one exists.
	 *
	 * @param  element The <code>Element</code> instance to test, not null
	 *
	 * @return          <code>True</code> if the <code>DataStore</code>
	 *                  instance contains a reference to the
	 *                  <code>Element</code>, <code>False</code> otherwise
	 */

	public abstract boolean contains (T element);

	/**
	 * Retrieve an <code>Element</code> from the <code>DataStore</code> which
	 * identifies the same as the specified <code>Element</code>.  If the
	 * specified <code>Element</code> came from the current instance of the
	 * <code>DataStore</code> then the same <code>Element</code> will be
	 * returned.  Otherwise, if there exists an <code>Element</code> in the
	 * <code>DataStore</code> which identifies to be the same as the specified
	 * <code>Element</code>, that <code>Element</code> will be returned.  This
	 * method will return <code>null</code> if there is no matching
	 * <code>Element</code> in the <code>DataStore</code>.
	 * <p>
	 * It should be noted that an <code>Element</code> which identifies the
	 * same as another <code>Element</code> may not be equal to that other
	 * <code>Element</code>.  Some <code>Element</code> instances (particularly
	 * <code>Activity</code> and <code>Enrolment</code>) have fields which are
	 * not to identify the <code>Element</code>.  Use the <code>equals</code>
	 * method to determine if the <code>Element</code> instances are equal.
	 *
	 * @param  element The <code>Element</code> to retrieve, not null
	 *
	 * @return         A reference to the <code>Element</code> in the
	 *                 <code>DataStore</code>, may be null
	 */

	public abstract T fetch (T element);

	/**
	 * Retrieve a <code>Element</code> instance from the <code>DataStore</code>
	 * based upon is <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Element</code> instance to retrieve, not null
	 *
	 * @return    The requested <code>Element</code> instance
	 */

	public abstract T fetchById (Long id);

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances represented by the <code>ElementManager</code> instance from
	 * the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	public abstract List<T> fetchAll ();

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> to insert, not null
	 *
	 * @return         A reference to the <code>Element</code> instance in the
	 *                 <code>DataStore</code>
	 */

	public abstract T insert (T element);

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> to remove, not null
	 */

	public abstract void remove (T element);
}
