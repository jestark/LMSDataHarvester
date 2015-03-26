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

package ca.uoguelph.socs.icc.edm.domain.builder;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

/**
 * Create an <code>ElementBuilder</code> which will create new
 * <code>Element</code> instances against the <code>DataStore</code>.
 * Implementations of this interface are intended to be used with the
 * <code>registerFactory</code> and <code>getInstance</code> methods in the
 * <code>AbstractBuilder</code> to create a specific
 * <code>ElementBuilder</code>.
 *
 * @author  James E. Stark
 * @version 1.1
 * @param   <T> The <code>Element</code> type of the factory
 * @param   <U> The type of <code>ElementBuilder</code> returned by the factory
 * @see     AbstractBuilder
 */

public interface BuilderFactory<T extends Element, U extends ElementBuilder<T>>
{
	/**
	 * Create the <code>ElementBuilder</code>.  The supplied
	 * <code>ManagerProxy</code> will be used by the builder to access the
	 * <code>ElementManager</code> to perform operations on the
	 * <code>DataStore</code>.
	 *
	 * @param  manager The <code>ManagerProxy</code> used to the
	 *                 <code>ElementManager</code> instance, not null
	 *
	 * @return         The <code>ElementBuilder</code>
	 */

	public abstract U create (ManagerProxy<T> manager);
}
