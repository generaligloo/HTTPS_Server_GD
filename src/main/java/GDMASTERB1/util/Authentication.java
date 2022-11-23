package GDMASTERB1.util;

import GDMASTERB1.core.HttpsConnectionWorkerThread;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.security.MessageDigest;

public class Authentication {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpsConnectionWorkerThread.class);

    public Authentication() {

    }

    public static boolean verifyAuthentication(String[] payload) {
        String username = payload[1];
        String password = payload[3];
        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(new FileReader("src\\main\\java\\GDMASTERB1\\users.json"));
            JSONArray usersArray = (JSONArray) object.get("users");
            String[] loginArray = new String[usersArray.size()];
            String[] passwordArray = new String[usersArray.size()];
            for (int i = 0; i < usersArray.size(); i++) {
                loginArray[i] = (String) ((JSONObject) usersArray.get(i)).get("login");
                passwordArray[i] = (String) ((JSONObject) usersArray.get(i)).get("password");
            }
            int index = -1;
            for (int i = 0; i < loginArray.length; i++) {
                if(username.equals(loginArray[i])) {
                    index = i;
                }
            }
            if(index > -1) {
                //Récupère le mdp depuis le json
                String should = passwordArray[index];

                //Crée un digest du mdp reçu
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(password.getBytes());
                //Ajout du "salt"
                byte[] result = md.digest("MaGa".getBytes());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < result.length; i++) {
                    sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
                }

                //Compare le digest du mdp recu avec le digest enregistre dans le json
                if(MessageDigest.isEqual(sb.toString().getBytes(), should.getBytes())) {
                    LOGGER.debug("User authenticated");
                    return true;
                } else {
                    LOGGER.debug("Password incorrect");
                    return false;
                }
            } else {
                LOGGER.debug("User is not in the database");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return true;
    }
}
