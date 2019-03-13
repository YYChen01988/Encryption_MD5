package com.example.licensekeyapi.controllers;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;



@RestController
@RequestMapping("/api")
public class Controller {

    private String SECRET = "opensesame";
    private String INTERNAL_PRIVATE_KEY = "nobody_knows";

    private String generateLicenceKey(String firstName, String lastName){
        String toHash = firstName + "|" + lastName + "|" + INTERNAL_PRIVATE_KEY;
        return getMD5Hash(toHash);
    }

    private String getMD5Hash(String toHash) {
        // from https://www.mkyong.com/java/java-md5-hashing-example/
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        }catch (NoSuchAlgorithmException e){
            return "";
        }
        byte[] hashInBytes = md.digest(toHash.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    private Boolean validateLicenceKey(String firstName, String secondName, String key){
        String expectedKey = this.generateLicenceKey(firstName,secondName);
        return key.equals(expectedKey);
    }


    @JsonIgnore
    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    public ResponseEntity<String> generate(@RequestParam(value = "firstName") String firstName,
                                           @RequestParam (value = "secondName") String secondName,
                                           @RequestParam (value = "secret") String secret,
                                           @RequestParam (value = "packageName") String packageName) {

        if(!secret.equals(this.SECRET))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        String licenceKey = generateLicenceKey(firstName, secondName);

        return ResponseEntity.ok(licenceKey);
    }

    @JsonIgnore
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public ResponseEntity<String> validate(@RequestParam (value = "firstName") String firstName,
                                           @RequestParam (value = "secondName") String secondName,
                                           @RequestParam (value = "key") String key) {

        Boolean keyValid = validateLicenceKey(firstName, secondName, key);


        if(keyValid)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}

