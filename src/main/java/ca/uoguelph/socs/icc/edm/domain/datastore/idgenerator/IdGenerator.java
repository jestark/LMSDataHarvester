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

package ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * An ID number generator.  Implementations of this interface provide ID numbers
 * suitable for use with a <code>DataStore</code>.  Each class implementing this
 * interface is responsible for determining how the ID numbers are calculated,
 * with different classes providing different distributions of ID numbers.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface IdGenerator
{
	/**
	 * Abstract interface for Dagger Components which create
	 * <code>IdGenerator</code> instances.  This interface allows other
	 * components to declare a dependency on the <code>IdGenerator</code>
	 * without committing to an implementation.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@GeneratorScope
	public static interface IdGeneratorComponent
	{
		/**
		 * Create the <code>IdGenerator</code>.
		 *
		 * @return The <code>IdGenerator</code>
		 */

		public abstract IdGenerator createIdGenerator ();
	}

	/**
	 * Representation of an <code>IdGenerator</code> used by the
	 * <code>ServiceLoader</code> to load the <code>IdGenerator</code>
	 * implementations into the JVM.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static interface Definition
	{
		/**
		 * Get the <code>IdGenerator</code> implementation represented by this
		 * <code>Definition</code>.
		 *
		 * @return  The <code>IdGenerator</code> implementation class
		 */

		public abstract Class<? extends IdGenerator> getIdGeneratorClass ();

		/**
		 * Create an <code>IdGeneratorComponent</code> for the specified
		 * <code>DomainModel</code>, and <code>Element</code> class.
		 *
		 * @param  model   The <code>DomainModel</code>, not null
		 * @param  element The <code>Element</code> class, not null
		 * @return         The <code>IdGeneratorComponent</code>
		 */

		public abstract IdGeneratorComponent createComponent (
				final DomainModel model,
				final Class<? extends Element> element);
	}

	/**
	 * Return the next available ID number.
	 *
	 * @return A <code>Long</code> containing the ID number
	 */

	public abstract Long nextId ();
}
