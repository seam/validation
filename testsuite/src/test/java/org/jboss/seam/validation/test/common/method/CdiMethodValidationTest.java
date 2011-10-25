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
package org.jboss.seam.validation.test.common.method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ResourceBundle;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.validation.ValidationInterceptor;
import org.jboss.seam.validation.test.common.method.domain.Movie;
import org.jboss.seam.validation.test.common.method.service.MovieRepository;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CdiMethodValidationTest {

    private static final String VALIDATION_MESSAGES_BUNDLE = "org.hibernate.validator.ValidationMessages";

    private static final String NOT_NULL_KEY = "javax.validation.constraints.NotNull.message";

    private final String notNullMessage = ResourceBundle.getBundle(VALIDATION_MESSAGES_BUNDLE).getString(NOT_NULL_KEY);

    @Inject
    private MovieRepository movieRepository;

    @Deployment
    public static JavaArchive createTestArchive() throws Exception {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addAsManifestResource("META-INF/beans.xml", ArchivePaths.create("beans.xml"))
                .addAsManifestResource(new File("../impl/src/main/resources/META-INF/services/javax.enterprise.inject.spi.Extension"))
                .addPackage(MovieRepository.class.getPackage()).addPackage(ValidationInterceptor.class.getPackage())
                .addPackage(Movie.class.getPackage());
    }

    @Test
    public void validMethodCall() {

        Set<Movie> moviesByBryanSinger = movieRepository.findMoviesByDirector("Bryan Singer");

        assertEquals(1, moviesByBryanSinger.size());
        assertEquals("The Usual Suspects", moviesByBryanSinger.iterator().next().getTitle());
    }

    @Test
    public void methodCallFailsDueToIllegalParameter() {

        try {
            movieRepository.findMoviesByDirector(null);
            fail("Expected " + MethodConstraintViolationException.class.getSimpleName() + " wasn't thrown.");
        } catch (MethodConstraintViolationException e) {
            Set<MethodConstraintViolation<?>> violations = e.getConstraintViolations();
            assertEquals(1, violations.size());
            MethodConstraintViolation<?> constraintViolation = violations.iterator().next();
            assertEquals(notNullMessage, constraintViolation.getMessage());
        }
    }

    @Test
    public void methodCallFailsDueToIllegalReturnValue() {

        try {
            movieRepository.findMoviesByDirector("John Hillcoat");
            fail("Expected " + MethodConstraintViolationException.class.getSimpleName() + " wasn't thrown.");
        } catch (MethodConstraintViolationException e) {
            Set<MethodConstraintViolation<?>> violations = e.getConstraintViolations();
            assertEquals(1, violations.size());
            MethodConstraintViolation<?> constraintViolation = violations.iterator().next();
            assertEquals(notNullMessage, constraintViolation.getMessage());
            assertEquals("MovieRepository#findMoviesByDirector()[].releaseDate", constraintViolation.getPropertyPath()
                    .toString());
        }
    }

}
