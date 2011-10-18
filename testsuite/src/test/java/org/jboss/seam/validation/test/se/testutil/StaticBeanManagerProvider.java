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
package org.jboss.seam.validation.test.se.testutil;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.solder.beanManager.BeanManagerProvider;

/**
 * A {@link BeanManagerProvider} implementation which provides access to an externally set {@link BeanManager}. This is useful
 * for testing scenarios, where the bean manager is not available via dependency injection but must be retrieved from JNDI.
 * 
 * @author Gunnar Morling
 */
public class StaticBeanManagerProvider implements BeanManagerProvider {

    private static BeanManager beanManager;

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public BeanManager getBeanManager() {
        return beanManager;
    }

    /**
     * Sets the bean manager to be returned by this provider.
     * 
     * @param beanManager The bean manager to be returned by this provider.
     */
    public static void setBeanManager(BeanManager beanManager) {
        StaticBeanManagerProvider.beanManager = beanManager;
    }

}
