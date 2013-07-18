package com.byteflair.elastic.geo.front.util;

import java.util.concurrent.atomic.AtomicLong;

public final class SequenceGenerator {
	private static final AtomicLong sequence = new AtomicLong();

	private SequenceGenerator() {
	}

	public static long next() {
		return sequence.incrementAndGet();
	}
}