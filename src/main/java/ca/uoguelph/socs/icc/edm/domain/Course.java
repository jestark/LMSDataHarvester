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

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of a course within the domain model.  Instances of the
 * <code>Course</code> interface contain the identifying information for a
 * particular offering of a course, including its name and semester (and year)
 * of offering.  The instances of the <code>Course</code> interface act as a
 * container for all of the data, via the associated instances of the
 * <code>Activity</code> and <code>Enrolment</code> interfaces, concerning a
 * particular offering of a course.
 * <p>
 * Within the domain model the <code>Course</code> interface is a root level
 * element, as such instances of the <code>Course</code> interface are not
 * dependant upon any other domain model element to exist.  An associated
 * instance of the <code>Course</code> interface is required for an instance of
 * the <code>Enrolment</code>, or <code>Activity</code> interfaces to exist.
 * If a particular instance of the <code>Course</code> interface is deleted,
 * then all of the associated instances of the <code>Activity</code> and
 * <code>Enrolment</code> interfaces must be deleted as well.
 * <p>
 * Once created, <code>Course</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     CourseBuilder
 * @see     CourseLoader
 */

public abstract class Course extends Element
{
	/** The name of the <code>Course</code> */
	public static final Property<String> NAME;

	/** The <code>Semester</code> of offering for the <code>Course</code> */
	public static final Property<Semester> SEMESTER;

	/** The year of offering for the <code>Course</code> */
	public static final Property<Integer> YEAR;

	/** The <code>Activity</code> instances associated with the <code>Course</code> */
	public static final Property<ActivityReference> ACTIVITIES;

	/** The <code>Enrolment</code> instances associated with the <code>Course</code> */
	public static final Property<Enrolment> ENROLMENTS;

	/** Select an <code>Course</code> instance by its name and date of offering */
	public static final Selector SELECTOR_OFFERING;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Course</code>.
	 */

	static
	{
		NAME = Property.getInstance (String.class, "name", Property.Flags.REQUIRED);
		SEMESTER = Property.getInstance (Semester.class, "semester", Property.Flags.REQUIRED);
		YEAR = Property.getInstance (Integer.class, "year", Property.Flags.REQUIRED);

		ACTIVITIES = Property.getInstance (ActivityReference.class, "activities", Property.Flags.MULTIVALUED);
		ENROLMENTS = Property.getInstance (Enrolment.class, "enrolments", Property.Flags.MULTIVALUED);

		SELECTOR_OFFERING = Selector.getInstance ("offering", true, NAME, SEMESTER, YEAR);

		Definition.getBuilder (Course.class, Element.class)
			.addProperty (NAME, Course::getName, Course::setName)
			.addProperty (SEMESTER, Course::getSemester, Course::setSemester)
			.addProperty (YEAR, Course::getYear, Course::setYear)
			.addRelationship (ACTIVITIES, Course::getActivityReferences, Course::addActivityReference, Course::removeActivityReference)
			.addRelationship (ENROLMENTS, Course::getEnrolments, Course::addEnrolment, Course::removeEnrolment)
			.addSelector (SELECTOR_OFFERING)
			.build ();
	}

	/**
	 * Get an instance of the <code>CourseBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>CourseBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Course</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is
	 *                               immutable
	 */

	public static CourseBuilder builder (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return new CourseBuilder (datastore);
	}

	/**
	 * Get an instance of the <code>CourseBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>CourseBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Course</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static CourseBuilder builder (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return Course.builder (model.getDataStore ());
	}

	/**
	 * Compare two <code>Course</code> instances to determine if they are
	 * equal.  The <code>Course</code> instances are compared based upon their
	 * names, as well as their year and <code>Semester</code> of offering.
	 *
	 * @param  obj The <code>Course</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Course</code> instances
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
		else if (obj instanceof Course)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.getName (), ((Course) obj).getName ());
			ebuilder.append (this.getYear (), ((Course) obj).getYear ());
			ebuilder.append (this.getSemester (), ((Course) obj).getSemester ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Course</code> instance.
	 * The hash code is computed based upon the name of the instance as well as
	 * the year and <code>Semester</code> of offering.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1061;
		final int mult = 937;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.getName ());
		hbuilder.append (this.getYear ());
		hbuilder.append (this.getSemester ());

		return hbuilder.toHashCode ();
	}

	/**
	 * Get a <code>String</code> representation of the <code>Course</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Course</code>
	 *         instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("name", this.getName ());
		builder.append ("semester", this.getSemester ());
		builder.append ("year", this.getYear ());

		return builder.toString ();
	}

	/**
	 * Get an <code>CourseBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates an
	 * <code>CourseBuilder</code> on the specified <code>DataStore</code> and
	 * initializes it with the contents of this <code>Course</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>CourseBuilder</code>
	 */

	@Override
	public CourseBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return Course.builder (datastore)
			.load (this);
	}

	/**
	 * Get the <code>MetaData</code> instance for this <code>Course</code>
	 * using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<Course> metadata ()
	{
		return this.getDomainModel ()
			.getDataStore ()
			.getProfile ()
			.getCreator (Course.class, this.getClass ());
	}

	/**
	 * Get the name of the <code>Course</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Course</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Course</code>.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Course</code> instance
	 * is loaded.
	 *
	 * @param  name The name of the <code>Course</code>
	 */

	protected abstract void setName (String name);

	/**
	 * Get the <code>Semester</code> in which the <code>Course</code> was
	 * offered.
	 *
	 * @return The <code>Semester</code> of offering
	 */

	public abstract Semester getSemester ();

	/**
	 * Set the <code>Semester</code> in which the <code>Course</code> was
	 * offered.  This method is intended to be used by a <code>DataStore</code>
	 * when the <code>Course</code> instance is loaded.
	 *
	 * @param  semester The <code>Semester</code> in which the
	 *                  <code>Course</code> was offered
	 */

	protected abstract void setSemester (Semester semester);

	/**
	 * Get the year in which the <code>Course</code> was offered.
	 *
	 * @return An <code>Integer</code> containing the year of offering
	 */

	public abstract Integer getYear ();

	/**
	 * Set the year in which the <code>Course</code> was offered.  This method
	 * is intended to be used by a <code>DataStore</code> when the
	 * <code>Course</code> instance is loaded.
	 *
	 * @param  year The year in which the <code>Course</code> was offered
	 */

	protected abstract void setYear (Integer year);

	/**
	 * Get the <code>List</code> of <code>Activity</code> instances which are
	 * associated with the <code>Course</code>.  The <code>List</code> will be
	 * empty if there are no <code>Activity</code> instances associated with
	 * the <code>Course</code>.
	 *
	 * @return A <code>List</code> of <code>Activity</code> instances
	 */

	public abstract List<Activity> getActivities ();

	/**
	 * Get the <code>List</code> <code>ActivityReference</code> instances which
	 * are associated with the <code>Course</code>.
	 *
	 * @return A <code>List</code> of <code>ActivityReference</code> instances
	 */

	protected abstract List<ActivityReference> getActivityReferences ();

	/**
	 * Initialize the <code>List</code> of <code>Activity</code> instances
	 * associated with the <code>Course</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Course</code> instance is loaded.
	 *
	 * @param  activities The <code>List</code> of <code>Activity</code>
	 *                    instances, not null
	 */

	protected abstract void setActivityReferences (List<ActivityReference> activities);

	/**
	 * Add the specified <code>ActivityReference</code> to the
	 * <code>Course</code>.
	 *
	 * @param  activity The <code>Activityreference</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>ActivityReference</code>
	 *                  was successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addActivityReference (ActivityReference activity);

	/**
	 * Remove the specified <code>ActivityReference</code> from the
	 * <code>Course</code>.
	 *
	 * @param  activity The <code>ActivityReference</code> to remove,  not null
	 *
	 * @return          <code>True</code> if the <code>ActivityReference</code>
	 *                  was successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeActivityReference (ActivityReference activity);

	/**
	 * Get the <code>List</code> of <code>Enrolment</code> instances which are
	 * associated with the <code>Course</code>.  The <code>List</code> will be
	 * empty if no one is enrolled in the <code>Course</code>.
	 *
	 * @return A <code>List</code> of <code>Enrolment</code> instances
	 */

	public abstract Set<Enrolment> getEnrolments ();

	/**
	 * Initialize the <code>List</code> of <code>Enrolment</code> instances
	 * associated with the <code>Course</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Course</code> instance is loaded.
	 *
	 * @param  enrolments The <code>List</code> of <code>Enrolment</code>
	 *                    instances, not null
	 */

	protected abstract void setEnrolments (Set<Enrolment> enrolments);

	/**
	 * Add the specified <code>Enrolment</code> to the <code>Course</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to add, not null
	 *
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addEnrolment (Enrolment enrolment);

	/**
	 * Remove the specified <code>Enrolment</code> from the
	 * <code>Course</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to remove, not null
	 *
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeEnrolment (Enrolment enrolment);
}

