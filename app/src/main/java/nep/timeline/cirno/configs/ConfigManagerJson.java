package nep.timeline.cirno.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.topjohnwu.superuser.io.SuFile;

import java.io.File;
import java.io.IOException;

import nep.timeline.cirno.GlobalVars;
import nep.timeline.cirno.configs.settings.ApplicationSettings;
import nep.timeline.cirno.configs.settings.GlobalSettings;
import nep.timeline.cirno.log.Log;
import nep.timeline.cirno.utils.RWUtils;

public class ConfigManagerJson {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private final String globalSettingsName = "GlobalSettings.json";
    private final String applicationSettingsName = "ApplicationSettings.json";

    public void readConfig() {
        try {
            File globalFile = new File(GlobalVars.CONFIG_DIR, globalSettingsName);
            if (!globalFile.exists()) {
                GlobalVars.globalSettings = new GlobalSettings();
                saveConfig();
            } else {
                String globalData = RWUtils.readConfig(GlobalVars.CONFIG_DIR + "/" + globalSettingsName);
                GlobalVars.globalSettings = gson.fromJson(globalData, GlobalSettings.class);
                if (GlobalVars.globalSettings == null) {
                    GlobalVars.globalSettings = new GlobalSettings();
                    saveConfig();
                }
            }
            File applicationFile = new File(GlobalVars.CONFIG_DIR, applicationSettingsName);
            if (!applicationFile.exists()) {
                GlobalVars.applicationSettings = new ApplicationSettings();
                saveConfig();
            } else {
                String applicationData = RWUtils.readConfig(GlobalVars.CONFIG_DIR + "/" + applicationSettingsName);
                GlobalVars.applicationSettings = gson.fromJson(applicationData, ApplicationSettings.class);
                if (GlobalVars.applicationSettings == null) {
                    GlobalVars.applicationSettings = new ApplicationSettings();
                    saveConfig();
                }
            }
        } catch (JsonSyntaxException | JsonIOException e) {
            GlobalVars.globalSettings = new GlobalSettings();
            GlobalVars.applicationSettings = new ApplicationSettings();
            saveConfig();
        }
    }

    public void saveConfig() {
        try {
            GlobalSettings globalSettings = GlobalVars.globalSettings;
            if (globalSettings != null) {
                String globalConfigStr = gson.toJson(GlobalVars.globalSettings);
                RWUtils.writeStringToFile(new File(GlobalVars.CONFIG_DIR, globalSettingsName), globalConfigStr);
            }
            ApplicationSettings applicationSettings = GlobalVars.applicationSettings;
            if (applicationSettings != null) {
                String applicationConfigStr = gson.toJson(GlobalVars.applicationSettings);
                RWUtils.writeStringToFile(new File(GlobalVars.CONFIG_DIR, applicationSettingsName), applicationConfigStr);
            }
        } catch (IOException e) {
            Log.e("Save Config", e);
        }
    }

    public boolean readConfigSU() {
        boolean read = true;
        try {
            SuFile globalFile = new SuFile(GlobalVars.CONFIG_DIR, globalSettingsName);
            if (!globalFile.exists()) {
                GlobalVars.globalSettings = new GlobalSettings();
                read = false;
            } else {
                String globalData = RWUtils.readConfig(globalFile);
                GlobalVars.globalSettings = gson.fromJson(globalData, GlobalSettings.class);
                if (GlobalVars.globalSettings == null) {
                    GlobalVars.globalSettings = new GlobalSettings();
                    read = false;
                }
            }
            SuFile applicationFile = new SuFile(GlobalVars.CONFIG_DIR, applicationSettingsName);
            if (!applicationFile.exists()) {
                GlobalVars.applicationSettings = new ApplicationSettings();
                read = false;
            } else {
                String applicationData = RWUtils.readConfig(applicationFile);
                GlobalVars.applicationSettings = gson.fromJson(applicationData, ApplicationSettings.class);
                if (GlobalVars.applicationSettings == null) {
                    GlobalVars.applicationSettings = new ApplicationSettings();
                    read = false;
                }
            }
        } catch (JsonSyntaxException | JsonIOException e) {
            GlobalVars.globalSettings = new GlobalSettings();
            GlobalVars.applicationSettings = new ApplicationSettings();
            return false;
        }

        return read;
    }

    public void saveConfigSU() {
        try {
            GlobalSettings globalSettings = GlobalVars.globalSettings;
            if (globalSettings != null) {
                String globalConfigStr = gson.toJson(GlobalVars.globalSettings);
                RWUtils.writeStringToFileSU(new SuFile(GlobalVars.CONFIG_DIR, globalSettingsName), globalConfigStr, false);
            }
            ApplicationSettings applicationSettings = GlobalVars.applicationSettings;
            if (applicationSettings != null) {
                String applicationConfigStr = gson.toJson(GlobalVars.applicationSettings);
                RWUtils.writeStringToFileSU(new SuFile(GlobalVars.CONFIG_DIR, applicationSettingsName), applicationConfigStr, false);
            }
        } catch (IOException e) {
            Log.e("Save Config", e);
        }
    }
}
