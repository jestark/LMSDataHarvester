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

/**
 * Root level interface for all of the elements of the domain model.  Since
 * there is no common behaviors shared by all of the elements in the domain
 * model this interface it empty.  It exists to allow the managers and the 
 * <code>DataStore</code> to handle all of the elements of the domain model
 * generically.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ElementManager
 * @see     ca.uoguelph.socs.icc.edm.domain.datastore.DataStore
 */

public interface Element
{
}
