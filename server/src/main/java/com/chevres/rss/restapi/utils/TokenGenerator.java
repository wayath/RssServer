/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author anthony
 */
public class TokenGenerator {

    private final String token;

    public TokenGenerator() {
        SecureRandom random = new SecureRandom();
        this.token = new BigInteger(130, random).toString(32);
    }

    public String getToken() {
        return token;
    }

}
