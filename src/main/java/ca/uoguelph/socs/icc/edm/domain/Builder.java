/* Copyright (C) 2015 James E. Stark
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
 * Create and modify instances of the <code>Element</code> implementations.
 * This class is the base class for all of the builders which produce
 * <code>Element</code> instances, containing all of the common functionality.
 * <p>
 * All of the builders are written to their corresponding <code>Element</code>
 * interface.  The builder itself does not know about the details of the
 * <code>Element</code> implementation.  Internally the builders use a
 * <code>MetaData</code> based builder which handles the implementation
 * details, which allows one builder class to handle all of the
 * implementations for a given <code>Element</code> interface.  When the
 * builder is instantiated, the <code>Element</code> implementation class is
 * determines though an examination of the profile data from the
 * <code>DataStore</code>, and the internal builder will be created from the
 * <code>MetaData</code> for the selected <code>Element</code> implementation
 * class.
 * <p>
 * Each builder will validate all of the inputs as they supplied to ensure that
 * required fields are not set to <code>null</code> and that the values are
 * within valid ranges.  When the <code>build</code> method is called, the
 * builder will ensure that all of the required fields are present, then it
 * will create an instance of the <code>Element</code> and insert it into the
 * <code>DataStore</code> before returning the <code>Element</code> instance to
 * the caller.
 * <p>
 * Unless it is otherwise noted all of the fields of an <code>Element</code>
 * are immutable after the <code>Element</code> instance is created.  Existing
 * <code>Element</code> instances can be loaded into the builder, to be used as
 * a template for creating new <code>Element</code> instances.  When the
 * builder as asked to build the <code>Element</code> instance, it will compare
 * data which it set in the builder to the data in the existing
 * <code>Element</code> instance.  If the builder detects that the changed
 * fields are a subset of the mutable fields, it will modify the existing
 * <code>Element</code> instance, rather than creating a new instance.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code> to be created by the builder
 */

public interface Builder<T extends Element>
{
	/**
	 * Create an instance of the <code>Element</code>.
	 *
	 * @return                       The new <code>Element</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	public T build ();
}
