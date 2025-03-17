package com.members.membersService.config;

import java.time.Duration;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.members.membersService.model.DatosEntrenamiento;
import com.members.membersService.model.ResumenEntrenamiento;

@Configuration
public class KafkaStreamsConfig {

    @Bean
    public StreamsBuilder streamsBuilder() {
        return new StreamsBuilder();  // This creates and returns a new StreamsBuilder instance
    }

    @Bean
    public KStream<String, DatosEntrenamiento> kStream(StreamsBuilder streamsBuilder) {
        // Crear un stream de Kafka con el topic "datos-entrenamiento"
        KStream<String, DatosEntrenamiento> stream = streamsBuilder.stream("datos-entrenamiento");

        stream.groupByKey()
              .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofDays(7)))  // Usar la ventana de 7 días
              .aggregate(
                  ResumenEntrenamiento::new,  // Función para inicializar el acumulador
                  (key, value, aggregate) -> {
                      aggregate.actualizar(value);  // Actualiza el resumen con los datos
                      return aggregate;
                  },
                  Materialized.as("resumen-entrenamiento-store")  // Almacenar en el estado de Kafka Streams
              )
              .toStream()
              .to("resumen-entrenamiento");  // Enviar los resultados procesados a otro topic

        return stream;
    }
}
