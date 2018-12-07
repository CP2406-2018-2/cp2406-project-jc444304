// Author: Yvan Burrie

import javax.swing.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

/**
 *
 */
public abstract class WebFrame extends JFrame {

    JFXPanel panel = new JFXPanel();

    WebView webView;

    final String contentHeader =
            "<!DOCTYPE html>" +
            "<htm>" +
            "<head>" +
            "<style>" +
            "h1 {color: black;}" +
            "</style>" +
            "</head>" +
            "<body>";

    final String contentFooter =
            "</body>" +
            "</html>";

    WebFrame(String title, int width, int height) {

        setTitle(title);
        setSize(width, height);
        setResizable(false);
        Platform.runLater(() -> panel.setScene(new Scene(webView = new WebView())));
        add(panel);
    }
}
