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

import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the participation of a particular <code>User</code> in a
 * <code>Course</code>.  The purpose of the <code>Enrolment</code> interface,
 * and its implementations, is to separate data about the participation in a
 * <code>Course</code> by a particular <code>User</code> from the data that
 * identifies the <code>User</code>.
 * <p>
 * Implementations of the <code>Enrolment</code> interface act as anonymous
 * place holders for the <code>User</code> interface, within the remainder of
 * the domain model.  If an implementation of the <code>Enrolment</code>
 * interface does not contain a link to an implementation of the
 * <code>User</code> interface then it must be impossible to identify the user
 * based on the data provided by the <code>Enrolment</code>.  For any given
 * <code>Enrolment</code> the associated <code>User</code> data may, or may
 * not, be present, so implementations must not rely on the presence of the
 * <code>User</code>.
 * <p>
 * The <code>Enrolment</code> interface (and its implementations) has a strong
 * dependency on the <code>Course</code> and <code>Role</code> interfaces, and
 * a weak dependency on the <code>User</code> interface.  Associated instances
 * of the <code>Course</code> and <code>Role</code> interfaces are required for
 * an instance of the <code>Enrolment</code> interface to exist.  If one of
 * these required instances is deleted, then the <code>Enrolment</code>
 * instance must be deleted as well.  An instance of the <code>User</code>
 * interface should be present for the initial creation of the
 * <code>Enrolment</code> instance.  However, if it should be possible, if
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
 * With the exception of adding and removing instances of the
 * <code>Grade</code> and <code>LogEntry</code> interfaces, instances of the
 * <code>Enrolment</code> interface, once created, are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     EnrolmentBuilder
 * @see     EnrolmentLoader
 */

public abstract class Enrolment extends Element
{
	/** The <code>MetaData</code> definition for the <code>Enrolment</code> */
	protected static final Definition<Enrolment> metadata;

	/** The associated <code>Course</code> */
	public static final Property<Course> COURSE;

	/** The final grade */
	public static final Property<Integer> FINALGRADE;

	/** The associated <code>Role</code> */
	public static final Property<Role> ROLE;

	/** Has consent been given to use this data for research */
	public static final Property<Boolean> USABLE;

	/** Select the <code>Enrolment</code> instance by its id */
	public static final Selector<Enrolment> SELECTOR_ID;

	/** Select all of the <code>Enrolment</code> instances */
	public static final Selector<Enrolment> SELECTOR_ALL;

	/** Select all <code>Enrolment</code> by <code>Role</code>*/
	public static final Selector<Enrolment> SELECTOR_ROLE;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Enrolment</code>.
	 */

	static
	{
		COURSE = Property.getInstance (Enrolment.class, Course.class, "course", false, true);
		FINALGRADE = Property.getInstance (Enrolment.class, Integer.class, "finalgrade", true, false);
		ROLE = Property.getInstance (Enrolment.class, Role.class, "role", false, true);
		USABLE = Property.getInstance (Enrolment.class, Boolean.class, "usable", true, true);

		SELECTOR_ID = Selector.getInstance (Enrolment.class, ID, true);
		SELECTOR_ALL = Selector.getInstance (Enrolment.class, "all", false);
		SELECTOR_ROLE = Selector.getInstance (Enrolment.class, ROLE, false);

		metadata = Definition.getBuilder (Enrolment.class, Element.metadata)
			.addRelationship (COURSE, Enrolment::getCourse, Enrolment::setCourse)
			.addRelationship (ROLE, Enrolment::getRole, Enrolment::setRole)
			.addProperty (FINALGRADE, Enrolment::getFinalGrade, Enrolment::setFinalGrade)
			.addProperty (USABLE, Enrolment::isUsable, Enrolment::setUsable)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_ROLE)
			.build ();

		Profile.registerMetaData (metadata);
	}

	/**
	 * Get the name associated with the <code>Enrolment</code>.  The contents
	 * of the <code>String</code> returned by this method are implementation
	 * dependent.  If the implementation has access to the <code>User</code>
	 * information this this method should return the result of the
	 * <code>getName</code> method for the associated <code>User</code>
	 * instance, otherwise it should return some other identifier, usually the
	 * identifier for the <code>Enrolment</code> in the <code>DataStore</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Enrolment</code>
	 * @see    User#getName
	 */

	public abstract String getName ();

	/**
	 * Get the <code>Course</code> in which the <code>User</code> represented
	 * by the <code>Enrolment</code> instance is enrolled.
	 *
	 * @return The <code>Course</code> instance
	 */

	public abstract Course getCourse();

	/**
	 * Set the <code>Course</code> in which the <code>User</code> is enrolled.
	 * This method is intended to be used by a <code>DataStore</code> when the
	 * <code>Enrolment</code> instance is loaded.
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
	 * <code>Course</code>. This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
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
	 * <code>Course</code>. This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
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

	public abstract Integer getFinalGrade ();

	/**
	 * Set the final grade for the <code>User</code> in the
	 * <code>Course</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
	 *
	 * @param  finalgrade The final grade for the <code>User</code> in the
	 *                    course, on the interval [0, 100]
	 */

	protected abstract void setFinalGrade (Integer finalgrade);

	/**
	 * Get the <code>Grade</code> for the specified <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> for which the grade is to be
	 *                  retrieved
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
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Enrolment</code> instance is loaded.
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
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Enrolment</code> instance is loaded.
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
