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
package org.jboss.seam.validation.di;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.validation.InjectingConstraintValidatorFactory;
import org.jboss.seam.validation.di.constraint.ValidHello;
import org.jboss.seam.validation.di.service.HelloWorldService;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for {@link InjectingConstraintValidatorFactory}.
 * 
 * @author Gunnar Morling
 * 
 */
@RunWith(Arquillian.class)
public class InjectingConstraintValidatorFactoryTest {

    @Inject
    private Validator validator;
    
	@Deployment
	public static JavaArchive createTestArchive() throws Exception {
		return ShrinkWrap
			.create(JavaArchive.class, "test.jar")
			.addManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
			.addManifestResource(new File("src/main/resources/META-INF/services/javax.enterprise.inject.spi.Extension"))
			.addPackage(InjectingConstraintValidatorFactoryTest.class.getPackage())
			.addPackage(HelloWorldService.class.getPackage())
			.addPackage(ValidHello.class.getPackage());
	}
	
	@Test
	public void testConstraintValidatorWithInjectedDependency() {
		
    	Set<ConstraintViolation<Model>> violations = validator.validate(new Model());
    	assertEquals(1, violations.size());
	}
	
}
