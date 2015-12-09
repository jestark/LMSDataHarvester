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

package ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.inject.Scope;

/**
 * Defines the Scope (within Dagger) for the <code>IdGenerator</code> instances.
 * <p>
 * In Dagger, any scoped object will produce one instance per component.
 * Behaviourally, there is no difference between <code>@Singleton</code> and
 * <code>@Generator</code>.  While <code>@Singleton</code> could be used, it
 * would be confusing to anyone who is not aware of Dagger's behaviour, so
 * <code>@Generator</code> exists to provide clarity.
 *
 * @author  James E. Stark
 * @version 1.0
 */

@Scope
@Documented
@Retention (value=RUNTIME)
public @interface GeneratorScope {}
