// Author: Yvan Burrie

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import SmartHome.*;

abstract public class Home {

    public static void main(String[] args) {

        MainFrame mainFrame = new MainFrame();
    }
}

class HomeError extends RuntimeException {

    private String errorMessage;

    HomeError(String messgae) {
        this.errorMessage = messgae;
    }
}
