package net.getko.iilrepository;

import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.config.GlobalConfig;
import net.getko.iilrepository.models.domain.Actor;
import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.models.dto.ActorDto;
import net.getko.iilrepository.models.dto.IilDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(GlobalConfig.class)
public class TestingConfiguration {
    @Bean
    public DomainDtoMapper iilDomainToDtoMapper() {
        return new DomainDtoMapper<Iil, IilDto>();
    }

    @Bean
    public DomainDtoMapper iilDtoToDomainMapper() {
        return new DomainDtoMapper<IilDto, Iil>();
    }

    @Bean
    public DomainDtoMapper actorDomainToDtoMapper() { return new DomainDtoMapper<Actor, ActorDto>(); }

    @Bean
    public DomainDtoMapper actorDtoToDomainMapper() { return new DomainDtoMapper<ActorDto, Actor>(); }

}
