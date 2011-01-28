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
package org.jboss.seam.validation.method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ResourceBundle;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.validator.MethodConstraintViolation;
import org.hibernate.validator.MethodConstraintViolationException;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.validation.ValidationInterceptor;
import org.jboss.seam.validation.method.domain.Movie;
import org.jboss.seam.validation.method.service.MovieRepository;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class CdiMethodValidationTest {

	private static String notNullMessage;

	@Inject
	private MovieRepository movieRepository;

	@Deployment
	public static JavaArchive createTestArchive() throws Exception {
		return ShrinkWrap
			.create(JavaArchive.class, "test.jar")
			.addManifestResource("META-INF/beans.xml", ArchivePaths.create("beans.xml"))
			.addManifestResource(new File("src/main/resources/META-INF/services/javax.enterprise.inject.spi.Extension"))
			.addPackage(MovieRepository.class.getPackage())
			.addPackage(ValidationInterceptor.class.getPackage())
			.addPackage(Movie.class.getPackage());
	}

	@BeforeClass
	public static void setUpClass() {
		ResourceBundle bundle = ResourceBundle.getBundle("org.hibernate.validator.ValidationMessages");
		notNullMessage = bundle.getString("javax.validation.constraints.NotNull.message");
	}
	
	@Test
	public void validMethodCall() {

		Set<Movie> moviesByBryanSinger = movieRepository
			.findMoviesByDirector("Bryan Singer");

		assertEquals(1, moviesByBryanSinger.size());
		assertEquals(
			"The Usual Suspects",
			moviesByBryanSinger.iterator().next().getTitle());
	}

	@Test
	public void methodCallFailsDueToIllegalParameter() {

		try {
			movieRepository.findMoviesByDirector(null);
			fail("Expected "
				+ MethodConstraintViolationException.class.getSimpleName()
				+ " wasn't thrown.");
		}
		catch (MethodConstraintViolationException e) {
			Set<MethodConstraintViolation<?>> violations = e
				.getConstraintViolations();
			assertEquals(1, violations.size());
			MethodConstraintViolation<?> constraintViolation = violations
				.iterator().next();
			assertEquals(notNullMessage, constraintViolation.getMessage());
		}
	}
	
	@Test
	public void methodCallFailsDueToIllegalReturnValue() {

		try {
			movieRepository.findMoviesByDirector( "John Hillcoat" );
			fail(
					"Expected "
							+ MethodConstraintViolationException.class.getSimpleName()
							+ " wasn't thrown."
			);
		}
		catch ( MethodConstraintViolationException e ) {
			Set<MethodConstraintViolation<?>> violations = e
					.getConstraintViolations();
			assertEquals( 1, violations.size() );
			MethodConstraintViolation<?> constraintViolation = violations
					.iterator().next();
			assertEquals( notNullMessage, constraintViolation.getMessage() );
			assertEquals(
					"MovieRepository#findMoviesByDirector()[].releaseDate",
					constraintViolation.getPropertyPath().toString()
			);
		}
	}
	
}
