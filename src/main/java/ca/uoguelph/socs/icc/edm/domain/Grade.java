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

import java.util.Set;
import java.util.Objects;

import java.util.function.Supplier;

import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the grade received by a <code>User</code> for a
 * particular <code>Activity</code>.  Instances of the <code>Grade</code>
 * interface are identified by the associated instances of the
 * <code>Activity</code> and <code>Enrolment</code> interfaces.  As such, a
 * loader does not exist for the <code>Grade</code> interface, as the
 * <code>Grade</code> instance may be retrieved from the associated
 * <code>Enrolment</code> or <code>Activity</code>.
 * <p>
 * Within the domain model, <code>Grade</code> is a leaf level interface.  No
 * instances of any other domain model interface depend upon the existence of
 * an instance of the <code>Grade</code> interface.  Instances of the
 * <code>Grade</code> interface have strong dependencies upon instances of the
 * <code>Activity</code> and <code>Enrolment</code> interfaces.  If an instance
 * of the <code>Enrolment</code> or <code>Activity</code> interfaces is
 * deleted, then all of the associated instances of the <code>Grade</code>
 * interface must be deleted as well.
 * <p>
 * Once created, <code>Grade</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     GradeBuilder
 */

public abstract class Grade extends Element
{
	/** The <code>MetaData</code> for the <code>Grade</code> */
	protected static final MetaData<Grade> METADATA;

	/** The associated <code>Activity</code> */
	public static final Property<ActivityReference> ACTIVITY;

	/** The associated <code>Enrolment</code> */
	public static final Property<Enrolment> ENROLMENT;

	/** The assigned grade */
	public static final Property<Integer> GRADE;

	/** Select a <code>Grade</code> based the <code>Activity</code> and <code>Enrolment</code> */
	public static final Selector SELECTOR_PKEY;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Grade</code>.
	 */

	static
	{
		ACTIVITY = Property.getInstance (ActivityReference.class, "activity", Property.Flags.REQUIRED);
		ENROLMENT = Property.getInstance (Enrolment.class, "enrolment", Property.Flags.REQUIRED);
		GRADE = Property.getInstance (Integer.class, "grade", Property.Flags.REQUIRED, Property.Flags.MUTABLE);

		SELECTOR_PKEY = Selector.getInstance ("pkey", true, Grade.ACTIVITY, Grade.ENROLMENT);

		METADATA = MetaData.builder (Element.METADATA)
			.addProperty (GRADE, Grade::getGrade, Grade::setGrade)
			.addRelationship (ACTIVITY, Grade::getActivityReference, Grade::setActivityReference)
			.addRelationship (ENROLMENT, Grade::getEnrolment, Grade::setEnrolment)
			.addSelector (SELECTOR_PKEY)
			.build ();
	}

	/**
	 * Register an implementation.  This method handles the registration of an
	 * implementation class such that instances of it can be returned a
	 * <code>Builder</code> or a <code>Query</code>.
	 *
	 * @param  <T>      The implementation type
	 * @param  impl     The Implementation <code>Class</code>, not null
	 * @param  supplier Method reference to create a new instance, not null
	 */

	protected static <T extends Grade> void registerImplementation (final Class<T> impl, final Supplier<T> supplier)
	{

	}

	/**
	 * Get an instance of the <code>GradeBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>GradeBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Grade</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static GradeBuilder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>Grade</code>.
	 */

	protected Grade ()
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
			.add ("enrolment", this.getEnrolment ())
			.add ("activity", this.getActivity ())
			.add ("grade", this.getGrade ());
	}

	/**
	 * Compare two <code>Grade</code> instances to determine if they are equal.
	 * The <code>Grade</code> instances are compared based upon the associated
	 * <code>Activity</code> and the associated <code>Enrolment</code>.
	 *
	 * @param  obj The <code>Grade</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Grade</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Grade)
			&& Objects.equals (this.getActivity (), ((Grade) obj).getActivity ())
			&& Objects.equals (this.getEnrolment (), ((Grade) obj).getEnrolment ());
	}

	/**
	 * Compare two <code>Grade</code> instances to determine if they are equal
	 * using all of the instance fields.  For <code>Grade</code> the
	 * <code>equals</code> methods excludes the mutable fields from the
	 * comparison.  This methods compares two <code>Grade</code> instances
	 * using all of the fields.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 *
	 * @return         <code>True</code> if the two <code>Grade</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equalsAll (final @Nullable Element element)
	{
		return (element == this) ? true : (element instanceof Grade)
			&& Objects.equals (this.getActivity (), ((Grade) element).getActivity ())
			&& Objects.equals (this.getEnrolment (), ((Grade) element).getEnrolment ())
			&& Objects.equals (this.getGrade (), ((Grade) element).getGrade ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Grade</code> instance.
	 * The hash code is computed based upon associated <code>Activity</code>
	 * and the associated <code>Enrolment</code>.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getActivity (), this.getEnrolment ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>Grade</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Grade</code>
	 *         instance
	 */

	@Override
	@CheckReturnValue
	public String toString()
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
	public Set<Property<?>> properties ()
	{
		return Grade.METADATA.getProperties ();
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Selector</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Set<Selector> selectors ()
	{
		return Grade.METADATA.getSelectors ();
	}

	/**
	 * Determine if the value contained in the <code>Element</code> represented
	 * by the specified <code>Property</code> has the specified value.  If the
	 * <code>Property</code> represents a singe value, then this method will be
	 * equivalent to calling the <code>equals</code> method on the value
	 * represented by the <code>Property</code>.  This method is equivalent to
	 * calling the <code>contains</code> method for <code>Property</code>
	 * instances that represent collections.
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Property</code> equals/contains the specified value,
	 *         <code>false</code> otherwise.
	 */

	@Override
	public <V> boolean hasValue (final Property<V> property, final V value)
	{
		return Grade.METADATA.hasValue (property, this, value);
	}

	/**
	 * Get a <code>Stream</code> containing all of the values in this
	 * <code>Element</code> instance which are represented by the specified
	 * <code>Property</code>.  This method will return a <code>Stream</code>
	 * containing zero or more values.  For a single-valued
	 * <code>Property</code>, the returned <code>Stream</code> will contain
	 * exactly zero or one values.  An empty <code>Stream</code> will be
	 * returned if the associated value is null.  A <code>Stream</code>
	 * containing all of the values in the associated collection will be
	 * returned for multi-valued <code>Property</code> instances.
	 *
	 * @param  <V>      The type of the values in the <code>Stream</code>
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The <code>Stream</code>
	 */

	@Override
	public <V> Stream<V> stream (final Property<V> property)
	{
		return Grade.METADATA.getStream (property, this);
	}

	/**
	 * Get an <code>GradeBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>GradeBuilder</code> on the specified <code>DomainModel</code> and
	 * initializes it with the contents of this <code>Grade</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>GradeBuilder</code>
	 */

	@Override
	public GradeBuilder getBuilder (final DomainModel model)
	{
		return Grade.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the <code>Activity</code> for which the <code>Grade</code> is
	 * assigned.
	 *
	 * @return The associated <code>Activity</code>
	 */

	public abstract Activity getActivity ();

	/**
	 * Get the <code>ActivityReference</code> for which the <code>Grade</code>
	 * is assigned.
	 *
	 * @return The associated <code>ActivityReference</code>
	 */

	protected abstract ActivityReference getActivityReference ();

	/**
	 * Set the <code>ActivityReference</code> which is associated with the
	 * <code>Grade</code>.  This method is intended to be used to initialize a
	 * new <code>Grade</code> instance.
	 *
	 * @param  activity The <code>ActivityReference</code>, not null
	 */

	protected abstract void setActivityReference (ActivityReference activity);

	/**
	 * Get the <code>Enrolment</code>, for the student, to which the
	 * <code>Grade</code> is assigned
	 *
	 * @return The associated <code>Enrolment</code>
	 */

	public abstract Enrolment getEnrolment ();

	/**
	 * Set the <code>Enrolment</code> which is associated with the
	 * <code>Grade</code>.  This method is intended to be used to initialize a
	 * new <code>Grade</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code>, not null
	 */

	protected abstract void setEnrolment (Enrolment enrolment);

	/**
	 * Get the grade that the student received for the <code>Activity</code>.
	 * The grade will be an <code>Integer</code> with a value on the range of
	 * [0, 100].
	 *
	 * @return An <code>Integer</code> containing the assigned grade.
	 */

	public abstract Integer getGrade ();

	/**
	 * Set the numeric grade assigned to the <code>Enrolment</code> for the
	 * <code>Activity</code>.  This method is intended to be used to initialize
	 * a new <code>Grade</code> instance.
	 *
	 * @param  grade The grade, on the interval [0, 100], not null
	 */

	protected abstract void setGrade (Integer grade);
}
