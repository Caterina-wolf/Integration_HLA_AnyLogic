package it.cl.hla.master.configurations;

import it.cl.hla.core.implementation.HlaCoreImpl;
import it.cl.hla.core.interfaces.HlaCore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HLACoreConfiguration {

    @Bean
    public HlaCore getHlaCore(){
        return new HlaCoreImpl();
    }
}
