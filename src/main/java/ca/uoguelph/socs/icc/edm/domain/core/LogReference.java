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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.io.Serializable;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

public abstract class LogReference<T extends ActivityGroupMember> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private LogEntry entry;
	private T activity;

	protected LogReference ()
	{
		this.entry = null;
		this.activity = null;
	}

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj != null)
		{
			if (obj == this)
			{
				result = true;
			}
			else if (obj.getClass () == this.getClass ())
			{
				result = this.entry.equals (obj);
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		return this.entry.hashCode ();
	}

	public LogEntry getEntry ()
	{
		return this.entry;
	}

	protected void setEntry (LogEntry entry)
	{
		this.entry = entry;
	}

	public Activity getActivity ()
	{
		return this.activity;
	}

	protected void setActivity (T activity)
	{
		this.activity = activity;
	}
}
