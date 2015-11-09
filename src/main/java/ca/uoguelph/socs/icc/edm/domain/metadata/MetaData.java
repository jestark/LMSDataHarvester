/* Copyright (C) 2015 James E. Stark
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General public abstract License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General public abstract License for more details.
 *
 * You should have received a copy of the GNU General public abstract License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.uoguelph.socs.icc.edm.domain.metadata;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Meta-data definition for an <code>Element</code> class.   This class
 * contains <code>MetaData</code> definition for an <code>Element</code>
 * interface.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> interface type
 * @see     Property
 * @see     Selector
 */

public final class MetaData<T extends Element>
{
	/** The Logger */
	private final Logger log;

	/** The <code>Element</code> interface class */
	private final Class<T> element;

	/** The parent <code>Element</code> class */
	private final @Nullable MetaData<? super T> parent;

	/** The <code>Property</code> instances associated with the interface */
	private final Set<Property<? extends Element, ?>> properties;

	/** The <code>Selector</code> instances for the interface */
	private final Set<Selector<? extends Element>> selectors;

	/** The <code>Relationship</code> instances for the interface */
	private final Map<Property<T, ?>, Relationship<T, ?>> relationships;

	/**
	 * Get the <code>MetaDataBuilder</code> for the specified
	 * <code>Element</code> interface class.
	 *
	 * @param  <T>     The <code>Element</code> interface type
	 * @param  element The <code>Element</code> interface class, not null
	 *
	 * @return         The <code>MetaDataBuilder</code> instance
	 */

	public static <T extends Element> MetaDataBuilder<T> builder (final Class<T> element)
	{
		return new MetaDataBuilder<T> (element, null);
	}

	/**
	 * Get the <code>MetaDataBuilder</code> for the specified
	 * <code>Element</code> interface class.
	 *
	 * @param  <T>     The <code>Element</code> interface type
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  parent  The <code>MetaData</code> for the parent class
	 *
	 * @return         The <code>MetaDataBuilder</code> instance
	 */

	public static <T extends Element> MetaDataBuilder<T> builder (final Class<T> element, final MetaData<? super T> parent)
	{
		return new MetaDataBuilder<T> (element, parent);
	}

	/**
	 * Create the <code>MetaData</code>.
	 *
	 * @param  element        The <code>Element</code> interface class, not null
	 * @param  parent         The parent </code>Element</code> class, not null
	 * @param  properties     The <code>Set</code> of <code>Property</code>
	 *                        instances, not null
	 * @param  selectors      The <code>Set</code> of <code>Selector</code>
	 *                        instances, not null
	 */

	protected MetaData (final Class<T> element,
			final MetaData<? super T> parent,
			final Set<Property<? extends Element, ?>> properties,
			final Set<Selector<? extends Element>> selectors)
	{
		assert element != null : "element is NULL";
		assert properties != null : "properties is NULL";
		assert selectors != null : "selectors is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.element = element;
		this.parent = parent;

		this.selectors = new HashSet<> (selectors);
		this.properties = new HashSet<> (properties);

		this.relationships = new HashMap<> ();
	}

	/**
	 * Compare two <code>MetaData</code> instances to determine if
	 * they are equal.
	 *
	 * @param  obj The <code>MetaData</code> instance to compare to
	 *             the one represented by the called instance
	 *
	 * @return     <code>true</code> if the two <code>MetaData</code>
	 *             instances are equal, <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof MetaData)
				&& Objects.equals (this.element, ((MetaData) obj).element);
	}

	/**
	 * Compute a hashCode for the <code>MetaData</code> instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.element);
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>MetaData</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>MetaData</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("element", this.element)
			.toString ();
	}

	/**
	 * Get the <code>Element</code> interface class represented by this
	 * <code>MetaData</code> instance.
	 *
	 * @return The <code>Element</code> interface class
	 */

	public Class<T> getElementClass ()
	{
		return this.element;
	}

	/**
	 * Get the <code>Relationship</code> associated with the specified
	 * <code>Property</code>.
	 *
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The <code>Relationship</code>
	 */

	@SuppressWarnings ("unchecked")
	public <V extends Element> Relationship<T, V> getRelationship (final Property<T, V> property)
	{
		Preconditions.checkNotNull (property, "property");
		Preconditions.checkArgument (property.hasFlags (Property.Flags.RELATIONSHIP), "Property is not a relationship: %s", property.getName ());

		return (Relationship<T, V>) this.relationships.get (property);
	}

	/**
	 * Get a <code>Stream</code> containing all of the <code>Property</code>
	 * which are instances associated with the <code>Element</code>.
	 *
	 * @return A <code>Stream</code> of <code>Property</code> instances
	 */

	public Stream<Property<? extends Element, ?>> properties ()
	{
		Stream<Property<? extends Element, ?>> result = this.properties.stream ();

		return (this.parent != null) ? Stream.concat (this.parent.properties (), result)
			: result;
	}

	/**
	 * Get a <code>Stream</code> of <code>Relationship</code> instances which
	 * are associated with the <code>Element</code>.
	 *
	 * @return A <code>Stream</code> of <code>Relationship</code> instances
	 */

	public Stream<Relationship<? super T, ?>> relationships ()
	{
		Stream<Relationship<? super T, ?>> result = this.relationships.entrySet ()
			.stream ()
			.map (x -> x.getValue ());

		return (this.parent != null) ? Stream.concat (this.parent.relationships (), result)
			: result;
	}

	/**
	 * Get a <code>Stream</code> of <code>Selector</code> instances which are
	 * associated with the <code>Element</code>.
	 *
	 * @return A <code>Stream</code> of <code>Selector</code> instances
	 */

	public Stream<Selector<? extends Element>> selectors ()
	{
		Stream<Selector<? extends Element>> result = this.selectors.stream ();

		return (this.parent != null) ? Stream.concat (this.parent.selectors (), result)
			: result;
	}
}
