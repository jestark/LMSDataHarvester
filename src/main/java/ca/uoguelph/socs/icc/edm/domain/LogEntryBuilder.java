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

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface LogEntryBuilder extends ElementBuilder<LogEntry>
{
	/**
	 * Get the <code>Action</code> which was performed upon the logged
	 * activity.
	 *
	 * @return A reference to the logged <code>Action</code>
	 */

	public abstract Action getAction ();

	/**
	 * Set the <code>Action</code> which was performed upon the logged
	 * <code>Activity</code>.
	 *
	 * @param  action                   The <code>Action</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Action</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public abstract void setAction (Action action);

	/**
	 * Get the <code>Activity</code> upon which the logged action was
	 * performed.
	 *
	 * @return A reference to the associated <code>Activity</code> instance
	 */

	public abstract Activity getActivity ();

	/**
	 * Set the <code>Activity</code> upon which the logged action was
	 * performed.
	 *
	 * @param  activity                 The <code>Activity</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Activity</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public abstract void setActivity (Activity activity);

	/**
	 * Get the <code>Enrolment</code> instance for the user which performed the
	 * logged action.
	 *
	 * @return A reference to the associated <code>Enrolment</code>
	 */

	public abstract Enrolment getEnrolment ();

	/**
	 * Set the <code>Enrolment</code> instance for the <code>User</code> which
	 * performed the logged action.
	 *
	 * @param  enrolment                The <code>Enrolment</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Enrolment</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public abstract void setEnrolment (Enrolment enrolment);

	/**
	 * Get the time of the logged action.
	 *
	 * @return A <code>Date</code> object containing the logged time
	 */

	public abstract Date getTime ();

	/**
	 * Set the time of the logged <code>Action</code>.
	 *
	 * @param  time The time
	 */

	public abstract void setTime (Date time);

	/**
	 * Get the Internet Protocol address which is associated with the logged
	 * action.
	 *
	 * @return A <code>String</code> containing the IP address, may be null
	 */

	public abstract String getIPAddress ();

	/**
	 * Set the Internet Protocol Address which is associated with the logged
	 * <code>Action</code>.
	 *
	 * @param  ipaddress A <code>String</code> containing the IP Address
	 */

	public abstract void setIPAddress (String ipaddress);
}
