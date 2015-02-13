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

/**
 * Root level interface for all of the elements of the domain model.  Since
 * there is no common behaviors shared by all of the elements in the domain
 * model the <code>Element</code> interface does not contain any methods. The
 * purpose of the <code>Element</code> interface is to allow instances of the
 * <code>ElementBuilder</code> and <code>ElementManager</code> interfaces along
 * with their supporting infrastructure to refer to use bounded generic types
 * when referring to the domain model interfaces and their implementations.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ElementBuilder
 * @see     ElementManager
 */

public interface Element
{
}
