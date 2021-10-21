package com.avijit.projects.elasticsearch.document;

import com.avijit.projects.elasticsearch.helper.Indices;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Map;

@Data
@NoArgsConstructor
@Document(indexName = Indices.TICKET_INDEX)
public class Ticket extends org.avijit.projects.generated.model.Ticket {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    private int id;
    public static Map<String, Object> getAsMap(final Ticket account) {
        return OBJECT_MAPPER.convertValue(account, new TypeReference<Map<String, Object>>() {
        });
    }
}
