package org.marzaha.im.front.startup;

import org.marzaha.im.front.control.FrontAppControl;

public class StartUp {

    public static void main(String... arg){
        FrontAppControl frontAppControl = new FrontAppControl();
        frontAppControl.initialize();
        frontAppControl.start();
    }
}
