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

package ca.uoguelph.socs.icc.edm.domain.element.metadata;

import java.util.Set;

import ca.uoguelph.socs.icc.edm.domain.Element;

public interface Builder<T extends Element>
{
	/**
	 * Get the <code>Property</code> associated with the <code>Element</code>
	 * with the specified name.
	 *
	 * @param  name The name of the <code>Property</code>
	 *
	 * @return      The specified <code>Property</code> instance or
	 *              <code>null</code> if a <code>Property</code> with the
	 *              specified name does not exist
	 */

	public abstract Property<?> getProperty (String name);

	/**
	 * Get the <code>Property</code> associated with the <code>Element</code>
	 * with the specified name and value type.
	 *
	 * @param  name The name of the <code>Property</code>
	 * @param  type The type of the value associated with the
	 *              <code>Property</code>
	 *
	 * @return      The specified <code>Property</code> instance or
	 *              <code>null</code> if a <code>Property</code> with the
	 *              specified name and type does not exist
	 */

	public abstract <V> Property<V> getProperty (String name, Class<V> type);

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances
	 * corresponding to the <code>Element</code>.
	 *
	 * @return A <code>Set</code> containing all of the <code>Property</code>
	 *         instances for the <code>Element</code>
	 */

	public abstract Set<Property<?>> getProperties ();

	/**
	 * Modify an existing <code>Element</code> instance, or create a new
	 * <code>Element</code> instance.  This method will compare the references
	 * for all of the values in the supplied <code>Element</code> instance to
	 * the values stored internally.  If all of the changed values are mutable
	 * then the supplied <code>Element</code> instance will be updated with the
	 * changes, and that instance will be returned.  A new <code>Element</code>
	 * instance will be returned if any immutable values have been changed.
	 * This method will return the supplied <code>Element</code> instance with
	 * no changes if if detects that none of the values had been changed.
	 *
	 * @param  element               The <code>Element</code> to update
	 *
	 * @return                       The supplied <code>Element</code> instance
	 *                               or a new <code>Element</code> instance
	 *                               depending on which values have changed
	 * @throws IllegalStateException if a required value is missing
	 */

	public abstract T build (final T element);

	/**
	 * Reset all of the stored values to null.
	 */

	public abstract void clear ();

	/**
	 * Get the value for the specified property.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The value associated with the specified property
	 */

	public abstract <V> V getPropertyValue (final Property<V> property);

	/**
	 * Set the specified property to the specified value.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 * @param  value    The value to set for the property
	 */

	public abstract <V> void setProperty (final Property<V> property, final V value);
}
