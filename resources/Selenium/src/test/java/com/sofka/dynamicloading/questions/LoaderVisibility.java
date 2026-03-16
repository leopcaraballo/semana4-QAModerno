package com.sofka.dynamicloading.questions;

import com.sofka.dynamicloading.ui.DynamicLoadingPage;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class LoaderVisibility implements Question<Boolean> {

    public static LoaderVisibility displayed() {
        return new LoaderVisibility();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            return DynamicLoadingPage.LOADER.resolveFor(actor).isVisible();
        } catch (Exception e) {
            return false;
        }
    }
}
