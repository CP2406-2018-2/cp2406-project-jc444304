// Author: Yvan Burrie

import javafx.application.Platform;

class GuideFrame extends WebFrame {

    GuideFrame() {

        super(MainFrame.APPLICATION_NAME + " - Guide", 600, 500);
        Platform.runLater(() -> webView.getEngine().loadContent(
            contentHeader +
            "<h1>Guide to using Auto Home</h1>" +
            "<p>Welcome</p>" +
            contentFooter
        ));
    }
}
