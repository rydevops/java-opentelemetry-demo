package com.example.oteldemo;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;


public class OtelDemo {
	private static Tracer tracer;
	private static LongCounter counter;
	
	public static void main(String[] args) {
		init_otel();
		System.out.println("OTelDemo!!!");
		doSomething("Demo World");
		doSomething("Demo World 2");
		doAnotherThing("Demo World 3");
		doAnotherThing("Demo World 3");
		doAnotherThing("Demo World 3");
		doAnotherThing("Demo World 3");
		doAnotherThing("Demo World 3");
		doAnotherThing("Demo World 3");
		doAnotherThing("Demo World 3");
	}

	public static void init_otel() {
		tracer = GlobalOpenTelemetry.getTracerProvider().tracerBuilder("otel-demo").build();
	
		MeterProvider meterProvider = GlobalOpenTelemetry.getMeterProvider();
		Meter meter = meterProvider.meterBuilder("my-meter").setInstrumentationVersion("1.0.0").build();
		counter = meter.counterBuilder("my-counter").setDescription("Sample counter").build();
	}
	
	@WithSpan
	public static void doSomething(@SpanAttribute("sampleInput") String input) {
		System.out.println("in doSomething");
		System.out.printf("SpanAttribute = %s\n", input);
	}
	
	public static void doAnotherThing(String someInput) {
		Span span = OtelDemo.tracer.spanBuilder("doAnotherThing").startSpan();

		counter.add(1);
		try (Scope scope = span.makeCurrent()) {
			span.setAttribute("someInput", someInput);
			System.out.println("in doAnotherThing");
		} finally {
			span.end();
		}
	}

}
