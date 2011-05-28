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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.Validator;

import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.hibernate.validator.method.MethodValidator;

/**
 * An interceptor which performs a validation of the Bean Validation constraints specified at the parameters and/or return
 * values of intercepted methods using the method validation functionality provided by Hibernate Validator.
 * 
 * 
 * @author Gunnar Morling
 */
@AutoValidating
@Interceptor
public class ValidationInterceptor implements Serializable {

    private static final long serialVersionUID = 604440259030722151L;

    /**
     * The validator to be used for method validation.
     * 
     * Although the concrete validator is not necessarily serializable (and HV's implementation indeed isn't) it is still
     * alright to have it as non-transient field here. Upon passivation not the validator itself will be serialized, but the
     * proxy injected here, which in turn is serializable.
     */
    @Inject
    private Validator validator;

    /**
     * Validates the Bean Validation constraints specified at the parameters and/or return value of the intercepted method.
     * 
     * @param ctx The context of the intercepted method invocation.
     * @return The result of the method invocation.
     * @throws Exception Any exception caused by the intercepted method invocation. A {@link MethodConstraintViolationException}
     *         in case at least one constraint violation occurred either during parameter or return value validation.
     */
    @AroundInvoke
    public Object validateMethodInvocation(InvocationContext ctx) throws Exception {

        Set<MethodConstraintViolation<Object>> violations = validator.unwrap(MethodValidator.class).validateAllParameters(
                ctx.getTarget(), ctx.getMethod(), ctx.getParameters());

        if (!violations.isEmpty()) {
            throw new MethodConstraintViolationException(getMessage(ctx.getMethod(), ctx.getParameters(), violations),
                    violations);
        }

        Object result = ctx.proceed();

        violations = validator.unwrap(MethodValidator.class).validateReturnValue(ctx.getTarget(), ctx.getMethod(), result);

        if (!violations.isEmpty()) {
            throw new MethodConstraintViolationException(getMessage(ctx.getMethod(), ctx.getParameters(), violations),
                    violations);
        }

        return result;
    }

    private String getMessage(Method method, Object[] args, Set<? extends MethodConstraintViolation<?>> violations) {

        StringBuilder message = new StringBuilder();
        message.append(violations.size());
        message.append(" constraint violation(s) occurred during method invocation.");
        message.append("\nMethod: ");
        message.append(method);
        message.append("\nArgument values: ");
        message.append(Arrays.toString(args));
        message.append("\nConstraint violations: ");

        int i = 1;
        for (MethodConstraintViolation<?> oneConstraintViolation : violations) {
            message.append("\n  (");
            message.append(i);
            message.append(") Kind: ");
            message.append(oneConstraintViolation.getKind());
            message.append("\n      parameter index: ");
            message.append(oneConstraintViolation.getParameterIndex());
            message.append("\n      message: ");
            message.append(oneConstraintViolation.getMessage());
            message.append("\n      root bean: ");
            message.append(oneConstraintViolation.getRootBean());
            message.append("\n      property path: ");
            message.append(oneConstraintViolation.getPropertyPath());
            message.append("\n      constraint: ");
            message.append(oneConstraintViolation.getConstraintDescriptor().getAnnotation());

            i++;
        }

        return message.toString();
    }

}