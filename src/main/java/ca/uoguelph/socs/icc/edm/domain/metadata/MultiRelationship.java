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
import java.util.Objects;

import javax.annotation.CheckReturnValue;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Representation of a relationship for an <code>Element</code> with a
 * cardinality greater than one.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class MultiRelationship<T extends Element, V extends Element> implements Relationship<T, V>
{
	/** The Logger */
	private final Logger log;

	/** The other side of the <code>Relationship</code> */
	private final InverseRelationship<V, T> inverse;

	/** The <code>MultiRelationship</code> used to manipulate the element */
	private final MultiAccessor<T, V> reference;

	/**
	 * Create the <code>MultiRelationship</code> from the specified values.
	 *
	 * @param  inverse   The <code>Inverse</code> relationship, not null
	 * @param  reference The <code>MultiAccessor</code>, not null
	 */

	public static <T extends Element, V extends Element> MultiRelationship<T, V> of (
			final InverseRelationship<V, T> inverse,
			final MultiAccessor<T, V> reference)
	{
		assert inverse != null : "inverse is NULL";
		assert reference != null : "reference is NULL";

		return new MultiRelationship<T, V> (inverse, reference);
	}

	/**
	 * Create the <code>MultiRelationship</code>.
	 *
	 * @param  inverse   The <code>Inverse</code> relationship, not null
	 * @param  reference The <code>RelationshipReference</code>, not null
	 */

	private MultiRelationship (final InverseRelationship<V, T> inverse, final MultiAccessor<T, V> reference)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.inverse = inverse;
		this.reference = reference;
	}

	/**
	 * Compare two <code>MultiRelationship</code> instances to determine if
	 * they are equal.
	 *
	 * @param  obj The <code>MultiRelationship</code> instance to compare to
	 *             the one represented by the called instance
	 *
	 * @return     <code>true</code> if the two <code>MultiRelationship</code>
	 *             instances are equal, <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof MultiRelationship)
				&& Objects.equals (this.inverse, ((MultiRelationship) obj).inverse)
				&& Objects.equals (this.reference, ((MultiRelationship) obj).reference);
	}

	/**
	 * Compute a hashCode for the <code>MultiRelationship</code> instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.inverse, this.reference);
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>MultiRelationship</code> instance, including the identifying
	 * fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>MultiRelationship</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("reference", this.reference)
			.add ("inverse", this.inverse)
			.toString ();
	}

	/**
	 * Connect the relationship represented by this <code>Relationship</code>
	 * instance for the specified <code>Element</code> instance.  This method
	 * will write the inverse relationship into the <code>Element</code>
	 * instance for the <code>Element</code> on the other side of the
	 * relationship.
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
				this.reference.getElementClass ().getSimpleName (),
				this.reference.getProperty ().getName ());

		assert element != null : "element";
		assert element.getDomainModel () != null : "missing DomainModel";
		assert element.getDomainModel ().contains (element) : "element is not in the model";

		return element.getDomainModel ().contains (element) && this.reference.stream (element)
			.allMatch (x -> this.inverse.insert (x, element));
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
				this.reference.getElementClass ().getSimpleName (),
				this.reference.getProperty ().getName ());

		assert element != null : "element";
		assert element.getDomainModel () != null : "missing DomainModel";
		assert element.getDomainModel ().contains (element) : "element is not in the model";

		return element.getDomainModel ().contains (element) && this.reference.stream (element)
			.allMatch (x -> this.inverse.remove (x, element));
	}
}
