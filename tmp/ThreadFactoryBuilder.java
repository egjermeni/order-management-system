package edd.example.java.util;

/*
 * https://github.com/google/guava/blob/master/guava/src/com/google/common/util/concurrent/ThreadFactoryBuilder.java
 *
 * Copyright (C) 2010 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

//package com.google.common.util.concurrent;

//import static com.google.common.base.Preconditions.checkArgument;
//import static com.google.common.base.Preconditions.checkNotNull;

//import com.google.common.annotations.GwtIncompatible;
//import com.google.errorprone.annotations.CanIgnoreReturnValue;
//import com.google.errorprone.annotations.CheckReturnValue;
//import com.sun.istack.internal.Nullable;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A ThreadFactory builder, providing any combination of these features:
 *
 * <ul>
 *   <li>whether threads should be marked as {@linkplain Thread#setDaemon daemon} threads
 *   <li>a {@linkplain ThreadFactoryBuilder#setNameFormat naming format}
 *   <li>a {@linkplain Thread#setPriority thread priority}
 *   <li>an {@linkplain Thread#setUncaughtExceptionHandler uncaught exception handler}
 *   <li>a {@linkplain ThreadFactory#newThread backing thread factory}
 * </ul>
 *
 * <p>If no backing thread factory is provided, a default backing thread factory is used as if by
 * calling {@code setThreadFactory(}{@link Executors#defaultThreadFactory()}{@code )}.
 *
 * @author Kurt Alfred Kluever
 * @since 4.0
 */
//@CanIgnoreReturnValue
//@GwtIncompatible
public final class ThreadFactoryBuilder {
	private String nameFormat = null;
	private Boolean daemon = null;
	private Integer priority = null;
	private UncaughtExceptionHandler uncaughtExceptionHandler = null;
	private ThreadFactory backingThreadFactory = null;

	/** Creates a new {@link ThreadFactory} builder. */
	public ThreadFactoryBuilder() {}

	/**
	 * Sets the naming format to use when naming threads ({@link Thread#setName}) which are created
	 * with this ThreadFactory.
	 *
	 * @param nameFormat a {@link String#format(String, Object...)}-compatible format String, to which
	 *     a unique integer (0, 1, etc.) will be supplied as the single parameter. This integer will
	 *     be unique to the built instance of the ThreadFactory and will be assigned sequentially. For
	 *     example, {@code "rpc-pool-%d"} will generate thread names like {@code "rpc-pool-0"}, {@code
	 *     "rpc-pool-1"}, {@code "rpc-pool-2"}, etc.
	 * @return this for the builder pattern
	 */
	public ThreadFactoryBuilder setNameFormat(String nameFormat) {
		String unused = format(nameFormat, 0); // fail fast if the format is bad or null
		this.nameFormat = nameFormat;
		return this;
	}

	/**
	 * Sets daemon or not for new threads created with this ThreadFactory.
	 *
	 * @param daemon whether or not new Threads created with this ThreadFactory will be daemon threads
	 * @return this for the builder pattern
	 */
	public ThreadFactoryBuilder setDaemon(boolean daemon) {
		this.daemon = daemon;
		return this;
	}

	/**
	 * Sets the priority for new threads created with this ThreadFactory.
	 *
	 * @param priority the priority for new Threads created with this ThreadFactory
	 * @return this for the builder pattern
	 */
	public ThreadFactoryBuilder setPriority(int priority) {
		// Thread#setPriority() already checks for validity. These error messages
		// are nicer though and will fail-fast.
		checkArgument(
				priority >= Thread.MIN_PRIORITY,
				"Thread priority (%s) must be >= %s",
				priority,
				Thread.MIN_PRIORITY);
		checkArgument(
				priority <= Thread.MAX_PRIORITY,
				"Thread priority (%s) must be <= %s",
				priority,
				Thread.MAX_PRIORITY);
		this.priority = priority;
		return this;
	}

	/**
	 * Sets the {@link UncaughtExceptionHandler} for new threads created with this ThreadFactory.
	 *
	 * @param uncaughtExceptionHandler the uncaught exception handler for new Threads created with
	 *     this ThreadFactory
	 * @return this for the builder pattern
	 */
	public ThreadFactoryBuilder setUncaughtExceptionHandler(
			UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = checkNotNull(uncaughtExceptionHandler);
		return this;
	}

	/**
	 * Sets the backing {@link ThreadFactory} for new threads created with this ThreadFactory. Threads
	 * will be created by invoking #newThread(Runnable) on this backing {@link ThreadFactory}.
	 *
	 * @param backingThreadFactory the backing {@link ThreadFactory} which will be delegated to during
	 *     thread creation.
	 * @return this for the builder pattern
	 * see MoreExecutors
	 */
	public ThreadFactoryBuilder setThreadFactory(ThreadFactory backingThreadFactory) {
		this.backingThreadFactory = checkNotNull(backingThreadFactory);
		return this;
	}

	/**
	 * Returns a new thread factory using the options supplied during the building process. After
	 * building, it is still possible to change the options used to build the ThreadFactory and/or
	 * build again. State is not shared amongst built instances.
	 *
	 * @return the fully constructed {@link ThreadFactory}
	 */
	//@CheckReturnValue
	public ThreadFactory build() {
		return doBuild(this);
	}

	// Split out so that the anonymous ThreadFactory can't contain a reference back to the builder.
	// At least, I assume that's why. TODO(cpovirk): Check, and maybe add a test for this.
	private static ThreadFactory doBuild(ThreadFactoryBuilder builder) {
		final String nameFormat = builder.nameFormat;
		final Boolean daemon = builder.daemon;
		final Integer priority = builder.priority;
		final UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
		final ThreadFactory backingThreadFactory =
				(builder.backingThreadFactory != null)
						? builder.backingThreadFactory
						: Executors.defaultThreadFactory();
		final AtomicLong count = (nameFormat != null) ? new AtomicLong(0) : null;
		return new ThreadFactory() {
			@Override
			public Thread newThread(Runnable runnable) {
				Thread thread = backingThreadFactory.newThread(runnable);
				if (nameFormat != null) {
					thread.setName(format(nameFormat, count.getAndIncrement()));
				}
				if (daemon != null) {
					thread.setDaemon(daemon);
				}
				if (priority != null) {
					thread.setPriority(priority);
				}
				if (uncaughtExceptionHandler != null) {
					thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
				}
				return thread;
			}
		};
	}

	private static String format(String format, Object... args) {
		return String.format(Locale.ROOT, format, args);
	}

	/**
	 * https://github.com/google/guava/blob/master/guava/src/com/google/common/base/Preconditions.java
	 *
	 * Ensures the truth of an expression involving one or more parameters to the calling method.
	 *
	 * <p>See {link #checkArgument(boolean, String, Object...)} for details.
	 *
	 * @since 20.0 (varargs overload since 2.0)
	 */
	public static void checkArgument(
			boolean b, /**@Nullable*/ String errorMessageTemplate, int p1, int p2) {
		if (!b) {
			throw new IllegalArgumentException(lenientFormat(errorMessageTemplate, p1, p2));
		}
	}


	/**
	 * https://github.com/google/guava/blob/master/guava/src/com/google/common/base/Preconditions.java
	 *
	 * Ensures the truth of an expression involving one or more parameters to the calling method.
	 *
	 * <p>See {link #checkArgument(boolean, String, Object...)} for details.
	 *
	 * @since 20.0 (varargs overload since 2.0)
	 */
	public static void checkArgument(
			boolean b, /**@Nullable*/ String errorMessageTemplate, int p1, char p2) {
		if (!b) {
			throw new IllegalArgumentException(lenientFormat(errorMessageTemplate, p1, p2));
		}
	}

	/**
	 *
	 * https://github.com/google/guava/blob/master/guava/src/com/google/common/base/Strings.java
	 *
	 * Returns the given {@code template} string with each occurrence of {@code "%s"} replaced with
	 * the corresponding argument value from {@code args}; or, if the placeholder and argument counts
	 * do not match, returns a best-effort form of that string. Will not throw an exception under any
	 * circumstances (as long as all arguments' {@code toString} methods successfully return).
	 *
	 * <p><b>Note:</b> For most string-formatting needs, use {@link String#format}, {link
	 * PrintWriter#format}, and related methods. These support the full range of {linkplain
	 * Formatter#syntax format specifiers}, and alert you to usage errors by throwing {link
	 * InvalidFormatException}.
	 *
	 * <p>In certain cases, such as outputting debugging information or constructing a message to be
	 * used for another unchecked exception, an exception during string formatting would serve little
	 * purpose except to supplant the real information you were trying to provide. These are the cases
	 * this method is made for; it instead generates a best-effort string with all supplied argument
	 * values present. This method is also useful in environments such as GWT where {@code
	 * String.format} is not available. As an example, method implementations of the {link
	 * Preconditions} class use this formatter, for both of the reasons just discussed.
	 *
	 * <p><b>Warning:</b> Only the exact two-character placeholder sequence {@code "%s"} is
	 * recognized.
	 *
	 * @param template a string containing zero or more {@code "%s"} placeholder sequences. {@code
	 *     null} is treated as the four-character string {@code "null"}.
	 * @param args the arguments to be substituted into the message template. The first argument
	 *     specified is substituted for the first occurrence of {@code "%s"} in the template, and so
	 *     forth. A {@code null} argument is converted to the four-character string {@code "null"};
	 *     non-null values are converted to strings using {@link Object#toString()}.
	 * @since NEXT
	 */
	// TODO(diamondm) consider using Arrays.toString() for array parameters
	// TODO(diamondm) capture exceptions thrown from arguments' toString methods
	public static String lenientFormat(
			/**@Nullable*/ String template, /**@Nullable*/ Object... args) {
		template = String.valueOf(template); // null -> "null"

		args = args == null ? new Object[] {"(Object[])null"} : args;

		// start substituting the arguments into the '%s' placeholders
		StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
		int templateStart = 0;
		int i = 0;
		while (i < args.length) {
			int placeholderStart = template.indexOf("%s", templateStart);
			if (placeholderStart == -1) {
				break;
			}
			builder.append(template, templateStart, placeholderStart);
			builder.append(args[i++]);
			templateStart = placeholderStart + 2;
		}
		builder.append(template, templateStart, template.length());

		// if we run out of placeholders, append the extra args in square braces
		if (i < args.length) {
			builder.append(" [");
			builder.append(args[i++]);
			while (i < args.length) {
				builder.append(", ");
				builder.append(args[i++]);
			}
			builder.append(']');
		}

		return builder.toString();
	}

	/**
	 * Ensures that an object reference passed as a parameter to the calling method is not null.
	 *
	 * @param reference an object reference
	 * @return the non-null reference that was validated
	 * @throws NullPointerException if {@code reference} is null
	 * //@see Verify#verifyNotNull Verify.verifyNotNull()
	 */
	//@CanIgnoreReturnValue
	public static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
		return reference;
	}
}
