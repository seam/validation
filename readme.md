# What is it?

This is the incubator for a [Seam 3](http://seamframework.org/Seam3) module dedicated to the integration of [Hibernate Validator](http://validator.hibernate.org/) and CDI ("Contexts and Dependency Injection for the Java<sup>TM</sup>
EE platform", defined by [JSR 299](http://jcp.org/en/jsr/detail?id=299)).

This module provides a CDI portable extension, which offers the following services:

* Dependency injection of `javax.validation.ValidatorFactory` and `javax.validation.Validator` instances in Non-Java-EE environments:

		public class MyBean {

			@Inject
			private Validator validator;
		
			public void foo(Bar bar) {
				Set<ConstraintViolation<Bar>> violations = validator.validate(bar);
				//...
			}
		}
	
* Dependency injection in `javax.validation.ConstraintValidator` instances:

		public class MyConstraintValidator implements ConstraintValidator<MyConstraint, String> {

			@Inject
			private FooService foo;
	
			@Override
			public void initialize(MyConstraint constraintAnnotation) {	}

			@Override
			public boolean isValid(String value, ConstraintValidatorContext context) {
		
				if(value == null) {
					return true;
				}
		
				return foo.isValid(value);
			}

		}

* Integration with the method validation feature of Hibernate Validator. Annotate any CDI bean with `@AutoValidating` to trigger automatic validation of invocations of it's methods:

		@AutoValidating
		public class FooService {
	
			public void bar(@NotNull @Size(min=3) String baz) {
				//...
			}
	
		}

If `FooService#bar()` is invoked with null or a String shorter than three characters as value for the `baz` parameter this call will be intercepted and a `MethodConstraintViolationException`will be thrown.