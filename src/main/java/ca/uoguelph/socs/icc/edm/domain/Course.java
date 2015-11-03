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
	/** The <code>MetaData</code> for the <code>Course</code> */
	protected static final MetaData<Course> METADATA;

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

		ACTIVITIES = Property.getInstance (ActivityReference.class, "activities", Property.Flags.RECOMMENDED, Property.Flags.MULTIVALUED);
		ENROLMENTS = Property.getInstance (Enrolment.class, "enrolments", Property.Flags.RECOMMENDED, Property.Flags.MULTIVALUED);

		SELECTOR_OFFERING = Selector.getInstance ("offering", true, NAME, SEMESTER, YEAR);

		METADATA = MetaData.builder (Element.METADATA)
			.addProperty (NAME, Course::getName, Course::setName)
			.addProperty (SEMESTER, Course::getSemester, Course::setSemester)
			.addProperty (YEAR, Course::getYear, Course::setYear)
			.addRelationship (ACTIVITIES, Course::getActivityReferences, Course::addActivityReference, Course::removeActivityReference)
			.addRelationship (ENROLMENTS, Course::getEnrolments, Course::addEnrolment, Course::removeEnrolment)
			.addSelector (SELECTOR_OFFERING)
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

	protected static <T extends Course> void registerImplementation (final Class<T> impl, final Supplier<T> supplier)
	{

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
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>Course</code>.
	 */

	protected Course ()
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
			.add ("name", this.getName ())
			.add ("semester", this.getSemester ())
			.add ("year", this.getYear ());
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
		return (obj == this) ? true : (obj instanceof Course)
			&& Objects.equals (this.getName (), ((Course) obj).getName ())
			&& Objects.equals (this.getYear (), ((Course) obj).getYear ())
			&& Objects.equals (this.getSemester (), ((Course) obj).getSemester ());
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
		return Objects.hash (this.getName (), this.getYear (), this.getSemester ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>Course</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Course</code>
	 *         instance
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
	public Set<Property<?>> properties ()
	{
		return Course.METADATA.getProperties ();
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
		return Course.METADATA.getSelectors ();
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
		return Course.METADATA.hasValue (property, this, value);
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
		return Course.METADATA.getStream (property, this);
	}

	/**
	 * Get an <code>CourseBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>CourseBuilder</code> on the specified <code>DomainModel</code> and
	 * initializes it with the contents of this <code>Course</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>CourseBuilder</code>
	 */

	@Override
	public CourseBuilder getBuilder (final DomainModel model)
	{
		return Course.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
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
	 * used to be used to initialize a new <code>Course</code> instance.
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
	 * offered.  This method is intended to be used to be used to initialize a
	 * new <code>Course</code> instance.
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
	 * is intended to be used to be used to initialize a new <code>Course</code>
	 * instance.
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
	 * intended to be used to be used to initialize a new <code>Course</code>
	 * instance.
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
	 * intended to be used to initialize a new <code>Course</code> instance.
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

