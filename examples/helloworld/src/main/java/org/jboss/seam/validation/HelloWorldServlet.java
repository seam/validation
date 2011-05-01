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

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet {

    private static final long serialVersionUID = -8659615639253408826L;

    @Inject
    private HelloWorldService service;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        String name = request.getParameter("name");
        if (name != null) {
            response.getWriter().println("<h1>" + service.sayHello(name) + "</h1>");
        } else {
            response.getWriter().println("<?xml version=\"1.0\" ?>");
            response.getWriter()
                    .println(
                            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
            response.getWriter().println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
            response.getWriter().println("   <head><title>Seam Validation Module Example</title></head>");
            response.getWriter().println("   <body>");
            response.getWriter().println("      <h1>Seam Validation Module Example - Method Validation</h1>");
            response.getWriter().println("      <p>Hi, what's your name? Enter at least three characters.</p>");
            response.getWriter().println("      <form action=\"HelloWorld\">");
            response.getWriter().println("         Name: <input name=\"name\" type=\"text\" size=\"30\">");
            response.getWriter().println("         <input type=\"submit\" value=\" OK \">");
            response.getWriter().println("      </form>");
            response.getWriter().println("   </body>");
            response.getWriter().println("</html>");
        }
    }
}
