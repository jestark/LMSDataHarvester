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

package ca.uoguelph.socs.icc.edm.domain.metadata;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

/**
 * A representation of an Accessor/Mutator.  This interface defines an accessor
 * and mutator for the value in the <code>Element</code> which is represented
 * by an associated <code>Property</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the <code>Element</code>
 * @param   <V> The type of the value
 * @see     MetaData
 * @see     Property
 */

public interface Accessor<T extends Element, V> extends Reference<T, V>
{
	/**
	 * Get the value from the specified <code>Element</code> instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          The value from the <code>Element</code>
	 */

	@CheckReturnValue
	public abstract V getValue (T element);

	/**
	 * Enter the specified value into the specified <code>Element</code>
	 * instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 * @param  value    The value to be set, may be null
	 */

	public abstract void setValue (T element, @Nullable V value);
}
