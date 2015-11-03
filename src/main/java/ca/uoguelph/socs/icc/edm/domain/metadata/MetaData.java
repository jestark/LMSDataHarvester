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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;

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

	/** The parent <code>Element</code> class */
	private final @Nullable MetaData<? super T> parent;

	/** The <code>Property</code> instances associated with the interface */
	private final Set<Property<?>> properties;

	/** The <code>Selector</code> instances for the interface */
	private final Set<Selector> selectors;

	/** The <code>Accessor</code> instances for the interface */
	private final Map<Property<?>, Accessor<T, ?>> accessors;

	/** The <code>Reference</code> instances for the interface */
	private final Map<Property<?>, Reference<T, ?>> references;

	/** The <code>Relationship</code> instances for the interface */
	private final Map<Property<?>, Relationship<T, ?>> relationships;

	/**
	 * Get the <code>MetaDataBuilder</code> for the specified
	 * <code>Element</code> interface class.
	 *
	 * @param  <T>    The <code>Element</code> interface type
	 * @param  parent The <code>MetaData</code> for the parent class
	 *
	 * @return        The <code>MetaDataBuilder</code> instance
	 */

	public static <T extends Element> MetaDataBuilder builder (final @Nullable MetaData<? super T> parent)
	{
		return new MetaDataBuilder<T> (parent);
	}

	/**
	 * Create the <code>MetaData</code>.
	 *
	 * @param  parent        The parent </code>Element</code> class, not null
	 * @param  properties    The <code>Set</code> of <code>Property</code>
	 *                       instances, not null
	 * @param  selectors     The <code>Set</code> of <code>Selector</code>
	 *                       instances, not null
	 * @param  relationships The <code>Map</code> of <code>Relationship</code>
	 *                       instances, not null
	 * @param  accessors     The <code>Map</code> of <code>Accessor</code>
	 *                       instances, not null
	 * @param  references    The <code>Map</code> of <code>Reference</code>
	 *                       instances, not null
	 */

	protected MetaData (final MetaData<? super T> parent,
			final Set<Property<?>> properties,
			final Set<Selector> selectors,
			final Map<Property<?>, Accessor<T, ?>> accessors,
			final Map<Property<?>, Reference<T, ?>> references,
			final Map<Property<?>, Relationship<T, ?>> relationships)
	{
		assert properties != null : "properties is NULL";
		assert selectors != null : "selectors is NULL";
		assert accessors != null : "accessors is NULL";
		assert references != null : "references is NULL";
		assert relationships != null : "relationships is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.parent = parent;

		this.selectors = new HashSet<> (selectors);
		this.properties = new HashSet<> (properties);

		this.accessors = new HashMap<> (accessors);
		this.references = new HashMap<> (references);
		this.relationships = new HashMap<> (relationships);
	}

	/**
	 * Determine if the specified <code>Property</code> is associated with the
	 * <code>Element</code> represented by this <code>MetaData</code> instance.
	 *
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return         <code>true</code> if the <code>Property</code> is
	 *                 associated with the <code>Element</code>,
	 *                 <code>false</code> otherwise
	 */

	public boolean hasProperty (final Property <?> property)
	{
		Preconditions.checkNotNull (property, "property");

		return this.properties.contains (property) || (this.parent != null && this.parent.hasProperty (property));
	}

	/**
	 * Get the <code>Accessor</code> associated with the specified
	 * <code>Property</code>.
	 *
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The <code>Accessor</code>
	 */

	public <V> Accessor<? super T, V> getAccessor (final Property<V> property)
	{
		Preconditions.checkNotNull (property, "property");
		Preconditions.checkArgument (this.hasProperty (property), "Property not associated with this element: %s", property.getName ());
		Preconditions.checkArgument (! property.hasFlags (Property.Flags.MULTIVALUED), "Property is Multi-Valued: %s", property.getName ());

		Accessor<? super T, ?> accessor = this.accessors.get (property);

		if ((accessor == null) && (this.parent != null))
		{
			accessor = this.parent.getAccessor (property);
		}

		return (Accessor<? super T, V>) accessor;
	}

	/**
	 * Get the <code>Reference</code> associated with the specified
	 * <code>Property</code>.
	 *
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The <code>Reference</code>
	 */

	public <V> Reference<? super T, V> getReference (final Property<V> property)
	{
		Preconditions.checkNotNull (property, "property");
		Preconditions.checkArgument (this.hasProperty (property), "Property not associated with this element: %s", property.getName ());

		Reference<? super T, ?> reference = this.references.get (property);

		if ((reference == null) && (this.parent != null))
		{
			reference = this.parent.getReferences (property);
		}

		assert reference != null : String.format ("No Reference found for Property: %s", property.getName ());

		return (Reference<? super T, V>) reference;
	}

	/**
	 * Get the <code>Relationship</code> associated with the specified
	 * <code>Property</code>.
	 *
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The <code>Relationship</code>
	 */

	public <V extends Element> Relationship<? super T, V> getRelationship (final Property<V> property)
	{
		Preconditions.checkNotNull (property, "property");
		Preconditions.checkArgument (property.hasFlags (Property.Flags.RELATIONSHIP), "Property is not a relationship: %s", property.getName ());

		Relationship<? super T, ?> relationship = this.relationships.get (property);

		if ((relationship == null) && (this.parent != null))
		{
			relationship = this.parent.getRelationship (property);
		}

		assert relationship != null : String.format ("Relationship does not exist for Property: %s", property.getName ());

		return (Relationship<? super T, V>) relationship;
	}

	/**
	 * Get a <code>Stream</code> containing all of the <code>Property</code>
	 * which are instances associated with the <code>Element</code>.
	 *
	 * @return A <code>Stream</code> of <code>Property</code> instances
	 */

	public Stream<Property<?>> propertyStream ()
	{
		Stream<Property<?>> result = this.properties.stream ();

		return (this.parent != null) ? this.parent.propertyStream ().concat (result)
			: result;
	}

	/**
	 * Get a <code>Stream</code> of <code>Relationship</code> instances which
	 * are associated with the <code>Element</code>.
	 *
	 * @return A <code>Stream</code> of <code>Relationship</code> instances
	 */

	public Stream<Relationship<? super T, ?>> relationshipStream ()
	{
		Stream<Relationship<? super T, ?>> result = this.relationships.entrySet ()
			.stream ()
			.map (x -> x.getValue ());

		return (this.parent != null) ? this.parent.relationshipStream ().concat (result)
			: result;
	}

	/**
	 * Get a <code>Stream</code> of <code>Selector</code> instances which are
	 * associated with the <code>Element</code>.
	 *
	 * @return A <code>Stream</code> of <code>Selector</code> instances
	 */

	public Stream<Selector> selectorStream ()
	{
		Stream<Property<?>> result = this.selectors.stream ();

		return (this.parent != null) ? this.parent.selectorStream ().concat (result)
			: result;
	}
}
