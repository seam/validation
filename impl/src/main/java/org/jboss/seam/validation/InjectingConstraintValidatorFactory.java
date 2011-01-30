/**
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
