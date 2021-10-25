package com.avijit.projects.elasticsearch.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration

public class ApplicationConfiguration  {



    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }


    private final int MAX_MEMORY_SIZE = 50 * 1024 * 1024 ;// 50 MB


    @Bean
    @Primary
    public CodecCustomizer codecCustomizer(){
        final CodecCustomizer codecCustomizer = configurer -> configurer.defaultCodecs().maxInMemorySize(MAX_MEMORY_SIZE);
        return codecCustomizer;

    }





}
