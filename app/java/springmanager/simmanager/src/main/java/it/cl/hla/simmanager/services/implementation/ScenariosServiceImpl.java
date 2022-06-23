package it.cl.hla.simmanager.services.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;


@Service
public class ScenariosServiceImpl {

    @Value("${scenario.dir}")
    String scenarioDIR;

    private Integer id;

    private String scenarioName;

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public Integer getId(){
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }


    public ArrayList<String> listScenarios(){
    ArrayList<String> scenarios = new ArrayList<>();
    File dir = new File(scenarioDIR + File.separator);
        if (dir.isDirectory()) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory() && file.getName().endsWith(".scenario")) {
                    scenarios.add(file.getName().substring(0, file.getName().length() - 9));
                }
            }
        }
    }
        return scenarios;
}
}
