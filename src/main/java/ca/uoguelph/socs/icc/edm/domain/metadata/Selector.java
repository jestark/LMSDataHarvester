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

import java.util.Set;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

import java.util.stream.Collectors;

import javax.annotation.CheckReturnValue;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * A representation of a set of <code>Element</code> instances.  This class
 * contains a <code>Set</code> of <code>Property</code> instances which identify
 * a set instances, for a given <code>Element</code> interface class, within the
 * <code>DomainModel</code>.
 * <p>
 * The maximum expected size of the set of <code>Element</code> instances which
 * are associated with a given <code>Selector</code> is specified by the
 * <code>Cardinality</code> enum.  The <code>Cardinality</code> can have the
 * following values:
 *
 * <ul>
 * <li> MULTIPLE: Indicating that the <code>Selector</code> is expected to match
 *                more then one <code>Element</code> instance
 * <li> SINGLE:   Indicating that the <code>Selector</code> is expected to match
 *                only one <code>Element</code> instance
 * <li> KEY:      A special case of SINGLE, indicating that the
 *                <code>Property</code> contained in the <code>Selector</code>
 *                is the primary key for the <code>Element</code>.
 * </ul>
 * <p>
 * If the cardinality is set to KEY, then the <code>Selector</code> must contain
 * exactly one <code>Property</code> instance.  Otherwise, the difference
 * between KEY and SINGLE is implementation dependant for the
 * <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     MetaData
 * @see     Property
 */

public final class Selector<T extends Element>
{
	/**
	 * A representation of the maximum cardinality of the set of
	 * <code>Element</code> represented by the <code>Selector</code>.
	 */

	public static enum Cardinality
	{
		/** The <code>Selector</code> represents the primary key for the <code>Element</code> */
		KEY,

		/** The <code>Selector</code> represents a single <code>Eleemnt</code> instance */
		SINGLE,

		/** The <code>Selector</code> represents multiple <code>Eleemnt</code> instances */
		MULTIPLE;
	}

	/**
	 * A builder for <code>Selector</code> instances.  This builder is intended
	 * to be used to construct the more complex <code>Selector</code> instances
	 * which are not based on a single <code>Property</code> instance.
	 * <p>
	 * A <code>Selector</code> must have a <code>Cardinality</code> and a name.
	 * The name may be set explicitly, or it may be derived from the first
	 * <code>Property</code> which is added to the <code>Selector</code>.  If
	 * The <code>Selector</code> has a cardinality of KEY, then it must contain
	 * exactly one <code>Property</code>.  Otherwise everything is optional.
	 */

	public static final class Builder<T extends Element>
	{
		/** The <code>Element</code> interface class */
		private final Class<T> element;

		/** The <code>Set</code> of <code>Property</code> instances */
		private final Set<Property<T, ?>> properties;

		/** The <code>Cardinality</code> of the <code>selector</code> */
		private Cardinality cardinality;

		/** The name of the <code>Selector</code> */
		private String name;

		/**
		 * Create the <code>Selector.Builder</code>
		 *
		 * @param  element The <code>Element</code> interface class, not null
		 */

		private Builder (final Class<T> element)
		{
			assert element != null : "element is NULL";

			this.element = element;
			this.cardinality = null;
			this.name = null;

			this.properties = new HashSet<> ();
		}

		/**
		 * Remove all of the <code>Property</code> instances from the
		 * <code>Selector.Builder</code>.
		 *
		 * @return This <code>Selector.Builder</code>
		 */

		public Builder<T> clear ()
		{
			this.properties.clear ();

			return this;
		}

		/**
		 * Set the <code>Cardinality</code> of the <code>Selector</code>.
		 *
		 * @param  cardinality The <code>Cardinality</code>, not null
		 *
		 * @return             This <code>Selector.Builder</code>
		 */

		public Builder<T> setCardinality (final Cardinality cardinality)
		{
			Preconditions.checkNotNull (cardinality);

			this.cardinality = cardinality;

			return this;
		}

		/**
		 * Set the name of the <code>Selector</code>.
		 *
		 * @param  name The name, not null
		 *
		 * @return      This <code>Selector.Builder</code>
		 */

		public Builder<T> setName (final String name)
		{
			Preconditions.checkNotNull (name);
			Preconditions.checkArgument (name.length () > 0, "name can not be empty");

			this.name = name;

			return this;
		}

		/**
		 * Add a <code>Property</code> to the <code>Selector</code>.  If the
		 * <code>Selector</code> is un-named, then the name of the
		 * <code>Property</code> will be set as the name of the
		 * <code>Selector</code>.
		 *
		 * @param  property The <code>Property</code>, not null
		 *
		 * @return          This <code>Selector.Builder</code>
		 */

		public Builder<T> addProperty (final Property<T, ?> property)
		{
			Preconditions.checkNotNull (property);

			this.properties.add (property);

			if (this.name == null)
			{
				this.name = property.getName ();
			}

			return this;
		}

		/**
		 * Build the <code>Selector</code>.
		 *
		 * @return The <code>Selector</code>
		 *
		 * @throws IllegalStateException if the name is not set
		 * @throws IllegalStateException if the cardinality is not set
		 * @throws IllegalStateException if the cardinality is set to KEY and
		 *                               the number of associated
		 *                               <code>Property</code> instances is not
		 *                               exactly one.
		 */

		public Selector<T> build ()
		{
			Preconditions.checkState (this.name != null, "name");
			Preconditions.checkState (this.cardinality != null, "cardinality");
			Preconditions.checkState ((this.cardinality != Cardinality.KEY)
					|| this.properties.size () == 1,
					"A KEY must have exactly one Property");

			return new Selector<T> (this.element, this.cardinality, this.name, this.properties);
		}
	}

	/** The <code>Element</code> type described by the <code>Selector</code> */
	private final Class<T> element;

	/** The name of the <code>Selector</code> */
	private final String name;

	/** Are all of the associated <code>Property</code> instances immutable */
	private final boolean constant;

	/** The <code>Cardinality</code> of the result set */
	private final Cardinality cardinality;

	/** The <code>Property</code> instances */
	private final Set<Property<T, ?>> properties;

	/**
	 * Get a <code>Selector.Builder</code> instance for the specified
	 * <code>Element</code> interface class.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 *
	 * @return         The <code>Selector.Builder</code> instance
	 */

	public static <T extends Element> Builder<T> builder (final Class<T> element)
	{
		Preconditions.checkNotNull (element);

		return new Builder<T> (element);
	}

	/**
	 * Create the <code>Selector</code> using a single <code>Property</code>.
	 *
	 * @param  cardinality The <code>Cardinality</code> of the result, not null
	 * @param  property    The property to be represented by the
	 *                     <code>Selector</code>, not null
	 *
	 * @return             The <code>Selector</code>
	 */

	public static <T extends Element> Selector<T> of (
			final Cardinality cardinality,
			final Property<T, ?> property)
	{
		Preconditions.checkNotNull (cardinality, "cardinality");
		Preconditions.checkNotNull (property, "property");

		Set<Property<T, ?>> properties = new HashSet<> ();
		properties.add (property);

		return new Selector<T> (property.getElementClass (), cardinality, property.getName (), properties);
	}

	/**
	 * Create the <code>Selector</code>.
	 *
	 * @param  element     The <code>Element</code> interface class, not null
	 * @param  name        The name of the <code>Selector</code>
	 * @param  cardinality The <code>Cardinality</code> of the result, not null
	 * @param  properties  The <code>Set</code> of <code>Property</code>
	 *                     instances represented by the <code>Selector</code>
	 */

	private Selector (
			final Class<T> element,
			final Cardinality cardinality,
			final String name,
			final Set<Property<T, ?>> properties)
	{
		this.element = element;
		this.name = name;
		this.cardinality = cardinality;
		this.properties = properties;

		this.constant = this.properties.stream ()
			.allMatch (p -> ! p.hasFlags (Property.Flags.MUTABLE));
	}

	/**
	 * Compare two <code>Selector</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>Selector</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>true</code> if the two <code>Selector</code> instances
	 *             are equal, <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Selector)
			&& Objects.equals (this.element, ((Selector) obj).element)
			&& Objects.equals (this.cardinality, ((Selector) obj).cardinality)
			&& Objects.equals (this.name, ((Selector) obj).name)
			&& Objects.equals (this.properties, ((Selector) obj).properties);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Selector</code> instance.
	 *
	 * @return The hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.element, this.cardinality, this.name, this.properties);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Selector</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Selector</code>
	 *         instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("element", this.element)
			.add ("cardinality", this.cardinality)
			.add ("name", this.name)
			.add ("properties", this.properties)
			.toString ();
	}

	/**
	 * Get the <code>Element</code> interface class that is described by the
	 * <code>Selector</code>.
	 *
	 * @return The <code>Element</code> interface class
	 */

	public Class<T> getElementClass ()
	{
		return this.element;
	}

	/**
	 * Get the name of the <code>Selector</code>.
	 *
	 * @return  A <code>String</code> containing the name
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Get the <code>Cardinality</code> of the set of <code>Element</code>
	 * instances represented by this<code>Selector</code>.
	 *
	 * @return The <code>Cardinality</code>
	 */

	public Cardinality getCardinality ()
	{
		return this.cardinality;
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances
	 * corresponding to the <code>Element</code>.
	 *
	 * @return A <code>Set</code> containing all of the <code>Property</code>
	 *         instances for the <code>Element</code>
	 */

	public Set<Property<T, ?>> getProperties ()
	{
		return Collections.unmodifiableSet (this.properties);
	}

	/**
	 * Determine if all of the <code>Property</code> instances associated with
	 * this <code>Selector</code> are immutable.
	 *
	 * @return <code>true</code> if all of the associated <code>Property</code>
	 *         instances are immutable, <code>false</code> otherwise
	 */

	public boolean isConstant ()
	{
		return this.constant;
	}
}
