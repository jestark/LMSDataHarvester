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

/**
 * Create and initialize <code>Element</code> instances.  This interface and
 * its sub-interfaces are used by the <code>ElementBuilder</code>
 * implementations to create and initialize instances of the implementation
 * classes for the corresponding domain model interface.
 * <p>
 * A <code>create</code> method with the necessary arguments, such that its
 * implementation can completely initialize the <code>Element</code>
 * implementation must be included in each of the interfaces which extend
 * <code>ElementFactory</code>.  The sub-interface must also provide the
 * methods required to add and remove any dependant <code>Element</code>
 * instances.
 * <p>
 * Each class implementing the <code>Element</code> interface must register an
 * instance of the  appropriate <code>ElementFactory</code> interface with the
 * <code>BuilderFactory</code> so that the builders can create and initialize
 * the <code>Element</code> implementations.  Since the 
 * <code>ElementFactory</code> implementation is specific to the
 * <code>Element</code> implementation, the <code>ElementFactory</code> is
 * usually implemented as an inner class in the <code>Element</code>
 * implementation.
 * <p>
 * Since the <code>ElementFactory</code> instances are only to be used
 * internally by the <code>ElementBuilder</code> implementations, they are not
 * expected to enforce any of the stated constraints on their methods.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code> which is being created
 * @see     ca.uoguelph.socs.icc.edm.domain.ElementBuilder
 */

public interface ElementFactory<T extends Element>
{
	/**
	 * Write the specified <code>DataStore</code> ID number into the
	 * <code>Element</code>.
	 *
	 * @param  element The <code>Element</code> to which the ID number is
	 *                 assigned, not null
	 * @param  id      The ID number assigned to the <code>Element</code>, not
	 *                 null
	 */

	public abstract void setId (T element, Long id);
}
