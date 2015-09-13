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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
	public static final Property<Activity> ACTIVITY;

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
		ACTIVITY = Property.getInstance (Activity.class, "activity", Property.Flags.REQUIRED);
		ENROLMENT = Property.getInstance (Enrolment.class, "enrolment", Property.Flags.REQUIRED);
		GRADE = Property.getInstance (Integer.class, "grade", Property.Flags.REQUIRED, Property.Flags.MUTABLE);

		SELECTOR_PKEY = Selector.getInstance ("pkey", true, Grade.ACTIVITY, Grade.ENROLMENT);

		Definition.getBuilder (Grade.class, Element.class)
			.addProperty (GRADE, Grade::getGrade, Grade::setGrade)
			.addRelationship (ACTIVITY, Grade::getActivity, Grade::setActivity)
			.addRelationship (ENROLMENT, Grade::getEnrolment, Grade::setEnrolment)
			.addSelector (SELECTOR_PKEY)
			.build ();
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
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof Grade)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.getActivity (), ((Grade) obj).getActivity ());
			ebuilder.append (this.getEnrolment (), ((Grade) obj).getEnrolment ());

			result = ebuilder.isEquals ();
		}

		return result;
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
		final int base = 1049;
		final int mult = 947;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.getActivity ());
		hbuilder.append (this.getEnrolment ());

		return hbuilder.toHashCode ();
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
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("enrolment", this.getEnrolment ());
		builder.append ("activity", this.getActivity ());
		builder.append ("grade", this.getGrade ());

		return builder.toString ();
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

		return new GradeBuilder (datastore)
			.load (this);
	}

	/**
	 * Get the <code>MetaData</code> instance for this <code>Grade</code>
	 * using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<Grade> getMetaData ()
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
	 * Set the <code>Activity</code> which is associated with the
	 * <code>Grade</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Grade</code> instance is loaded.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 */

	protected abstract void setActivity (Activity activity);

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
