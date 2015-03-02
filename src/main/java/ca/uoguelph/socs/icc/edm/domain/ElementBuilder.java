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

/**
 *
 * @author  James E. Stark
 * @version 1.1
 */

public interface ElementBuilder<T extends Element>
{
	/**
	 * Create an instance of the <code>Element</code>.  
	 */

	public abstract T build ();
	
	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	public abstract void clear ();

	/**
	 * Load a <code>Element</code> instance into the <code>ElementBuilder</code>.
	 * This method resets the <code>ElementBuilder</code> and initializes all of
	 * its parameters from the specified <code>Element</code> instance.  The
	 * parameters are validated as they are set.
	 *
	 * @param  element                  The <code>Element</code> to load into the
	 *                                  <code>ElementBuilder</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the 
	 *                                  <code>Element</code> instance to be loaded
	 *                                  are not valid
	 */

	public abstract void load (T element);
}
