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

package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Set;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;

/**
 * 
 *
 * @see ca.uoguelph.socs.icc.edm.domain.ActivitySource The ActivitySource interface
 * @author James E. Stark
 * @version 1.0
 */

public interface ActivitySourceManager extends DataStoreManager<ActivitySource>
{
	/**
	 * Retrive the ActivitySource object associated with the specified name
	 * from the underlying datastore.
	 *
	 * @param name The name of the ActivitySource to retrieve
	 * @return The ActivitySource object associated with the specified name.
	 */

	public abstract ActivitySource fetchByName (String name);
}
