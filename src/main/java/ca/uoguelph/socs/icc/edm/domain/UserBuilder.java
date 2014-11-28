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

public interface UserBuilder extends DomainModelBuilder<User>
{
	public abstract Integer getIdNumber ();
	public abstract UserBuilder setIdNumber (Integer idnumber);
	public abstract String getFirstname ();
	public abstract UserBuilder setFirstname (String firstname);
	public abstract String getLastname ();
	public abstract UserBuilder setLastname (String lastname);
	public abstract String getUsername ();
	public abstract UserBuilder setUsername (String username);
}
