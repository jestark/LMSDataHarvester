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

package ca.uoguelph.socs.icc.edm.domain.builder;

import java.util.Date;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

/**
 * Factory interface to create new <code>LogEntry</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class implementing the <code>LogEntry</code>
 * domain model interface.  It also provides the functionality required to set
 * the <code>DataStore</code> ID for the <code>LogEntry</code> instance.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     LogReferenceElementFactory
 * @see     ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder
 */

public interface LogEntryElementFactory extends ElementFactory<LogEntry>
{
	/**
	 * Create a new <code>LogEntry</code> instance.  Defaults to the current
	 * system time, if the specified time is null.
	 *
	 * @param  action    The <code>Action</code>, not null
	 * @param  activity  The <code>Activity</code> upon which the
	 *                   <code>Action</code> was performed, not null
	 * @param  enrolment The <code>Enrolment</code> which performed the
	 *                   <code>Action</code>, not null
	 * @param  ip        The remote IP Address
	 * @param  time      The time that the <code>Action</code> was performed
	 *
	 * @return           The new <code>LogEntry</code> instance
	 */

	public abstract LogEntry create (Action action, Activity activity, Enrolment enrolment, String ip, Date time);

	/**
	 * Add the reference to the Sub-Activity to the <code>LogEntry</code>.  Some
	 * <code>LogEntry</code> instances will record an <code>Action</code> upon a
	 * sub-activity, rather than the <code>Activity</code> itself.  This method
	 * sets the reference to the sub-activity via a <code>LogReference</code>
	 * instance.
	 *
	 * @param  entry     The <code>LogEntry</code> to which the
	 *                   <code>LogReference</code> is to be added, not null
	 * @param  reference The <code>LogReference</code> instance to be added to the
	 *                   <code>LogEntry</code>, not null
	 */

	public abstract void setReference (LogEntry entry, LogReference reference);
}
