package com.avijit.projects.elasticsearch.helper;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

public final class Indices {

    public static final String TICKET_INDEX = "ticket-system";
    public static final String TICKET_TYPE = "_doc";



    public static NativeSearchQuery getNativeQuery(String attribute, String value) {
        NativeSearchQuery searchQuery ;
        if(attribute.equalsIgnoreCase("id")){
            searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery(attribute,value))
                    .build();
        }
        else{
            searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery(attribute,value)
                            .operator(Operator.OR)
                            .lenient(true)
                            .fuzziness(Fuzziness.ONE)
                            .prefixLength(3)
                    )
                    .build();
        }
        return searchQuery;
    }
}
