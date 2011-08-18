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

import java.util.Collection;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBannedValidator implements ConstraintValidator<NotBanned, String> {

    /*
     * Note that the Seam Validation constraint factory (org.jboss.seam.validation.InjectingConstraintValidatorFactory) 
     * must be enabled in META-INF/validation.xml for the dependency injection to work in constraint validators.
     */
    @Inject
    @BannedNames
    private Collection<String> bannedNames;
    
    private Pattern pattern;
    
    @Override
    public void initialize(NotBanned annotation) {
        // We build a pattern matching all the banned names
        StringBuilder regex = new StringBuilder();
        for (String name : bannedNames) {
            if (regex.length() > 0) {
                regex.append('|');
            }
            regex.append(name);
        }
        pattern = Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {        
        return !pattern.matcher(name).find();
    }
}
