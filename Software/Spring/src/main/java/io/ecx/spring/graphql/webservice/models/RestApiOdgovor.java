package io.ecx.spring.graphql.webservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestApiOdgovor<T> {

    private int brojRezultata;
    private HttpStatus status;
    private List<T> rezultat;

}
