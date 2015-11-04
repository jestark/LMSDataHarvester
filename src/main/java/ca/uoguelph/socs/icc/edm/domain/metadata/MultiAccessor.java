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

import java.util.Collection;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * A representation of an Accessor/Mutator.  This interface defines an accessor
 * and mutator for a <code>Collection</code> of values in the
 * <code>Element</code> which is represented by the associated
 * <code>Property</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the <code>Element</code>
 * @param   <V> The type of the value
 * @see     MetaData
 * @see     Property
 */

public interface MultiAccessor<T extends Element, V extends Element> extends Reference<T, V>
{
	/**
	 * Get the value from the specified <code>Element</code> instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          The value from the <code>Element</code>
	 */

	public abstract Collection<V> getValue (T element);

	/**
	 * Add the specified value to the specified <code>Element</code> instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 * @param  value    The value to be added, not null
	 */

	public abstract boolean addValue (T element, V value);

	/**
	 * Remove the specified value from the specified <code>Element</code>
	 * instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 * @param  value    The value to be removed, not null
	 */

	public abstract boolean removeValue (T element, V value);
}
