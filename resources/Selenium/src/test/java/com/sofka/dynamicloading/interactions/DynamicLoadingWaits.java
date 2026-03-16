package com.sofka.dynamicloading.interactions;

import com.sofka.dynamicloading.ui.DynamicLoadingPage;

import net.serenitybdd.screenplay.Performable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotVisible;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;
import net.serenitybdd.screenplay.waits.WaitUntil;

public final class DynamicLoadingWaits {

    private static final int DEFAULT_TIMEOUT_SECONDS = 15;

    private DynamicLoadingWaits() {
    }

    public static Performable untilStartButtonIsReady() {
        return WaitUntil.the(DynamicLoadingPage.START_BUTTON, isClickable())
                .forNoMoreThan(DEFAULT_TIMEOUT_SECONDS)
                .seconds();
    }

    public static Performable untilLoaderIsVisible() {
        return WaitUntil.the(DynamicLoadingPage.LOADER, isVisible())
                .forNoMoreThan(DEFAULT_TIMEOUT_SECONDS)
                .seconds();
    }

    public static Performable untilLoaderDisappears() {
        return WaitUntil.the(DynamicLoadingPage.LOADER, isNotVisible())
                .forNoMoreThan(DEFAULT_TIMEOUT_SECONDS)
                .seconds();
    }

    public static Performable untilHelloWorldIsVisible() {
        return WaitUntil.the(DynamicLoadingPage.FINISH_TEXT, isVisible())
                .forNoMoreThan(DEFAULT_TIMEOUT_SECONDS)
                .seconds();
    }
}
