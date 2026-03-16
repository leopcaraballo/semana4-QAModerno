package com.sofka.dynamicloading.ui;

import net.serenitybdd.screenplay.targets.Target;

public final class DynamicLoadingPage {

    public static final String BASE_URL = "https://the-internet.herokuapp.com/dynamic_loading";

    public static final Target START_BUTTON = Target.the("start button")
            .locatedBy("#start button");

    public static final Target LOADER = Target.the("loading spinner")
            .locatedBy("#loading");

    public static final Target FINISH_TEXT = Target.the("hello world text")
            .locatedBy("#finish h4");

        private DynamicLoadingPage() {
    }
}
