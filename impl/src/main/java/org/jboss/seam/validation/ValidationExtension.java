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

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.method.MethodValidator;

/**
 * A CDI portable extension which registers beans for {@link ValidatorFactory} and {@link Validator}, if such beans not yet
 * exist (which for instance would be the case in a Java EE 6 container). Furthermore a {@link MethodValidator} bean is
 * registered. All registered beans will be {@link ApplicationScoped}.
 * 
 * @author Gunnar Morling
 * 
 */
public class ValidationExtension implements Extension {

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {

        addValidatorFactoryIfRequired(abd, bm);
        addValidatorIfRequired(abd, bm);
        addMethodValidator(abd, bm);
    }

    private void addValidatorFactoryIfRequired(AfterBeanDiscovery abd, final BeanManager beanManager) {

        // if a ValidatorFactory already exists, only inject it's ConstraintValidatorFactory if required
        if (!beanManager.getBeans(ValidatorFactory.class).isEmpty()) {

            ValidatorFactory validatorFactory = getReference(beanManager, ValidatorFactory.class);

            ConstraintValidatorFactory constraintValidatorFactory = validatorFactory.getConstraintValidatorFactory();
            if (constraintValidatorFactory instanceof InjectingConstraintValidatorFactory) {
                inject(beanManager, InjectingConstraintValidatorFactory.class,
                        (InjectingConstraintValidatorFactory) constraintValidatorFactory);
            }

            return;
        }

        abd.addBean(new Bean<ValidatorFactory>() {

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

                qualifiers.add(new AnnotationLiteral<Default>() {
                });
                qualifiers.add(new AnnotationLiteral<Any>() {
                });

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
            public ValidatorFactory create(CreationalContext<ValidatorFactory> ctx) {

                ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

                ConstraintValidatorFactory constraintValidatorFactory = validatorFactory.getConstraintValidatorFactory();
                if (constraintValidatorFactory instanceof InjectingConstraintValidatorFactory) {
                    inject(beanManager, InjectingConstraintValidatorFactory.class,
                            (InjectingConstraintValidatorFactory) constraintValidatorFactory);
                }

                return validatorFactory;
            }

            @Override
            public void destroy(ValidatorFactory instance, CreationalContext<ValidatorFactory> ctx) {

            }
        });
    }

    private void addValidatorIfRequired(AfterBeanDiscovery abd, final BeanManager bm) {

        // do nothing, if Validator already exists
        if (!bm.getBeans(Validator.class).isEmpty()) {
            return;
        }

        abd.addBean(new Bean<Validator>() {

            @Override
            public Class<?> getBeanClass() {

                return Validator.class;
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {

                return Collections.emptySet();
            }

            @Override
            public String getName() {

                return "validator";
            }

            @SuppressWarnings("serial")
            @Override
            public Set<Annotation> getQualifiers() {

                Set<Annotation> qualifiers = new HashSet<Annotation>();

                qualifiers.add(new AnnotationLiteral<Default>() {
                });
                qualifiers.add(new AnnotationLiteral<Any>() {
                });

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

                types.add(MethodValidator.class);
                types.add(Validator.class);
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
            public Validator create(CreationalContext<Validator> ctx) {

                ValidatorFactory validatorFactory = getReference(bm, ValidatorFactory.class);

                return validatorFactory.getValidator();
            }

            @Override
            public void destroy(Validator instance, CreationalContext<Validator> ctx) {

            }
        });
    }

    private void addMethodValidator(AfterBeanDiscovery abd, final BeanManager bm) {

        abd.addBean(new Bean<MethodValidator>() {

            @Override
            public Class<?> getBeanClass() {

                return MethodValidator.class;
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {

                return Collections.emptySet();
            }

            @Override
            public String getName() {

                return "methodValidator";
            }

            @SuppressWarnings("serial")
            @Override
            public Set<Annotation> getQualifiers() {

                Set<Annotation> qualifiers = new HashSet<Annotation>();

                qualifiers.add(new AnnotationLiteral<Default>() {
                });
                qualifiers.add(new AnnotationLiteral<Any>() {
                });

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

                types.add(MethodValidator.class);
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
            public MethodValidator create(CreationalContext<MethodValidator> ctx) {

                return getReference(bm, ValidatorFactory.class).getValidator().unwrap(MethodValidator.class);
            }

            @Override
            public void destroy(MethodValidator instance, CreationalContext<MethodValidator> ctx) {

            }
        });
    }

    @SuppressWarnings("unchecked")
    private <T> T getReference(BeanManager bm, Class<T> clazz) {

        Bean<T> bean = (Bean<T>) bm.getBeans(clazz).iterator().next();
        CreationalContext<T> context = bm.createCreationalContext(bean);

        return (T) bm.getReference(bean, clazz, context);
    }

    private <T> void inject(final BeanManager beanManager, Class<T> type, T constraintValidatorFactory) {

        AnnotatedType<T> annotatedType = beanManager.createAnnotatedType(type);
        InjectionTarget<T> it = beanManager.createInjectionTarget(annotatedType);
        CreationalContext<T> cvfCtx = beanManager.createCreationalContext(null);

        it.inject(constraintValidatorFactory, cvfCtx);
        it.postConstruct(constraintValidatorFactory);
    }

}
