package nl.paulzijlmans.axoncqrseventsourcing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SerializerConfiguration {

    // By default, we want the XStreamSerializer
    @Primary
    @Bean
    public Serializer defaultSerializer() {
        // Set the secure types on the xStream instance
        XStream xStream = new XStream();
        return XStreamSerializer.builder()
                .xStream(xStream)
                .build();
    }

    // But for all our messages we'd prefer the JacksonSerializer due to JSON's smaller format
    @Qualifier("messageSerializer")
    @Bean
    public Serializer messageSerializer(ObjectMapper mapper) {
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
        return JacksonSerializer.builder()
                .objectMapper(mapper)
                .lenientDeserialization()
                .build();
    }
}
