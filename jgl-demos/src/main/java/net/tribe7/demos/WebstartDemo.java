package net.tribe7.demos;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
public @interface WebstartDemo {}
