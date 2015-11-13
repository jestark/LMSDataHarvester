/* Copyright (C) 2014, 2015 James E. Stark
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

import java.util.Objects;

import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the nature of a particular <code>Activity</code>.
 * Instances of the <code>ActivityType</code> interface serve to describe the
 * nature of the instances of the <code>Activity</code> interface with which
 * they are associated.  Example <code>ActivityType</code> instances include:
 * "Assignment," "Quiz," and "Presentation."  For instances of
 * <code>Activity</code> where the data was harvested from a Learning
 * Management System (such as Moodle) the <code>ActivityType</code> will be the
 * name of the module from which the data contained in the associated
 * <code>Activity</code> instances was harvested.
 * <p>
 * Instances <code>ActvityType</code> interface has a strong dependency on the
 * associated instances of the <code>ActivitySource</code> interface.  If an
 * instance of a particular <code>ActivitySource</code> is removed from the
 * <code>DomainModel</code> then all of the associated instances of the
 * <code>ActivityType</code> interface must be removed as well.  Similarly,
 * instances of the <code>Activity</code> interface are dependent on the
 * associated instance of the <code>ActivityType</code> interface.  Removing an
 * instance of the <code>ActivityType</code> interface from the
 * <code>DomainModel</code> will require the removal of the associated instances
 * of the <code>Activity</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityTypeBuilder
 * @see     ActivityTypeLoader
 */

public abstract class ActivityType extends Element
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>ActivityType</code> */
	protected static final MetaData<ActivityType> METADATA;

	/** The <code>DataStore</code> identifier of the <code>ActivityType</code> */
	public static final Property<ActivityType, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>ActivityType</code> */
	public static final Property<ActivityType, DomainModel> MODEL;

	/** The name of the <code>ActivityType</code> */
	public static final Property<ActivityType, String> NAME;

	/** The associated <code>ActivitySource</code> */
	public static final Property<ActivityType, ActivitySource> SOURCE;

	/** Select the <code>ActivityType</code> instance by its id */
	public static final Selector<ActivityType> SELECTOR_ID;

	/** Select all of the <code>ActivityType</code> instances */
	public static final Selector<ActivityType> SELECTOR_ALL;

	/** Select an <code>ActivityType</code> instance by name and <code>ActivitySource</code> */
	public static final Selector<ActivityType> SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>ActivityType</code>.
	 */

	static
	{
		ID = Property.of (ActivityType.class, Long.class, "id",
				ActivityType::getId, ActivityType::setId);

		MODEL = Property.of (ActivityType.class, DomainModel.class, "domainmodel",
				ActivityType::getDomainModel, ActivityType::setDomainModel);

		NAME = Property.of (ActivityType.class, String.class, "name",
				ActivityType::getName, ActivityType::setName,
				Property.Flags.REQUIRED);

		SOURCE = Property.of (ActivityType.class, ActivitySource.class, "source",
				ActivityType::getSource, ActivityType::setSource,
				Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);

		SELECTOR_ALL = Selector.builder (ActivityType.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		SELECTOR_NAME = Selector.builder (ActivityType.class)
			.setCardinality (Selector.Cardinality.SINGLE)
			.addProperty (NAME)
			.addProperty (SOURCE)
			.build ();

		METADATA = MetaData.builder (ActivityType.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (NAME)
			.addRelationship (SOURCE, ActivitySource.METADATA, ActivitySource.TYPES)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_NAME)
			.build ();
	}

	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>ActivityTypeBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivityType</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static ActivityTypeBuilder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>ActivityType</code>.
	 */

	protected ActivityType ()
	{
		super ();
	}

	/**
	 * Template method to create and initialize a <code>ToStringHelper</code>.
	 *
	 * @return The <code>ToStringHelper</code>
	 */

	@Override
	@CheckReturnValue
	protected MoreObjects.ToStringHelper toStringHelper ()
	{
		return super.toStringHelper ()
			.add ("source", this.getSource ())
			.add ("name", this.getName ());
	}

	/**
	 * Compare two <code>ActivityType</code> instances to determine if they are
	 * equal.  The <code>ActivityType</code> instances are compared based upon
	 * their associated <code>ActivitySource</code> and their names.
	 *
	 * @param  obj The <code>ActivityType</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>ActivityType</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof ActivityType)
			&& Objects.equals (this.getName (), ((ActivityType) obj).getName ())
			&& Objects.equals (this.getSource (), ((ActivityType) obj).getSource ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>ActivityType</code>
	 * instance.  The hash code is computed based upon the associated
	 * <code>ActivitySource</code> and name of the instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getName (), this.getSource ());
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>ActivityType</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>ActivityType</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return this.toStringHelper ()
			.toString ();
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Property<? extends Element, ?>> properties ()
	{
		return ActivityType.METADATA.properties ();
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Selector</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Selector<? extends Element>> selectors ()
	{
		return ActivityType.METADATA.selectors ();
	}

	/**
	 * Get an <code>ActivityTypeBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>ActivityTypeBuilder</code> on the specified <code>DomainModel</code>
	 * and initializes it with the contents of this <code>ActivityType</code>
	 * instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>ActivityTypeBuilder</code>
	 */

	@Override
	public ActivityTypeBuilder getBuilder (final DomainModel model)
	{
		return ActivityType.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>ActivityType</code>.  This method is intended
	 * to be used to initialize a new <code>ActivityType</code> instance.
	 *
	 * @param  name The name of the <code>ActivityType</code>
	 */

	protected abstract void setName (String name);

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	public abstract ActivitySource getSource ();

	/**
	 * Set the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 * This method is intended to be used initialize a new
	 * <code>ActivityType</code> instance.
	 *
	 * @param  source The <code>ActivitySource</code> for the
	 *                <code>ActivityType</code>
	 */

	protected abstract void setSource (ActivitySource source);
}
