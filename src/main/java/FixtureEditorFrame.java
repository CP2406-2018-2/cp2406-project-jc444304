// Author: Yvan Burrie

import com.sun.istack.internal.NotNull;
import SmartHome.*;

/**
 *
 */
class FixtureEditorFrame extends EntityEditorFrame<Fixture> {

    NavigationPanel navigationPanel;

    private Fixture fixture;

    FixtureEditorFrame(@NotNull NavigationPanel navigationPanel, @NotNull Fixture fixture) {

        super(fixture);

        this.navigationPanel = navigationPanel;
        this.fixture = fixture;

        setSize(300, 500);
    }
}
