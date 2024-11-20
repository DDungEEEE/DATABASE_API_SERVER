package net.ddns.sbapiserver.repository.helper;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public record RepositoryHelper(EntityManager em) {



}
