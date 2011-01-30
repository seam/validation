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
package org.jboss.seam.validation;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.resource.ResourceCollection;

public class JettyRunner
{

   public static void main(String[] args) throws Exception
   {
      Server server = new Server();

      Connector connector = new SelectChannelConnector();
      connector.setPort(8080);
      connector.setHost("127.0.0.1");
      server.addConnector(connector);

      WebAppContext wac = new WebAppContext();
      wac.setContextPath("/seam-validation");
      wac.setBaseResource(new ResourceCollection(new String[] { "./src/main/webapp-jetty" }));

      wac.setConfigurationClasses(new String[] {

      "org.mortbay.jetty.webapp.WebInfConfiguration", "org.mortbay.jetty.plus.webapp.EnvConfiguration", "org.mortbay.jetty.plus.webapp.Configuration", "org.mortbay.jetty.webapp.JettyWebXmlConfiguration" });

      server.setHandler(wac);
      server.setStopAtShutdown(true);
      server.start();
      System.out.println("Started Seam Validation Example Application on http://localhost:8080/seam-validation/HelloWorld");

      server.join();
   }
}
