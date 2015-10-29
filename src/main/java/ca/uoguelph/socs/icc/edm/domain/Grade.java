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

import com.google.common.base.MoreObjects;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
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

		Definition.getBuilder (Grade.class, Element.class)
			.addProperty (GRADE, Grade::getGrade, Grade::setGrade)
			.addRelationship (ACTIVITY, Grade::getActivityReference, Grade::setActivityReference)
			.addRelationship (ENROLMENT, Grade::getEnrolment, Grade::setEnrolment)
			.addSelector (SELECTOR_PKEY)
			.build ();
	}

	/**
	 * Get an instance of the <code>GradeBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>GradeBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Grade</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is
	 *                               immutable
	 */

	public static GradeBuilder builder (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return new GradeBuilder (datastore);
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
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return Grade.builder (model.getDataStore ());
	}

	/**
	 * Template method to create and initialize a <code>ToStringHelper</code>.
	 *
	 * @return The <code>ToStringHelper</code>
	 */

	@Override
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
	public boolean equalsAll (final Element element)
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
	public String toString()
	{
		return this.toStringHelper ()
			.toString ();
	}

	/**
	 * Get an <code>GradeBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates an
	 * <code>GradeBuilder</code> on the specified <code>DataStore</code> and
	 * initializes it with the contents of this <code>Grade</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>GradeBuilder</code>
	 */

	@Override
	public GradeBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return Grade.builder (datastore)
			.load (this);
	}

	/**
	 * Get the <code>MetaData</code> instance for this <code>Grade</code>
	 * using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<Grade> metadata ()
	{
		return this.getDomainModel ()
			.getDataStore ()
			.getProfile ()
			.getCreator (Grade.class, this.getClass ());
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
	 * <code>Grade</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Grade</code> instance is loaded.
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
	 * <code>Grade</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Grade</code> instance is loaded.
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
	 * <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Grade</code> instance is loaded.
	 *
	 * @param  grade The grade, on the interval [0, 100], not null
	 */

	protected abstract void setGrade (Integer grade);
}
