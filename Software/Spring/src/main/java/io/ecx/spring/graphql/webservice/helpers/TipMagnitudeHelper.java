package io.ecx.spring.graphql.webservice.helpers;

import io.ecx.spring.graphql.webservice.models.TipMagnitude;

import java.util.ArrayList;
import java.util.List;

public class TipMagnitudeHelper {

    public static List<TipMagnitude> dohvatiSveTipoveMagnitude() {
        final List<TipMagnitude> tipovi = new ArrayList<>();
        tipovi.add(new TipMagnitude("2"));
        tipovi.add(new TipMagnitude("4"));
        tipovi.add(new TipMagnitude("fa"));
        tipovi.add(new TipMagnitude("H"));
        tipovi.add(new TipMagnitude("lg"));
        tipovi.add(new TipMagnitude("m"));
        tipovi.add(new TipMagnitude("ma"));
        tipovi.add(new TipMagnitude("mb"));
        tipovi.add(new TipMagnitude("MbLg"));
        tipovi.add(new TipMagnitude("mb_lg"));
        tipovi.add(new TipMagnitude("mc"));
        tipovi.add(new TipMagnitude("Md"));
        tipovi.add(new TipMagnitude("md1"));
        tipovi.add(new TipMagnitude("Me"));
        tipovi.add(new TipMagnitude("mfa"));
        tipovi.add(new TipMagnitude("mh"));
        tipovi.add(new TipMagnitude("Mi"));
        tipovi.add(new TipMagnitude("mint"));
        tipovi.add(new TipMagnitude("ml"));
        tipovi.add(new TipMagnitude("mlg"));
        tipovi.add(new TipMagnitude("mlr"));
        tipovi.add(new TipMagnitude("mlv"));
        tipovi.add(new TipMagnitude("Ms"));
        tipovi.add(new TipMagnitude("ms_20"));
        tipovi.add(new TipMagnitude("Mt"));
        tipovi.add(new TipMagnitude("mun"));
        tipovi.add(new TipMagnitude("mw"));
        tipovi.add(new TipMagnitude("mwb"));
        tipovi.add(new TipMagnitude("mwc"));
        tipovi.add(new TipMagnitude("mwp"));
        tipovi.add(new TipMagnitude("mwr"));
        tipovi.add(new TipMagnitude("mww"));
        tipovi.add(new TipMagnitude("no"));
        tipovi.add(new TipMagnitude("uk"));
        tipovi.add(new TipMagnitude("Unknown"));
        return tipovi;
    }

}
