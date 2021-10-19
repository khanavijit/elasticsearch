package com.avijit.projects.elasticsearch.document;

import com.avijit.projects.elasticsearch.helper.Indices;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = Indices.TICKET_INDEX)
public class Ticket {


    @Id
    private int id;
    private String type;
    private String subject;
    private String description;
    private String priority;
    private String status;


}
