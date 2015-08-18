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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Abstract representation of a relationship between to <code>Element</code>
 * classes.  To ensure the integrity of the <code>DataStore</code>, the
 * relationships between <code>Element</code> instances are subject to
 * constraints.  The constrains are that both of the <code>Elements</code>
 * involved in a relationship must exist in the same <code>DataStore</code> and
 * a relationship between any two <code>Element</code> instances must be valid
 * for all of the <code>Element</code> instances in the <code>DataStore</code>.
 * Specifically, the <code>DataStore</code> must prevent an existing
 * relationship between two <code>Element</code> instances from being
 * overwritten by the addition of a third <code>Element</code> instance.
 * <p>
 * To ensure its integrity, the <code>DataStore</code> must process the
 * relationships for each <code>Element</code> instance as it is added or
 * removed.  When an <code>Element</code> instance is added to the
 * <code>DataStore</code>, its relationships are checked to ensure that they
 * will not overwrite any existing relationships, then the <code>Element</code>
 * instance is inserted into the <code>DataStore</code> and finally the
 * relationships on the associated <code>Element</code> instances are updated
 * to point at the new <code>Element</code> instance as necessary.  Similarly,
 * when an <code>Element</code> instance is removed from the
 * <code>DataStore</code>, its relationships are checked to ensure that the
 * removal of the <code>Element</code> instance will not cause other
 * <code>Element</code> instances in the <code>DataStore</code> to become
 * invalid, then the relationships between the <code>Element</code> instance to
 * be removed and other <code>Element</code> instances in the
 * <code>DataStore</code> are broken, and finally the <code>Element</code>
 * instance is removed.
 * <p>
 * An instance of this class models one side of the relationship between two
 * <code>Element</code> classes.  While each instance of this class knows the
 * cardinality and navigability of the side of the relationship that it
 * represents it does not know those characteristics for the other side.  When
 * an operation is to be performed on a relationship the request is made to the
 * owning side (via the public interface).  The owning side will perform its
 * own safety checks, retrieve the <code>Relationship</code> instance for the
 * other side and pass on the request (via the protected interface) to perform
 * the actual operation.
 * <p>
 * All of the possible relationships can be represented through a combination
 * of any two of the <code>Relationship</code> implementations:
 *
 * <ul>
 * 	<li><code>SingleRelationship</code> represents a side of a relationship
 * 	    which is navigable with a cardinality of one
 * 	<li><code>MultiRelationship</code> represents a side of a relationship
 * 	    which is navigable with a cardinality greater then one
 * 	<li><code>SelectorRelationship</code> represents a side of a relationship
 * 	    which is not navigable
 * </ul>
 *
 * All relationships are modelled bi-directionally, with the
 * <code>SelectorRelationship</code> representing the side of a uni-directional
 * relationship that does not see the other side.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

public abstract class Relationship<T extends Element, V extends Element>
{
	/** The logger */
	protected final Logger log;

	/** The owning <code>Element</code> interface class */
	private final Class<T> type;

	/** The associated <code>Element</code> interface class */
	private final Class<V> value;

	/**
	 * Get a <code>Relationship</code> instance with a cardinality of one.
	 *
	 * @param  <T>       The type of the owning <code>Element</code>
	 * @param  <V>       The type of the associated <code>Element</code>
	 * @param  type      The owning <code>Element</code> interface class, not
	 *                   null
	 * @param  property  The <code>Property</code>, not null
	 * @param  reference The <code>PropertyReference</code>, not null
	 *
	 * @return           The <code>Relationship</code>
	 */

	protected static final <T extends Element, V extends Element> Relationship<T, V> getInstance (final Class<T> type, final Property<V> property, final PropertyReference<T, V> reference)
	{
		assert type != null : "type is NULL";
		assert property != null : "property is null";
		assert reference != null : "reference is NULL";

		return new SingleRelationship<T, V> (type, property, reference);
	}

	/**
	 * Get a <code>Relationship</code> instance with a cardinality greater than
	 * one.
	 *
	 * @param  <T>       The type of the owning <code>Element</code>
	 * @param  <V>       The type of the associated <code>Element</code>
	 * @param  type      The owning <code>Element</code> interface class, not
	 *                   null
	 * @param  property  The <code>Property</code>, not null
	 * @param  reference The <code>RelationshipReference</code>, not null
	 *
	 * @return           The <code>Relationship</code>
	 */

	protected static final <T extends Element, V extends Element> Relationship<T, V> getInstance (final Class<T> type, final Property<V> property, final RelationshipReference<T, V> reference)
	{
		assert type != null : "type is NULL";
		assert property != null : "property is NULL";
		assert reference != null : "reference is NULL";

		return new MultiRelationship<T, V> (type, property, reference);
	}

	/**
	 * Get a <code>Relationship</code> instance for the non-navigable side of a
	 * uni-directional relationship.  The supplied <code>Property</code>
	 * represents the owning <code>Element</code>, while the supplied
	 * <code>Selector</code> is used to get queries for the associated
	 * <code>Element</code>.
	 *
	 * @param  <T>      The type of the owning <code>Element</code>
	 * @param  <V>      The type of the associated <code>Element</code>
	 * @param  value    The <code>Element</code> interface class, not null
	 * @param  property The <code>Property</code>, not null
	 * @param  selector The <code>Selector</code>, not null
	 *
	 * @return          The <code>Relationship</code>
	 */

	protected static final <T extends Element, V extends Element> Relationship<T, V> getInstance (final Class<V> value, final Property<T> property, final Selector selector)
	{
		assert value != null : "value is NULL";
		assert property != null : "property is NULL";
		assert selector != null : "selector is NULL";

		return new SelectorRelationship<T, V> (value, property, selector);
	}

	/**
	 * Create the <code>Relationship</code>
	 *
	 * @param  type  The owning <code>Element</code> interface class, not null
	 * @param  value The associated <code>Element</code> interface class, not
	 *               null
	 */

	protected Relationship (final Class<T> type, final Class<V> value)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.type = type;
		this.value = value;
	}

	/**
	 * Get the inverse <code>Relationship</code> instance.  This method finds
	 * and returns the <code>Relationship</code> instance corresponding to the
	 * other side of the relationship for the specified <code>Element</code>
	 * implementation class.
	 *
	 * @param  impl The <code>Element</code> implementation class, not null
	 *
	 * @return      The inverse <code>Relationship</code>
	 */

	protected final Relationship<V, T> getInverse (final Class<? extends Element> impl)
	{
		this.log.trace ("getInverse: impl={}", impl);

		assert impl != null : "impl is NULL";
		assert this.value.isAssignableFrom (impl) : "impl is not an implementation class for this relationship";

		Relationship<V, T> inverse = null;

		if (Container.getInstance ().containsMetaData (impl))
		{
			inverse = Container.getInstance ().getMetaData (this.value, impl).getRelationship (this.type);

			if (inverse == null)
			{
				this.log.error ("Failed to retrieve Relationship for {} from {}", this.type.getSimpleName (), impl.getSimpleName ());
				throw new IllegalStateException ("No inverse relationship for element");
			}
		}
		else
		{
			this.log.error ("Element does not have a regitered MetaData instance: {}", impl.getSimpleName ());
			throw new IllegalStateException ("No MetaData registered for element");
		}

		return inverse;
	}

	/**
	 * Determine if a value can be inserted into the <code>Element</code> to
	 * create a relationship.  It is generally safe to insert a value into an
	 * <code>Element</code> if the <code>Element</code> allows multiple values
	 * for the relationship, of if the <code>Element</code> instance does not
	 * currently have a value for the relationship.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> instance to test, not null
	 *
	 * @return           <code>true</code> if the relationship can be safely
	 *                   inserted, <code>false</code> otherwise
	 */

	protected abstract boolean canInsert (final DataStore datastore, final T element);

	/**
	 * Determine if a value can be safely removed from the <code>Element</code>
	 * to break the relationship.  It is generally safe to remove a
	 * relationship if the <code>Element</code> represented does not require
	 * the relationship for unique identification.
	 *
	 * @return <code>true</code> if the relationship can be safely removed,
	 *         <code>false</code> otherwise
	 */

	protected abstract boolean canRemove ();

	/**
	 * Insert the specified value into the specified <code>Element</code> to
	 * create the relationship.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to operate on, not null
	 * @param  value     The <code>Element</code> to be inserted, not null
	 *
	 * @return           <code>true</code> if the value was successfully
	 *                   inserted, <code>false</code> otherwise
	 */

	protected abstract boolean insert (final DataStore datastore, final T element, final V value);

	/**
	 * Remove the specified value from the specified <code>Element</code> to
	 * break the relationship.
	 *
	 * @param  element The <code>Element</code> to operate on, not null
	 * @param  value   The <code>Element</code> to be removed, not null
	 *
	 * @return         <code>true</code> if the value was successfully
	 *                 removed, <code>false</code> otherwise
	 */

	protected abstract boolean remove (final T element, final V value);

	/**
	 * Determine if the relationship represented by this
	 * <code>Relationship</code> instance can be safely connected for the
	 * specified <code>Element</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship can be created,
	 *                   <code>false</code> otherwise
	 */

	public abstract boolean canConnect (final DataStore datastore, final T element);

	/**
	 * Determine if the relationship represented by this
	 * <code>Relationship</code> instance can be safely disconnected for the
	 * specified <code>Element</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship can be broken,
	 *                   <code>false</code> otherwise
	 */

	public abstract boolean canDisconnect (final DataStore datastore, final T element);

	/**
	 * Connect the relationship represented by this <code>Relationship</code>
	 * instance for the specified <code>Element</code> instance.  This method
	 * will write the inverse relationship into the <code>Element</code>
	 * instance for the <code>Element</code> on the other side of the
	 * relationship.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship was successfully
	 *                   created <code>false</code> otherwise
	 */

	public abstract boolean connect (final DataStore datastore, final T element);

	/**
	 * Disconnect the relationship represented by this
	 * <code>Relationship</code> instance for the specified
	 * <code>Element</code> instance.  This method will remove the inverse
	 * relationship from the <code>Element</code> instance for the
	 * <code>Element</code> on the other side of the relationship.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship was successfully
	 *                   broken <code>false</code> otherwise
	 */

	public abstract boolean disconnect (final DataStore datastore, final T element);
}
