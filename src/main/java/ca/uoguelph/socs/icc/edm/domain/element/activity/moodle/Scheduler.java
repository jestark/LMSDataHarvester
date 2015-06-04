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

package ca.uoguelph.socs.icc.edm.domain.element.activity.moodle;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultNamedActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.element.GenericNamedActivity;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.DefinitionBuilder;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/scheduler
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>Activity</code> interface,
 * along with the relevant manager, and builder.  See the <code>Activity</code>
 * interface documentation for details.
 * <p>
 * This class was generated from the <code>NamedActivity</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = scheduler
 * <li>ClassName      = Scheduler
 * <li>Builder        = DefaultNamedActivityBuilder
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class Scheduler extends GenericNamedActivity
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>Scheduler</code> with the factories on
	 * initialization.
	 */

	static
	{
		DefinitionBuilder<Scheduler, Activity.Properties> builder = DefinitionBuilder.newInstance (Activity.class, Scheduler.class, Activity.Properties.class);
		builder.setCreateMethod (Scheduler::new);

		builder.addUniqueAttribute (Activity.Properties.ID, Long.class, false, false, Scheduler::getId, Scheduler::setId);

		builder.addAttribute (Activity.Properties.COURSE, Course.class, true, false, Scheduler::getCourse, Scheduler::setCourse);
		builder.addAttribute (Activity.Properties.TYPE, ActivityType.class, true, false, Scheduler::getType, Scheduler::setType);
		builder.addAttribute (Activity.Properties.NAME, String.class, true, false, Scheduler::getName, Scheduler::setName);

		builder.addRelationship ("grades", Grade.class, Scheduler::addGrade, Scheduler::removeGrade);
		builder.addRelationship ("log", LogEntry.class, Scheduler::addLog, Scheduler::removeLog);

		GenericNamedActivity.registerActivity (builder.build (), DefaultNamedActivityBuilder.class, "moodle", "scheduler");
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	public Scheduler ()
	{
		super ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Activity</code>
	 * instance.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
	public Long getId ()
	{
		return super.getId ();
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code>
	 * instance is loaded, or by the <code>ActivityBuilder</code>
	 * implementation to set the <code>DataStore</code> identifier, prior to
	 * storing a new <code>Activity</code> instance.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final Long id)
	{
		super.setId (id);
	}

	/**
	 * Create the <code>Activity</code> instance.
	 *
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code>, not null
	 * @param  course  The <code>Course</code> which is associated with the
	 *                 <code>Activity</code> instance, not null
	 * @param  name    The name of the <code>Activity</code>, not null
	 */

	public Scheduler (final ActivityType type, final Course course, final String name)
	{
		super (type, course, name);
	}
}