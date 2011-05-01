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

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.resource.ResourceCollection;

public class JettyRunner {

    public static void main(String[] args) throws Exception {
        Server server = new Server();

        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);
        connector.setHost("127.0.0.1");
        server.addConnector(connector);

        WebAppContext wac = new WebAppContext();
        wac.setContextPath("/validation-helloworld");
        wac.setBaseResource(new ResourceCollection(new String[] { "./src/main/webapp", "./src/main/webapp-jetty" }));

        wac.setConfigurationClasses(new String[] {

        "org.mortbay.jetty.webapp.WebInfConfiguration", "org.mortbay.jetty.plus.webapp.EnvConfiguration",
                "org.mortbay.jetty.plus.webapp.Configuration", "org.mortbay.jetty.webapp.JettyWebXmlConfiguration" });

        server.setHandler(wac);
        server.setStopAtShutdown(true);
        server.start();
        System.out
                .println("Started Seam Validation Example Application on http://localhost:8080/validation-helloworld/HelloWorld");

        server.join();
    }
}
