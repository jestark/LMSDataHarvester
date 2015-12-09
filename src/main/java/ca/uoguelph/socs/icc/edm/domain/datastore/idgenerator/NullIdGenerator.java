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

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import com.google.auto.service.AutoService;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * An <code>IdGenerator</code> which always returns a null reference.  This
 * <code>IdGenerator</code> is intended for situations where the application
 * logic requires that an ID number is assigned, but the actual ID number will
 * be determined though other means (such as being automatically assigned by an
 * underlying database).
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class NullIdGenerator implements IdGenerator
{
	/**
	 * Dagger Component to create <code>SequentialIdGenerator</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@GeneratorScope
	@Component (modules = {IdGeneratorModule.class})
	public static interface IdGeneratorComponent extends IdGenerator.IdGeneratorComponent
	{
		/**
		 * Create the <code>IdGenerator</code>.
		 *
		 * @return The <code>IdGenerator</code>
		 */

		@Override
		public abstract IdGenerator createIdGenerator ();
	}

	/**
	 * Dagger Module to create the <code>NullIdGenerator</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module
	protected static final class IdGeneratorModule
	{
		/**
		 * Create the <code>RandomIdGenerator</code>.
		 *
		 * @return     The <code>RandomIdGenerator</code>
		 */

		@Provides
		@GeneratorScope
		public IdGenerator createIdGenerator ()
		{
			return new NullIdGenerator ();
		}
	}

	/**
	 * Representation of the <code>NullIdGenerator</code> used by the
	 * <code>ServiceLoader</code> to load it into the JVM.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@AutoService (IdGenerator.Definition.class)
	public static class Definition implements IdGenerator.Definition
	{
		/**
		 * Get the <code>IdGenerator</code> implementation represented by this
		 * <code>Definition</code>.
		 *
		 * @return  The <code>IdGenerator</code> implementation class
		 */

		@Override
		public Class<? extends IdGenerator> getIdGeneratorClass ()
		{
			return NullIdGenerator.class;
		}

		/**
		 * Create an <code>IdGeneratorComponent</code> for the specified
		 * <code>DomainModel</code>, and <code>Element</code> class.
		 *
		 * @param  model   The <code>DomainModel</code>, not null
		 * @param  element The <code>Element</code> class, not null
		 * @return         The <code>IdGeneratorComponent</code>
		 */

		@Override
		public IdGeneratorComponent createComponent (
				final DomainModel model,
				final Class<? extends Element> element)
		{
			return NullIdGenerator.createComponent ();
		}
	}

	/**
	 * Create an <code>IdGeneratorComponent</code> for the specified
	 * <code>DomainModel</code>.  This method produces a Dagger Component which
	 * will create and initialize a <code>NullIdGenerator</code>.
	 *
	 * @return         The <code>IdGeneratorComponent</code>
	 */

	public static IdGeneratorComponent createComponent ()
	{
		return DaggerNullIdGenerator_IdGeneratorComponent.create ();
	}

	/**
	 * Create the <code>NullIdGenerator</code>.
	 */

	private NullIdGenerator () {}

	/**
	 * Return the next available ID number.  This method will always return
	 * null.
	 *
	 * @return A <code>Long</code> containing the next id number
	 */

	@Override
	public Long nextId ()
	{
		return null;
	}
}
