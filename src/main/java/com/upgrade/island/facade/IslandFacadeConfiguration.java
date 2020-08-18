package com.upgrade.island.facade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;

@Configuration
public class IslandFacadeConfiguration {

  @Bean
  public ConversionService conversionService(){
    ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
    factoryBean.setConverters(IslandConverter.all());
    factoryBean.afterPropertiesSet();
    return factoryBean.getObject();
  }
}
