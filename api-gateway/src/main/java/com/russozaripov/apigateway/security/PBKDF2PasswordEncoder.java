package com.russozaripov.apigateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PBKDF2PasswordEncoder implements PasswordEncoder {
    private static final String SECRET_KEY_INSTANCE = "PBKDF2WithHmacSHA512";
    @Value("${jwt.password.encoder.secret}")
    private String secret;
    @Value("${jwt.password.encoder.iteration}")
    private Integer iteration; // количество итераций энкодинга
    @Value("${jwt.password.encoder.keyLength}")
    private Integer keyLength; // длина ключа

    @Override
    public String encode(CharSequence rawPassword) {

        try {
            byte[] result = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE) // берем экземпляр этого алгоритма PBKDF2WithHmacSHA512
                    .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(),
                            secret.getBytes(),
                            iteration,
                            keyLength)) // кодируем пароль на основе пароля который к нам поступил, на основе нашего секрета, длины секрета и кол-ва итераций
                    .getEncoded();
            return Base64.getEncoder()
                    .encodeToString(result); // кодируем получившийся массив байтов в строку

        }catch (NoSuchAlgorithmException | InvalidKeySpecException exception){
            throw new RuntimeException(exception);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword); // проверяем пароль с клиента и закодированный пароль который мы передадим из базы данных
    }
}
