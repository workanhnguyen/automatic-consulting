package com.nva.server.views.user;

import com.nva.server.entities.User;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserViewTest {
    static {
        // Prevent Vaadin Development mode to launch browser window
        System.setProperty("vaadin.launch-browser", "false");
    }

    @Autowired
    private UserView userView;

    @Test
    public void formShownWhenUserSelected() {
        Grid<User> userGrid = userView.getUserGrid();
        User firstUser = getFirstItem(userGrid);

        UserForm userForm = userView.getUserForm();

        Assertions.assertFalse(userForm.isVisible());
        userGrid.asSingleSelect().setValue(firstUser);

        Assertions.assertTrue(userView.isVisible());
        Assertions.assertEquals(firstUser.getFirstName(), userForm.getFirstName().getValue());
    }

    private User getFirstItem(Grid<User> userGrid) {
        return ((ListDataProvider<User>) userGrid.getDataProvider()).getItems().iterator().next();
    }
}
