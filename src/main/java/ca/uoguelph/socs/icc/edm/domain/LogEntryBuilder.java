/* Copyright (C) 2014 James E. Stark
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

import java.util.Date;

public interface LogEntryBuilder extends ElementBuilder<LogEntry>
{
	public abstract Action getAction ();
	public abstract LogEntryBuilder setAction (Action action);
	public abstract Activity getActivity ();
	public abstract LogEntryBuilder setActivity (Activity activity);
	public abstract Enrolment getEnrolment ();
	public abstract LogEntryBuilder setEnrolment (Enrolment enrolment);
	public abstract Date getTime ();
	public abstract LogEntryBuilder setTime (Date time);
	public abstract String getIPAddress ();
	public abstract LogEntryBuilder setIPAddress (String ipaddress);
}
