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

import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.element.metadata.Property;

/**
 * Implementation of the <code>Builder</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code>
 * @param   <U> The implementation type of the <code>Element</code>
 */

final class BuilderImpl<T extends Element, U extends T> implements Builder<T>
{
	/** The logger */
	private final Logger log;

	/** The meta-data definition of the <code>Element</code> */
	private final MetaData<T, U> definition;

	/** The value associated with each property */
	private U values;

	/**
	 * Create the <code></code>.
	 *
	 * @param definition The meta-data definition of the <code>Element</code>
	 */

	protected BuilderImpl (final MetaData<T, U> definition)
	{
		assert definition != null : "definition is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.definition = definition;
		this.values = this.definition.createElement ();
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances
	 * corresponding to the <code>Element</code>.
	 *
	 * @return A <code>Set</code> containing all of the <code>Property</code>
	 *         instances for the <code>Element</code>
	 */

	public Set<Property<?>> getProperties ()
	{
		return this.definition.getProperties ();
	}

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

	public T build (final T element)
	{
		this.log.trace ("build: element={}", element);

		U result = null;
		Set<Property<?>> properties = new HashSet<Property<?>> ();

		// If the provided element is of the implementation type check for changes, ignore it otherwise
		if ((this.definition.getElementClass ()).isInstance (element))
		{
			result = (this.definition.getElementClass ()).cast (element);
		}

		for (Property<?> property : this.definition.getProperties ())
		{
			// If any required are missing then bail
			if ((property.isRequired ()) && (this.definition.getValue (property, this.values) == null))
			{
				this.log.error ("Required property is NULL: {}", property.getName ());
				throw new IllegalStateException ("One or more required properties has a NULL value");
			}

			// Check for changes if appropriate, if a immutable property has been changed then we have to make a new instance
			if ((result != null) && (this.definition.getValue (property, this.values) != this.definition.getValue (property, result)))
			{
				if (property.isMutable ())
				{
					properties.add (property);
				}
				else
				{
					result = null;
				}
			}
		}

		// If we aren't making changes then we copy everything into a new instance
		if (result == null)
		{
			result = this.definition.createElement ();
			properties = this.definition.getProperties ();
		}

		for (Property<?> property : properties)
		{
			this.definition.copyValue (property, result, this.values);
		}

		return result;
	}

	/**
	 * Reset all of the stored values to null.
	 */

	public void clear ()
	{
		this.log.trace ("clear:");

		this.values = this.definition.createElement ();
	}

	/**
	 * Get the value for the specified property.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 *                  not null
	 * @param  type     The type Class of the value to return
	 *
	 * @return          The value associated with the specified property
	 */

	public <V> V getPropertyValue (final Property<V> property)
	{
		this.log.trace ("getProperty: property={}", property);

		assert property != null : "property is NULL";

		return this.definition.getValue (property, this.values);
	}

	/**
	 * Set the specified property to the specified value.
	 *
	 * @param  <V>      The type of the value associated with the property
	 * @param  property The <code>Property</code>, not null
	 * @param  value    The value to set for the property
	 */

	public <V> void setProperty (final Property<V> property, final V value)
	{
		this.log.trace ("setProperty: property={}, value={}", property, value);

		assert property != null : "property is NULL";

		this.definition.setValue (property, this.values, value);
	}
}
