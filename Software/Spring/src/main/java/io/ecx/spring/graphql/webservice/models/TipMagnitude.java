package io.ecx.spring.graphql.webservice.models;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table
@Entity
public class TipMagnitude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tip;

    public TipMagnitude(final String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return this.tip;
    }
}
