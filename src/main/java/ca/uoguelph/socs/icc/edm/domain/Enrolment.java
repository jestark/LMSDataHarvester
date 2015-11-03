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
 * A representation of the participation of a particular <code>User</code> in a
 * <code>Course</code>.  The purpose of the <code>Enrolment</code> interface,
 * and its implementations, is to separate data about the participation in a
 * <code>Course</code> by a particular <code>User</code> from the data that
 * identifies the <code>User</code>.  As such, the <code>Enrolment</code>
 * interface act as an anonymous place holders for the <code>User</code>
 * interface, within the remainder of the domain model.
 * <p>
 * The <code>Enrolment</code> interface (and its implementations) has a strong
 * dependency on the <code>Course</code> and <code>Role</code> interfaces, and
 * a weak dependency on the <code>User</code> interface.  Associated instances
 * of the <code>Course</code> and <code>Role</code> interfaces are required for
 * an instance of the <code>Enrolment</code> interface to exist.  If one of
 * these required instances is deleted, then the <code>Enrolment</code>
 * instance must be deleted as well.  An instance of the <code>User</code>
 * interface should be present for the initial creation of the
 * <code>Enrolment</code> instance.  However, it should be possible, if
 * difficult, to create an <code>Enrolment</code> instance without an
 * associated <code>User</code> instance.  Deletion of the associated
 * <code>User</code> instance should not impact the <code>Enrolment</code>
 * instance.
 * <p>
 * Instances of the <code>Grade</code> and <code>LogEntry</code> interfaces
 * have a strong dependency on the <code>Enrolment</code> interface.  If an
 * instance of the <code>Enrolment</code> interface is deleted, then the
 * associated instances of the <code>Grade</code> and <code>LogEntry</code>
 * interfaces must be deleted as well.
 * <p>
 * A single <code>Enrolment</code> instance exists to represent the
 * participation of a single <code>User</code> in a single <code>Course</code>.
 * Normally the <code>User</code> and <code>Course</code> instances would be
 * used to uniquely identify the <code>Enrolment</code>.  Since the
 * <code>Enrolment</code> instance does not contain a link to the associated
 * <code>User</code> instance (which may not exist in the
 * <code>DomainModel</code>), <code>Enrolment</code> uses its
 * <code>DomainModel</code> ID as a stand in for the <code>User</code> during
 * comparisons.  As a result, two otherwise identical <code>Enrolment</code>
 * instances from different data stores will probably compare as different.
 * <p>
 * Once created an <code>Enrolment</code> instance is immutable except for the
 * <code>finalGrade</code> and <code>usable</code> properties.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     EnrolmentBuilder
 * @see     EnrolmentLoader
 */

public abstract class Enrolment extends Element
{
	/** The <code>MetaData</code> for the <code>Enrolment</code> */
	protected static final MetaData<Enrolment> METADATA;

	/** The associated <code>Course</code> */
	public static final Property<Course> COURSE;

	/** The final grade */
	public static final Property<Integer> FINALGRADE;

	/** The associated <code>Role</code> */
	public static final Property<Role> ROLE;

	/** Has consent been given to use this data for research */
	public static final Property<Boolean> USABLE;

	/** The <code>Grade</code> instances assigned to the <code>Enrolment</code> */
	public static final Property<Grade> GRADES;

	/** The <code>LogEntry</code> instances associated with the <code>Enrolment</code> */
	public static final Property<LogEntry> LOGENTRIES;

	/** Select all <code>Enrolment</code> by <code>Role</code>*/
	public static final Selector SELECTOR_ROLE;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Enrolment</code>.
	 */

	static
	{
		COURSE = Property.getInstance (Course.class, "course", Property.Flags.REQUIRED);
		FINALGRADE = Property.getInstance (Integer.class, "finalgrade", Property.Flags.MUTABLE);
		ROLE = Property.getInstance (Role.class, "role", Property.Flags.REQUIRED);
		USABLE = Property.getInstance (Boolean.class, "usable", Property.Flags.REQUIRED, Property.Flags.MUTABLE);

		GRADES = Property.getInstance (Grade.class, "grades", Property.Flags.MULTIVALUED);
		LOGENTRIES = Property.getInstance (LogEntry.class, "logentries", Property.Flags.MULTIVALUED);

		SELECTOR_ROLE = Selector.getInstance (ROLE, false);

		METADATA = MetaData.builder (Element.METADATA)
			.addProperty (FINALGRADE, Enrolment::getFinalGrade, Enrolment::setFinalGrade)
			.addProperty (USABLE, Enrolment::isUsable, Enrolment::setUsable)
			.addRelationship (COURSE, Enrolment::getCourse, Enrolment::setCourse)
			.addRelationship (ROLE, Enrolment::getRole, Enrolment::setRole)
			.addRelationship (GRADES, Enrolment::getGrades, Enrolment::addGrade, Enrolment::removeGrade)
			.addRelationship (LOGENTRIES, Enrolment::getLog, Enrolment::addLog, Enrolment::removeLog)
			.addRelationship (User.class, User.ENROLMENTS, User.SELECTOR_ENROLMENTS)
			.addSelector (SELECTOR_ROLE)
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

	protected static <T extends Enrolment> void registerImplementation (final Class<T> impl, final Supplier<T> supplier)
	{

	}

	/**
	 * Get an instance of the <code>EnrolmentBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>EnrolmentBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Enrolment</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static EnrolmentBuilder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>Enrolment</code>.
	 */

	protected Enrolment ()
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
			.add ("usable", this.isUsable ())
			.add ("finalgrade", this.getFinalGrade ())
			.add ("course", this.getCourse ())
			.add ("role", this.getRole ());
	}

	/**
	 * Compare two <code>Enrolment</code> instances to determine if they are
	 * equal.  The <code>Enrolment</code> instances are compared based upon the
	 * <code>Course</code>, <code>Role</code> and <code>DataStore</code> id.
	 *
	 * @param  obj The <code>Enrolment</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Enrolment</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Enrolment)
			&& Objects.equals (this.getId (), ((Enrolment) obj).getId ())
			&& Objects.equals (this.getCourse (), ((Enrolment) obj).getCourse ())
			&& Objects.equals (this.getRole (), ((Enrolment) obj).getRole ());
	}

	/**
	 * Compare two <code>Enrolment</code> instances to determine if they are
	 * equal using all of the instance fields.  For <code>Enrolment</code> the
	 * <code>equals</code> methods excludes the mutable fields from the
	 * comparison.  This methods compares two <code>Enrolment</code> instances
	 * using all of the fields.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 *
	 * @return         <code>True</code> if the two <code>Enrolment</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equalsAll (final @Nullable Element element)
	{
		return (element == this) ? true : (element instanceof Enrolment)
			&& Objects.equals (this.getCourse (), ((Enrolment) element).getCourse ())
			&& Objects.equals (this.getRole (), ((Enrolment) element).getRole ())
			&& Objects.equals (this.getFinalGrade (), ((Enrolment) element).getFinalGrade ())
			&& Objects.equals (this.isUsable (), ((Enrolment) element).isUsable ());
	}

	/**
	 * Compare two <code>Enrolment</code> instances to determine if they are
	 * equal using the minimum set fields required to identify the
	 * <code>Element</code> instance.  The <code>equals</code> method for
	 * <code>Enrolment</code> includes the <code>DataStore</code> id in the
	 * comparison.  This methods compares two <code>Enrolment</code> instances
	 * without using the <code>DataStore</code> id.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 *
	 * @return         <code>True</code> if the two <code>Enrolment</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equalsUnique (final @Nullable Element element)
	{
		return (element == this) ? true : (element instanceof Enrolment)
			&& Objects.equals (this.getCourse (), ((Enrolment) element).getCourse ())
			&& Objects.equals (this.getRole (), ((Enrolment) element).getRole ());
	}


	/**
	 * Compute a <code>hashCode</code> of the <code>Enrolment</code> instance.
	 * The hash code is computed based upon the associated <code>Course</code>
	 * and the associated <code>Role</code>.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getId (), this.getCourse (), this.getRole ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>Enrolment</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>Enrolment</code> instance
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
		return Enrolment.METADATA.getProperties ();
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
		return Enrolment.METADATA.getSelectors ();
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
		return Enrolment.METADATA.hasValue (property, this, value);
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
		return Enrolment.METADATA.getStream (property, this);
	}

	/**
	 * Get an <code>EnrolmentBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>EnrolmentBuilder</code> on the specified <code>DomainModel</code>
	 * and initializes it with the contents of this <code>Enrolment</code>
	 * instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>EnrolmentBuilder</code>
	 */

	@Override
	public EnrolmentBuilder getBuilder (final DomainModel model)
	{
		return Enrolment.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the <code>Course</code> in which the <code>User</code> represented
	 * by the <code>Enrolment</code> instance is enrolled.
	 *
	 * @return The <code>Course</code> instance
	 */

	public abstract Course getCourse();

	/**
	 * Set the <code>Course</code> in which the <code>User</code> is enrolled.
	 * This method is intended to be used to initialize a new
	 * <code>Enrolment</code> instance.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	protected abstract void setCourse (final Course course);

	/**
	 * Get the <code>Role</code> of the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.
	 *
	 * @return The <code>Role</code> instance
	 */

	public abstract Role getRole();

	/**
	 * Set the <code>Role</code> of the <code>User</code> in the
	 * <code>Course</code>. This method is intended to be used to initialize a
	 * new <code>Enrolment</code> instance.
	 *
	 * @param  role The <code>Role</code>, not null
	 */

	protected abstract void setRole (Role role);

	/**
	 * Determine if the <code>User</code> has given their consent for the data
	 * associated with this <code>Enrolment</code> to be used for research.
	 *
	 * @return <code>True</code> if the <code>User</code> has consented,
	 *         <code>False</code> otherwise.
	 */

	public abstract Boolean isUsable ();

	/**
	 * Set the usable flag for the data related to the <code>User</code> in the
	 * <code>Course</code>. This method is intended to be used to initialize a
	 * new <code>Enrolment</code> instance.
	 *
	 * @param  usable Indication if the data may be used for research, not null
	 */

	protected abstract void setUsable (Boolean usable);

	/**
	 * Get the final grade for the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.  The
	 * final grade will be null if no final grade was assigned for the
	 * <code>User</code> associated with this <code>Enrolment</code>.
	 *
	 * @return An <code>Integer</code> containing the final grade, or null if
	 *         there is no final grade
	 */

	@CheckReturnValue
	public abstract Integer getFinalGrade ();

	/**
	 * Set the final grade for the <code>User</code> in the
	 * <code>Course</code>.  This method is intended to be used to initialize a
	 * new <code>Enrolment</code> instance.
	 *
	 * @param  finalgrade The final grade for the <code>User</code> in the
	 *                    course, on the interval [0, 100]
	 */

	protected abstract void setFinalGrade (@Nullable Integer finalgrade);

	/**
	 * Get the <code>Grade</code> for the specified <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> for which the grade is to be
	 *                  retrieved
	 *
	 * @return          The <code>Grade</code> for the specified
	 *                  <code>Activity</code>
	 */

	public abstract Grade getGrade (Activity activity);

	/**
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Enrolment</code> instance.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances.
	 */

	public abstract Set<Grade> getGrades ();

	/**
	 * Initialize the <code>Set</code> of <code>Grade</code> instances
	 * associated with the <code>Enrolment</code> instance.  This method is
	 * intended to be used to initialize a new <code>Enrolment</code> instance.
	 *
	 * @param  grades The <code>Set</code> of <code>Grade</code> instances, not
	 *                null
	 */

	protected abstract void setGrades (Set<Grade> grades);

	/**
	 * Add the specified <code>Grade</code> to the <code>Enrolment</code>.
	 *
	 * @param  grade  The <code>Grade</code> to add, not null
	 *
	 * @return        <code>True</code> if the <code>Grade</code> was
	 *                successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addGrade (Grade grade);

	/**
	 * Remove the specified <code>Grade</code> from the <code>Enrolment</code>.
	 *
	 * @param  grade The <code>Grade</code> to remove, not null
	 *
	 * @return       <code>True</code> if the <code>Grade</code> was
	 *               successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeGrade (Grade grade);

	/**
	 * Get the <code>List</code> of <code>LogEntry</code> instances associated
	 * with this <code>Enrolment</code>.  The <code>List</code> will be empty
	 * if there are no associated <code>LogEntry</code> instances.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	public abstract List<LogEntry> getLog ();

	/**
	 * Initialize the <code>List</code> of <code>LogEntry</code> instances
	 * associated with the <code>Enrolment</code> instance.  This method is
	 * intended to be used to initialize a new <code>Enrolment</code> instance.
	 *
	 * @param  log The <code>List</code> of <code>LogEntry</code> instances,
	 *             not null
	 */

	protected abstract void setLog (List<LogEntry> log);

	/**
	 * Add the specified <code>LogEntry</code> to the <code>Enrolment</code>.
	 *
	 * @param  entry The <code>LogEntry</code> to add, not null
	 *
	 * @return       <code>True</code> if the <code>LogEntry</code> was
	 *               successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addLog (LogEntry entry);

	/**
	 * Remove the specified <code>LogEntry</code> from the
	 * <code>Enrolment</code>.
	 *
	 * @param  entry The <code>LogEntry</code> to remove, not null
	 *
	 * @return       <code>True</code> if the <code>LogEntry</code> was
	 *               successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeLog (LogEntry entry);
}
