package com.nva.server.configs;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@PWA(name = "Admin", shortName = "Admin", startPath = "/", backgroundColor = "#227aef", themeColor = "#227aef")
@Theme(value = "my-app", variant = Material.LIGHT)
public class CustomAppShellConfig implements AppShellConfigurator {
}
