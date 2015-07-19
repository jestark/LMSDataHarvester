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

package ca.uoguelph.socs.icc.edm.domain.metadata;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Represents a function which injects a <code>MetaData</code> instance
 * into the target entity.  Implementations of this interface  will receive
 * a <code>MetaData</code> instance and the type parameter for the
 * <code>Element</code> implementation represented by the
 * </code>MetaData</code> instance.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> interface type
 * @param   <R> The result type of the <code>Receiver</code>
 */

public interface Receiver<T extends Element, R>
{
	/**
	 * Apply the <code>MetaData</code> instance to the given argument.
	 *
	 * @param <U>      The <code>Element</code> implementation type
	 * @param metadata The <code>MetaData</code> instance, not null
	 * @param type     The <code>Element</code> implementation class, not null
	 *
	 * @return         The return value of the receiving method
	 */

	public abstract <U extends T> R apply (MetaData<T> metadata, Class<U> type);
}
