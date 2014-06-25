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

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@ApplicationScoped
@AutoValidating
public class HelloWorldService {

    public HelloWorldService() {
    }
    
    @Size(max = 16)
    public String composeName(@NotNull @NotEmpty String firstName, @NotNull String middleName, @NotNull String lastName) {
        StringBuffer name = new StringBuffer(firstName);
        
        if (middleName.length() > 0) {
            name.append(' ');
            name.append(middleName);           
        }
        
        if (lastName.length() > 0) {
            name.append(' ');
            name.append(lastName);
        }
        
        return name.toString();
    }

    public String sayHello(@NotNull @Size(min = 3) @NotBanned String name) {
        return "Hello, " + name + "!";
    }
}
