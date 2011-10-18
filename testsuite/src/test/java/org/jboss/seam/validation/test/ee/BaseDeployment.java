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
package org.jboss.seam.validation.test.ee;

import java.io.File;
import java.util.Collection;

import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependency;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolutionFilter;

/**
 * 
 * @author Marek Schmidt
 *
 */
public class BaseDeployment {
    
    private static final MavenResolutionFilter validationArtifactsFilter = new MavenResolutionFilter() {
        @Override
        public boolean accept(MavenDependency element) {
            if (element.getCoordinates().startsWith("org.jboss.seam.validation")) {
                return false;
            }
            return true;
        }

        @Override
        public MavenResolutionFilter configure(Collection<MavenDependency> dependencies) {
            return this;
        }};
    
    public static WebArchive createBaseDeployment() {
        return ShrinkWrap
            .create(WebArchive.class, "test.war")
                
                .addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class)
                        .loadMetadataFromPom("pom.xml")
                        .artifact("org.jboss.seam.validation:seam-validation")
                        .resolveAs(GenericArchive.class, validationArtifactsFilter))
            
                .addAsLibraries(
                        ShrinkWrap.create(ZipImporter.class, "seam-validation.jar")
                            .importFrom(new File("../impl/target/seam-validation.jar"))
                            .as(JavaArchive.class),
                        ShrinkWrap.create(ZipImporter.class, "seam-validation-api.jar")
                            .importFrom(new File("../api/target/seam-validation-api.jar"))
                            .as(JavaArchive.class));
    }
}
