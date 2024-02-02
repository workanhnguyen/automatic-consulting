package com.nva.server.configs;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

@Theme(value = "my-app")
@PWA(
        name = "Admin Console",
        shortName = "Admin Console",
        startPath = "/admin",
        backgroundColor = "#227aef",
        themeColor = "#227aef",
        offlinePath = "offline.html",
        offlineResources = {"icons/icon.png", "images/offline.png"}
)
public class CustomVaadinUIConfig implements AppShellConfigurator {
}
