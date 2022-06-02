package src;

import hla.rti1516e.exceptions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Classe Main, gestisce join e resign tramite istanza di un nuovo Coordinator
 */
public class Main {

    public void run() throws RTIexception {
        Coordinator coordinator = new Coordinator();
        coordinator.Start();
        System.out.println("Press Q and Enter to exit");
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String in = inputReader.readLine();
                if (in.equalsIgnoreCase("q")) {
                    coordinator.Stop();
                    break;
                }
            } catch (IOException | RTIinternalError ignore) {}
        }
    }


    public void stop() throws RTIinternalError {
        Coordinator coordinator = new Coordinator();
        coordinator.Stop();
    }

    public static void main(String[] args) throws RTIexception {
        Main federate = new Main();
        federate.run();
    }
}