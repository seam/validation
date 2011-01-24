/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

final class ValidatorFactoryBean implements Bean<ValidatorFactory> {
	
	@Override
	public Class<?> getBeanClass() {

		return ValidatorFactory.class;
	}

	@Override
	public Set<InjectionPoint> getInjectionPoints() {

		return Collections.emptySet();
	}

	@Override
	public String getName() {

		return "validatorFactory";

	}

	@SuppressWarnings("serial")
	@Override
	public Set<Annotation> getQualifiers() {

		Set<Annotation> qualifiers = new HashSet<Annotation>();

		qualifiers.add(new AnnotationLiteral<Default>() {});
		qualifiers.add(new AnnotationLiteral<Any>() {});

		return qualifiers;
	}

	@Override
	public Class<? extends Annotation> getScope() {

		return ApplicationScoped.class;
	}

	@Override
	public Set<Class<? extends Annotation>> getStereotypes() {

		return Collections.emptySet();
	}

	@Override
	public Set<Type> getTypes() {

		Set<Type> types = new HashSet<Type>();

		types.add(ValidatorFactory.class);
		types.add(Object.class);

		return types;
	}

	@Override
	public boolean isAlternative() {

		return false;
	}

	@Override
	public boolean isNullable() {

		return false;
	}

	@Override
	public ValidatorFactory create(
			CreationalContext<ValidatorFactory> ctx) {

		return Validation.buildDefaultValidatorFactory();
	}

	@Override
	public void destroy(ValidatorFactory instance, CreationalContext<ValidatorFactory> ctx) {

		ctx.release();
	}
}