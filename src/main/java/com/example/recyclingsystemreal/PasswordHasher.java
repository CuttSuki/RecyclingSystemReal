package com.example.recyclingsystemreal;

import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Argon2;

public class PasswordHasher {
    static String hashPassword(String password){
        Hash hash = Password.hash(password).withArgon2();
        return hash.getResult();
    }
    static boolean checkPassword(String plainPassword, String storedHash) {
        return Password.check(plainPassword, storedHash).withArgon2();
    }
}
