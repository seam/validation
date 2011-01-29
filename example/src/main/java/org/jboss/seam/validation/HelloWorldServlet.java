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

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet
{

   private static final long serialVersionUID = -8659615639253408826L;

   @Inject
   private HelloWorldService service;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {

      response.setContentType("text/html");
      response.setStatus(HttpServletResponse.SC_OK);

      String name = request.getParameter("name");
      if (name != null)
      {
         response.getWriter().println("<h1>" + service.sayHello(name) + "</h1>");
      }
      else
      {
         response.getWriter().println("<h1>Hi, what's your name?</h1>");
         response.getWriter().println("<form action=\"HelloWorld\"");
         response.getWriter().println("Name: <input name=\"name\" type=\"text\" size=\"30\">");
         response.getWriter().println("<input type=\"submit\" value=\" OK \">");
         response.getWriter().println("</form>");
      }
   }
}