package io.andrelucas.toiletnearme.common.infrastrucuture.config;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OtelConfiguration {
    @Bean
    public OpenTelemetry openTelemetry(@Value("${otel.exporter.endpoint}") String endpoint) {
        return openTelemetryManuallySdk(endpoint);
    }

    private OpenTelemetrySdk openTelemetryManuallySdk(String endpoint) {
        Resource resource = Resource.getDefault().toBuilder()
                .put(ResourceAttributes.SERVICE_NAME, "toilet-near-me-spring")
                .put(ResourceAttributes.SERVICE_VERSION, "0.1.0").build();

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(BatchSpanProcessor.builder(
                        OtlpGrpcSpanExporter
                                .builder()
                                .setEndpoint(endpoint)
                                .setTimeout(Duration.ofSeconds(2))
                                .build())
                        .setScheduleDelay(Duration.ofMillis(100))
                        .build())
                .build();

        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
                .setResource(resource)
                .registerMetricReader(
                        PeriodicMetricReader.builder(OtlpGrpcMetricExporter
                                        .builder()
                                        .setEndpoint(endpoint)
                                        .build()
                                ).setInterval(Duration.ofSeconds(1))
                                .build())
                .build();

        SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
                .setResource(resource)
                .addLogRecordProcessor(BatchLogRecordProcessor.builder(OtlpGrpcLogRecordExporter
                                .builder()
                                .setEndpoint(endpoint)
                        .setTimeout(Duration.ofSeconds(2))
                                .build())
                        .setScheduleDelay(Duration.ofMillis(100))
                        .build())
                .build();

        OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setMeterProvider(sdkMeterProvider)
                .setLoggerProvider(sdkLoggerProvider)
                .build();

        Runtime.getRuntime().addShutdownHook(new Thread(openTelemetrySdk::shutdown));

        return openTelemetrySdk;

    }
}
