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

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Abstract representation of a relationship between to <code>Element</code>
 * classes.  To ensure the integrity of the <code>DomainModel</code>, the
 * relationships between <code>Element</code> instances are subject to
 * constraints.  Both of the <code>Elements</code> involved in a relationship
 * must exist in the same <code>DomainModel</code> and a relationship between
 * any two <code>Element</code> instances must be valid for all of the
 * <code>Element</code> instances in the <code>DomainModel</code>. Specifically,
 * the <code>DomainModel</code> must prevent an existing relationship between
 * two <code>Element</code> instances from being overwritten by the addition of
 * a third <code>Element</code> instance.
 * <p>
 * To ensure its integrity, the <code>DomainModel</code> must process the
 * relationships for each <code>Element</code> instance as it is added or
 * removed.  When an <code>Element</code> instance is added to the
 * <code>DomainModel</code>, its relationships are checked to ensure that they
 * will not overwrite any existing relationships, then the <code>Element</code>
 * instance is inserted into the <code>DomainModel</code> and finally the
 * relationships on the associated <code>Element</code> instances are updated
 * to point at the new <code>Element</code> instance as necessary.  Similarly,
 * when an <code>Element</code> instance is removed from the
 * <code>DomainModel</code>, its relationships are checked to ensure that the
 * removal of the <code>Element</code> instance will not cause other
 * <code>Element</code> instances in the <code>DomainModel</code> to become
 * invalid, then the relationships between the <code>Element</code> instance to
 * be removed and other <code>Element</code> instances in the
 * <code>DomainModel</code> are broken, and finally the <code>Element</code>
 * instance is removed.
 * <p>
 * An instance of this interface models one side of the relationship between two
 * <code>Element</code> classes.  While each instance of this class knows the
 * cardinality and navigability of the side of the relationship that it
 * represents it does not know those characteristics for the other side.  When
 * an operation is to be performed on a relationship the request is made to the
 * owning side.  The owning side will perform its own safety checks then pass
 * on the request to the other side to perform the actual operation.
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
 * @version 2.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

public interface Relationship<T extends Element, V extends Element>
{
	/**
	 * Abstract representation of the inverse relationship.  All relationships
	 * are modelled bi-directionally, with instances of the
	 * <code>Relationship</code> interface modelling the owning side (the side
	 * from which an operation is initiated).  Implementations of this interface
	 * model the other side, which does the actual work of adding or breaking
	 * the relationship.  This is an internal interface, who's instances should
	 * only be called from instances of the <code>Relationship</code> interface.
	 *
	 * @version 1.0
	 * @param   <T> The type of the owning <code>Element</code>
	 * @param   <V> The type of the associated <code>Element</code>
	 */

	public static interface Inverse<T extends Element, V extends Element>
	{
		/**
		 * Determine if a value can be inserted into the <code>Element</code> to
		 * create a relationship.  It is generally safe to insert a value into
		 * an <code>Element</code> if the <code>Element</code> allows multiple
		 * values for the relationship, of if the <code>Element</code> instance
		 * does not currently have a value for the relationship.
		 *
		 * @param  model   The <code>DomainModel</code>, not null
		 * @param  element The <code>Element</code> instance to test, not null
		 *
		 * @return         <code>true</code> if the relationship can be safely
		 *                 inserted, <code>false</code> otherwise
		 */

		public abstract boolean canInsert (final DomainModel model, final T element);

		/**
		 * Determine if a value can be safely removed from the
		 * <code>Element</code> to break the relationship.  It is generally safe
		 * to remove a relationship if the <code>Element</code> represented does
		 * not require the relationship for unique identification.
		 *
		 * @return <code>true</code> if the relationship can be safely removed,
		 *         <code>false</code> otherwise
		 */

		public abstract boolean canRemove ();

		/**
		 * Insert the specified value into the specified <code>Element</code> to
		 * create the relationship.
		 *
		 * @param  model   The <code>DomainModel</code>, not null
		 * @param  element The <code>Element</code> to operate on, not null
		 * @param  value   The <code>Element</code> to be inserted, not null
		 *
		 * @return         <code>true</code> if the value was successfully
		 *                 inserted, <code>false</code> otherwise
		 */

		public abstract boolean insert (final DomainModel model, final T element, final V value);

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

		public abstract boolean remove (final T element, final V value);
	}

	/**
	 * Determine if the relationship represented by this
	 * <code>Relationship</code> instance can be safely connected for the
	 * specified <code>Element</code> instance.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship can be created,
	 *                 <code>false</code> otherwise
	 */

	public abstract boolean canConnect (final DomainModel model, final T element);

	/**
	 * Determine if the relationship represented by this
	 * <code>Relationship</code> instance can be safely disconnected for the
	 * specified <code>Element</code> instance.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship can be broken,
	 *                 <code>false</code> otherwise
	 */

	public abstract boolean canDisconnect (final DomainModel model, final T element);

	/**
	 * Connect the relationship represented by this <code>Relationship</code>
	 * instance for the specified <code>Element</code> instance.  This method
	 * will write the inverse relationship into the <code>Element</code>
	 * instance for the <code>Element</code> on the other side of the
	 * relationship.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship was successfully
	 *                 created <code>false</code> otherwise
	 */

	public abstract boolean connect (final DomainModel model, final T element);

	/**
	 * Disconnect the relationship represented by this
	 * <code>Relationship</code> instance for the specified
	 * <code>Element</code> instance.  This method will remove the inverse
	 * relationship from the <code>Element</code> instance for the
	 * <code>Element</code> on the other side of the relationship.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> to process, not null
	 *
	 * @return         <code>true</code> if the relationship was successfully
	 *                 broken <code>false</code> otherwise
	 */

	public abstract boolean disconnect (final DomainModel model, final T element);
}
