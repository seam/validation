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
package org.jboss.seam.validation.test.common.di.constraint;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.jboss.seam.validation.test.common.di.service.HelloWorldService;

/**
 * Validator for the {@link ValidHello} constraint. This validator is not portable between BV implementations, as it relies on
 * dependency injection.
 * 
 * @author Gunnar Morling
 * 
 */
public class ValidHelloValidator implements ConstraintValidator<ValidHello, String> {

    private String name;

    @Inject
    private HelloWorldService service;

    @Override
    public void initialize(ValidHello constraintAnnotation) {
        this.name = constraintAnnotation.value();

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        return value.equals(service.sayHello(this.name));
    }

}
