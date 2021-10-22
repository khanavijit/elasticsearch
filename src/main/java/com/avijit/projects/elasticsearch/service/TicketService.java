package com.avijit.projects.elasticsearch.service;

import com.avijit.projects.elasticsearch.helper.Constants;
import com.avijit.projects.elasticsearch.document.Ticket;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@CacheConfig(cacheNames = Constants.CACHE_NAME)
public interface TicketService {

    @Cacheable(key ="'getTicket-' + #attribute + #value + #fileId")
    public List<Ticket> getTickets(String attribute, String value, String fileId);

}
