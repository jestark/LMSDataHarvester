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

import java.util.Objects;

import javax.annotation.CheckReturnValue;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * A representation of a uni-directional relationship.  Specifically, instances
 * of this class represent the side of the relationship which can not see the
 * other side.  A <code>Selector</code> is used to represent the
 * <code>Element</code> and retrieve instances from the <code>DataStore</code>
 * to perform the necessary checks.
 *
 * @author  James E. Stark
 * @version 2.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class SelectorRelationship<T extends Element, V extends Element> implements Relationship<T, V>
{
	/** The Logger */
	private final Logger log;

	/** The other side of the <code>Relationship</code> */
	private final InverseRelationship<V, T> inverse;

	/** The <code>Property</code> for owning <code>Element</code> */
	private final Property<T> property;

	/** The <code>Selector</code> for the associated <code>Element</code> */
	private final Selector<V> selector;

	/**
	 * Create the <code>SelectorRelationship</code> from the specified values.
	 *
	 * @param  inverse   The <code>Inverse</code> relationship, not null
	 * @param  property The <code>Property</code>, not null
	 * @param  selector The <code>Selector</code>, not null
	 */

	public static <T extends Element, V extends Element> SelectorRelationship<T, V> of (
			final InverseRelationship<V, T> inverse,
			final Property<T> property,
			final Selector<V> selector)
	{
		assert inverse != null : "inverse is NULL";
		assert property != null : "property is NULL";
		assert selector != null : "selector is NULL";

		return new SelectorRelationship<T, V> (inverse, property, selector);
	}

	/**
	 * Create the <code>SelectorRelationship</code>.
	 *
	 * @param  inverse  The <code>Inverse</code> relationship, not null
	 * @param  property The <code>Property</code>, not null
	 * @param  selector The <code>Selector</code>, not null
	 */

	private SelectorRelationship (final InverseRelationship<V, T> inverse,
			final Property<T> property,
			final Selector<V> selector)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.inverse = inverse;
		this.property = property;
		this.selector = selector;
	}

	private Query<V> getQuery ()
	{
		return null; // model.getQuery (Container.getInstance ()
//				.getMetaData (this.value),
//				this.selector);
	}

	/**
	 * Compare two <code>SelectorRelationship</code> instances to determine if
	 * they are equal.
	 *
	 * @param  obj The <code>SelectorRelationship</code> instance to compare to
	 *             the one represented by the called instance
	 *
	 * @return     <code>true</code> if the two <code>SelectorRelationship</code>
	 *             instances are equal, <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof SelectorRelationship)
				&& Objects.equals (this.inverse, ((SelectorRelationship) obj).inverse)
				&& Objects.equals (this.property, ((SelectorRelationship) obj).property)
				&& Objects.equals (this.selector, ((SelectorRelationship) obj).selector);
	}

	/**
	 * Compute a hashCode for the <code>SelectorRelationship</code> instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.inverse, this.property, this.selector);
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>SelectorRelationship</code> instance, including the identifying
	 * fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>SelectorRelationship</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("property", this.property)
			.add ("selector", this.selector)
			.add ("inverse", this.inverse)
			.toString ();
	}

	/**
	 * Connect the relationship represented by this <code>Relationship</code>
	 * instance for the specified <code>Element</code> instance.
	 * <p>
	 * For a uni-directional relationship, there is nothing to do, so this is a
	 * no-op.
	 *
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship was successfully
	 *                 created <code>false</code> otherwise
	 */

	@Override
	public boolean connect (final T element)
	{
		this.log.trace ("connect: element={}", element);
		this.log.debug ("Connecting Relationship: {} -> {}",
				this.property.getName (), this.selector.getName ());

		assert element != null : "element";
		assert element.getDomainModel () != null : "missing DomainModel";
		assert element.getDomainModel ().contains (element) : "element is not in the model";

		return element.getDomainModel ().contains (element);
	}

	/**
	 * Disconnect the relationship represented by this
	 * <code>Relationship</code> instance for the specified
	 * <code>Element</code> instance.  This method will remove the inverse
	 * relationship from the <code>Element</code> instance for the
	 * <code>Element</code> on the other side of the relationship.
	 *
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship was successfully
	 *                 broken <code>false</code> otherwise
	 */

	@Override
	public boolean disconnect (final T element)
	{
		this.log.trace ("disconnect: element={}", element);
		this.log.debug ("Disconnecting Relationship: {} -> {}",
				this.property.getName (), this.selector.getName ());

		assert element != null : "element";
		assert element.getDomainModel () != null : "missing DomainModel";
		assert element.getDomainModel ().contains (element) : "element is not in the model";

		return element.getDomainModel ().contains (element) && this.getQuery ()
			.setValue (this.property, element)
			.queryAll ()
			.stream ()
			.allMatch (x -> this.inverse.remove (x, element));
	}
}
