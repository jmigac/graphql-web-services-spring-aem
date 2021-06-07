package io.ecx.spring.graphql.webservice.datafetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.ecx.spring.graphql.webservice.models.PotresModel;
import io.ecx.spring.graphql.webservice.repository.PotresRepozitorij;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TsunamiDataFetcher implements DataFetcher<List<PotresModel>> {

    @Autowired
    private PotresRepozitorij repozitorij;

    @Override
    public List<PotresModel> get(final DataFetchingEnvironment dataFetchingEnvironment) {
        return this.repozitorij.findPotresModelsByTsunamiTrue();
    }
}
