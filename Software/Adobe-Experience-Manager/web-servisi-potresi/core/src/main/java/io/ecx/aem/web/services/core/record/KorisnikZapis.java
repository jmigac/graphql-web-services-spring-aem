package io.ecx.aem.web.services.core.record;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KorisnikZapis {

    private String ime;
    private String prezime;
    private String email;

}
