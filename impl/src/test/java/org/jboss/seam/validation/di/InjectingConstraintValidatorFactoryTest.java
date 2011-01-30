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
public class InjectingConstraintValidatorFactoryTest
{

   @Inject
   private Validator validator;

   @Deployment
   public static JavaArchive createTestArchive() throws Exception
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar").addManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml")).addManifestResource(new File("src/main/resources/META-INF/services/javax.enterprise.inject.spi.Extension")).addPackage(InjectingConstraintValidatorFactoryTest.class.getPackage()).addPackage(HelloWorldService.class.getPackage()).addPackage(ValidHello.class.getPackage());
   }

   @Test
   public void testConstraintValidatorWithInjectedDependency()
   {

      Set<ConstraintViolation<Model>> violations = validator.validate(new Model());
      assertEquals(1, violations.size());
   }

}
