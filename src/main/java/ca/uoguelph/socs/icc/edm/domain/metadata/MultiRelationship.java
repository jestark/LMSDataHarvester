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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Representation of a relationship for an <code>Element</code> with a
 * cardinality greater than one.
 *
 * @author  James E. Stark
 * @version 2.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

final class MultiRelationship<T extends Element, V extends Element> implements Relationship<T, V>
{
	/**
	 * Representation of a inverse relationship for an <code>Element</code> with
	 * a cardinality greater than one.
	 *
	 * @version 1.0
	 * @param   <T> The type of the owning <code>Element</code>
	 * @param   <V> The type of the associated <code>Element</code>
	 */

	static final class MultiInverse <T extends Element, V extends Element> implements Relationship.Inverse<T, V>
	{
		/** The Logger */
		private final Logger log;

		/** The <code>RelationshipReference</code> used to manipulate the element */
		private final MultiReference<T, V> reference;

		/**
		 * Create the <code>MultiInverse</code>
		 *
		 * @param  reference The <code>MultiReference</code>, not null
		 */

		protected MultiInverse (final MultiReference<T, V> reference)
		{
			assert reference != null : "reference is NULL";

			this.log = LoggerFactory.getLogger (this.getClass ());

			this.reference = reference;
		}

		/**
		 * Determine if a value can be inserted into the <code>Element</code> to
		 * create a relationship.  It is generally safe to insert a value into
		 * an <code>Element</code> if the <code>Element</code> allows multiple
		 * values for the relationship, of if the <code>Element</code> instance
		 * does not currently have a value for the relationship.
		 * <p>
		 * For the multi-valued relationship, a new relationship can always be
		 * inserted
		 *
		 * @param  element The <code>Element</code> instance to test, not null
		 *
		 * @return         <code>true</code> if the relationship can be safely
		 *                 inserted, <code>false</code> otherwise
		 */

		@Override
		public boolean canInsert (final DomainModel model, final T element)
		{
			this.log.trace ("canInsert: element={}", element);

			assert model != null : "model is null";
			assert element != null : "element is NULL";
			assert model.contains (element) : "element is not in the model";

			return model.contains (element);
		}

		/**
		 * Determine if a value can be safely removed from the
		 * <code>Element</code> to break the relationship.  It is generally safe
		 * to remove a relationship if the <code>Element</code> represented does
		 * not require the relationship for unique identification.
		 * <p>
		 * For the multi-valued relationship, a relationship can always be
		 * broken.
		 *
		 * @return <code>true</code> if the relationship can be safely removed,
		 *         <code>false</code> otherwise
		 */

		@Override
		public boolean canRemove ()
		{
			this.log.trace ("canRemove:");

			return true;
		}

		/**
		 * Insert the specified value into the specified <code>Element</code> to
		 * create the relationship.
		 * <p>
		 * This method adds the specified value to the <code>Collection</code>
		 * which contains all of the values for the relationship.  Note that it
		 * may be possible to add the same value to an <code>Element</code>
		 * multiple times, depending of the implementation of the
		 * <code>Element</code>.  The called must ensure that this does not
		 * happen.
		 *
		 * @param  model   The <code>DomainModel</code>, not null
		 * @param  element The <code>Element</code> to operate on, not null
		 * @param  value   The <code>Element</code> to be inserted, not null
		 *
		 * @return         <code>true</code> if the value was successfully
		 *                 inserted, <code>false</code> otherwise
		 */

		@Override
		public boolean insert (final DomainModel model, final T element, final V value)
		{
			this.log.trace ("insert: element={}, value={}", element, value);
//			this.log.debug ("inserting Relationship: {} -> {}", this.type.getSimpleName (), this.value.getSimpleName ());

			assert model != null : "model is null";
			assert element != null : "element is NULL";
			assert value != null : "value is NULL";
			assert model.contains (element) : "element is not in the model";
			assert model.contains (value) : "value is not in the model";

			return model.contains (element) && this.reference.addValue (element, value);
		}

		/**
		 * Remove the specified value from the specified <code>Element</code> to
		 * break the relationship.
		 * <p>
		 * This method removes the specified value from the
		 * <code>Collection</code> which contains all of the values for the
		 * relationship.
		 *
		 * @param  element The <code>Element</code> to operate on, not null
		 * @param  value   The <code>Element</code> to be removed, not null
		 *
		 * @return         <code>true</code> if the value was successfully
		 *                 removed, <code>false</code> otherwise
		 */

		@Override
		public boolean remove (final T element, final V value)
		{
			this.log.trace ("remove: element={}, value={}", element, value);
//			this.log.debug ("removing Relationship: {} -> {}", this.type.getSimpleName (), this.value.getSimpleName ());

			assert element != null : "element is NULL";
			assert value != null : "value is NULL";

			return this.reference.removeValue (element, value);
		}
	}

	/** The Logger */
	private final Logger log;

	/** The other side of the <code>Relationship</code> */
	private final Relationship.Inverse<V, T> inverse;

	/** The <code>MultiRelationship</code> used to manipulate the element */
	private final MultiReference<T, V> reference;

	/**
	 * Create the <code>MultiRelationship</code>.
	 *
	 * @param  inverse   The <code>Inverse</code> relationship, not null
	 * @param  reference The <code>RelationshipReference</code>, not null
	 */

	protected MultiRelationship (final Relationship.Inverse<V, T> inverse, final MultiReference<T, V> reference)
	{
		assert inverse != null : "inverse is NULL";
		assert reference != null : "reference is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.inverse = inverse;
		this.reference = reference;
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

	@Override
	public boolean canConnect (final DomainModel model, final T element)
	{
		this.log.trace ("canConnect: element={}", element);

		assert model != null : "model is null";
		assert element != null : "element is NULL";

		return this.reference.stream (element)
			.allMatch (x -> this.inverse.canInsert (model, x));
	}

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

	@Override
	public boolean canDisconnect (final DomainModel model, final T element)
	{
		this.log.trace ("canDisconnect: element={}", element);

		assert model != null : "model is null";
		assert element != null : "element is NULL";
		assert model.contains (element) : "element is not in the model";

		return model.contains (element) && this.reference.stream (element)
			.allMatch (x -> this.inverse.canRemove ());
	}

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

	@Override
	public boolean connect (final DomainModel model, final T element)
	{
		this.log.trace ("connect: element={}", element);
//		this.log.debug ("Connecting Relationship: {} -> {}", this.type.getSimpleName (), this.value.getSimpleName ());

		assert model != null : "model is null";
		assert element != null : "element is NULL";
		assert model.contains (element) : "element is not in the model";

		return model.contains (element) && this.reference.stream (element)
			.allMatch (x -> this.inverse.insert (model, x, element));
	}

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

	@Override
	public boolean disconnect (final DomainModel model, final T element)
	{
		this.log.trace ("disconnect: element={}", element);
//		this.log.debug ("Disconnecting Relationship: {} -> {}", this.type.getSimpleName (), this.value.getSimpleName ());

		assert model != null : "model is null";
		assert element != null : "element is NULL";
		assert model.contains (element) : "element is not in the model";

		return model.contains (element) && this.reference.stream (element)
			.allMatch (x -> this.inverse.remove (x, element));
	}
}
