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

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

/**
 * A {@link ConstraintValidatorFactory} which enables dependency injection for
 * the created {@link ConstraintValidator}s.
 * 
 * @author Gunnar Morling
 * 
 */
public class InjectingConstraintValidatorFactory implements ConstraintValidatorFactory
{

   @Inject
   private BeanManager beanManager;

   @Override
   public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key)
   {

      AnnotatedType<T> type = beanManager.createAnnotatedType(key);
      InjectionTarget<T> it = beanManager.createInjectionTarget(type);
      CreationalContext<T> ctx = beanManager.createCreationalContext(null);

      T instance = it.produce(ctx);
      it.inject(instance, ctx);
      it.postConstruct(instance);

      return instance;
   }

}
