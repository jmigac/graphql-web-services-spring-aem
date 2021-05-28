package io.ecx.spring.graphql.webservice.datafetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.ecx.spring.graphql.webservice.models.PotresModel;
import io.ecx.spring.graphql.webservice.repository.PotresRepozitorij;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class PotresDataFetcher implements DataFetcher<PotresModel> {

    @Autowired
    private PotresRepozitorij repozitorij;

    @Override
    public PotresModel get(final DataFetchingEnvironment dataFetchingEnvironment) {
        final String potresId = dataFetchingEnvironment.getArgument("id");
        return Optional.ofNullable(this.repozitorij.findPotresModelById(potresId)).orElse(null);
    }
}
