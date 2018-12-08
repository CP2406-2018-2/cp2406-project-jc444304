// Author: Yvan Burrie

import javafx.application.Platform;

class GuideFrame extends WebFrame {

    GuideFrame() {

        super(MainFrame.APPLICATION_NAME + " - Guide", 600, 500);
        Platform.runLater(() -> webView.getEngine().loadContent(
                contentHeader +
                        "<h1>Guide to using " + MainFrame.APPLICATION_NAME + "</h1>" +
                        "<p>Welcome</p>" +
                        "<h2>Triggers</h2>" +
                        "<p>Triggers is essential for <i>" + MainFrame.APPLICATION_NAME + "</i> automated ability." +
                        "It allows you to create a set of triggers that contain events followed by actions.</p>" +
                        contentFooter
        ));
    }
}
