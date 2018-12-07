// Author: Yvan Burrie

package SmartHome;

import javafx.application.Platform;

/**
 *
 */
public class AboutFrame extends WebFrame {

    AboutFrame() {

        super(MainFrame.APPLICATION_NAME + " - About", 300, 300);
        Platform.runLater(() -> webView.getEngine().loadContent(
            contentHeader +
            "<h1>About Auto Home</h1>" +
            contentFooter
        ));
    }
}
