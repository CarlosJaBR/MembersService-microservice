package com.members.membersService.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class RecuperationService {

    private final KafkaConsumer<String, String> consumer;
    private final String offsetFilePath = "/opt/kafka/offsets/offsets.json"; // Ruta donde se almacenarán los offsets

    public RecuperationService(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    public void iniciarProcesamiento() {
        consumer.subscribe(Arrays.asList("datos-entrenamiento", "resumen-entrenamiento"));

        // Cargar el último offset procesado desde el archivo
        Map<TopicPartition, Long> ultimoOffsetProcesado = cargarUltimoOffset();

        // Ajustar el consumidor al último offset procesado
        for (TopicPartition partition : ultimoOffsetProcesado.keySet()) {
            consumer.seek(partition, ultimoOffsetProcesado.get(partition));
        }

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                procesarRecord(record);
                // Guardar el offset en el archivo después de procesar el mensaje
                guardarOffset(record.topic(), record.partition(), record.offset());
            }
        }
    }

    // Cargar el último offset procesado desde el archivo
    private Map<TopicPartition, Long> cargarUltimoOffset() {
        Map<TopicPartition, Long> offsets = new HashMap<>();

        File file = new File(offsetFilePath);
        if (!file.exists()) {
            // Si el archivo no existe, inicializamos el archivo con valores predeterminados (por ejemplo, 0L)
            return offsets;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Aquí puedes convertir la línea en un TopicPartition y un offset
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    TopicPartition partition = new TopicPartition(parts[0], Integer.parseInt(parts[1]));
                    long offset = Long.parseLong(parts[2]);
                    offsets.put(partition, offset);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Manejo de errores (podrías agregar más robustez aquí)
        }

        return offsets;
    }

    // Guardar el offset procesado en un archivo
    private void guardarOffset(String topic, int partition, long offset) {
        File file = new File(offsetFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(topic + ":" + partition + ":" + offset);
            writer.newLine(); // Guardamos el nuevo offset al final del archivo
        } catch (IOException e) {
            e.printStackTrace();  // Manejo de errores
        }
    }

    // Procesar el mensaje de Kafka
    private void procesarRecord(ConsumerRecord<String, String> record) {
        // Aquí va la lógica para procesar el mensaje
    }
}
