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

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;

import org.jboss.solder.beanManager.BeanManagerAware;

/**
 * A {@link ConstraintValidatorFactory} which enables CDI based dependency injection for the created {@link ConstraintValidator}
 * s. Validator types must be valid CDI beans - in particular they must be defined in a bean deployment archive (BDA) - in order
 * to make use of DI services.
 * 
 * @author Gunnar Morling
 * 
 */
public class InjectingConstraintValidatorFactory extends BeanManagerAware implements ConstraintValidatorFactory {

    /**
     * The default constraint validator factory. The creation of validators which are no compliant CDI bean (not contained in a
     * BDA etc.) will be delegated to this factory.
     */
    private final ConstraintValidatorFactory delegate;

    public InjectingConstraintValidatorFactory() {

        delegate = Validation.byDefaultProvider().configure().getDefaultConstraintValidatorFactory();
    }

    @SuppressWarnings("unchecked")
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {

        T theValue;

        if (!isBeanManagerAvailable()) {
            throw new IllegalStateException(
                    "No bean manager is available. In order to use InjectingConstraintValidatorFactory, the javax.validation.Validator "
                            + "must either be retrieved via dependency injection or a bean manager must be available via JNDI.");
        }

        BeanManager beanManager = getBeanManager();

        Set<Bean<?>> beans = beanManager.getBeans(key);

        // The given type is a CDI bean, so the container will deal with injection etc.
        if (!beans.isEmpty()) {
            Bean<?> bean = beanManager.resolve(beans);
            CreationalContext<?> ctx = beanManager.createCreationalContext(bean);

            theValue = (T) beanManager.getReference(bean, key, ctx);
        }
        // The given type is no CDI bean, so delegate the creation to the default factory
        else {
            theValue = delegate.getInstance(key);
        }

        return theValue;
    }

}
