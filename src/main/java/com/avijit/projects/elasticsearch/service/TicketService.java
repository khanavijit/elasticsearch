package com.avijit.projects.elasticsearch.service;

import com.avijit.projects.elasticsearch.helper.Constants;
import com.avijit.projects.elasticsearch.document.Ticket;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;

@CacheConfig(cacheNames = Constants.CACHE_NAME )
public interface TicketService {

    @Cacheable(key ="'getTicket-' + #attribute + #value + #fileId", unless="#result==null or #result.isEmpty()")
    public List<Ticket> getTickets(String attribute, String value, String fileId);

    List<Ticket> getTicketsInd(String attribute, String value, String s);


}
