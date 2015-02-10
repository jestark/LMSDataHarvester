/* Copyright (C) 2015 James E. Stark
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

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

import ca.uoguelph.socs.icc.edm.domain.core.ActivityGroupMember;

/**
 * Factory interface to create new <code>LogReference</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a sub-class of <code>LogReference</code> which is
 * used to refer a <code>LogEntry</code> to a Sub-Activity.  Since
 * <code>LogReference</code> and it's sub-classes do not have
 * <code>DataStore</code> ID's, the <code>setId</code> method will throw a
 * <code>UnsupportedOperationException</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     LogEntryElementFactory
 * @see     ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder
 * @see     java.lang.UnsupportedOperationException
 */

public interface LogReferenceElementFactory extends ElementFactory<LogEntry>
{
	/**
	 * Create a new <code>LogReference</code> instance.
	 *
	 * @param  entry    The <code>LogEntry</code> which refers to the
	 *                  sub-activity, not null
	 * @param  activity The Sub-Activity (<code>ActivityGroupMember</code>) which
	 *                  is being referenced, not null
	 *
	 * @return          The new <code>LogReference</code> (as a
	 *                  <code>LogEntry</code>)
	 */

	public abstract LogEntry create (LogEntry entry, ActivityGroupMember activity);
}
