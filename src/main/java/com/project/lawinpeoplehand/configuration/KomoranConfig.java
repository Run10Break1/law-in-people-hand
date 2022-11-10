package com.project.lawinpeoplehand.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;


@Configuration
public class KomoranConfig {

    @Bean
    Komoran getKomoran() {
        return new Komoran(DEFAULT_MODEL.FULL);
    }
}
