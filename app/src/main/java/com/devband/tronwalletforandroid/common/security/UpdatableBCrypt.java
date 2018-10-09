package com.devband.tronwalletforandroid.common.security;

import android.os.Build;

import org.mindrot.jbcrypt.BCrypt;

import java.util.function.Function;

// https://github.com/StubbornJava/StubbornJava/blob/master/stubbornjava-common/src/main/java/com/stubbornjava/common/UpdatableBCrypt.java
public class UpdatableBCrypt {

    private final int logRounds;

    public UpdatableBCrypt(int logRounds) {
        this.logRounds = logRounds;
    }

    public String gensalt() {
        return BCrypt.gensalt(logRounds);
    }

    public String hash(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public boolean verifyAndUpdateHash(String password, String hash, Function<String, Boolean> updateFunc) {
        if (BCrypt.checkpw(password, hash)) {
            int rounds = getRounds(hash);
            // It might be smart to only allow increasing the rounds.
            // If someone makes a mistake the ability to undo it would be nice though.
            if (rounds != logRounds) {
                String newHash = hash(password, gensalt());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return updateFunc.apply(newHash);
                }
            }
            return true;
        }
        return false;
    }

    /*
     * Copy pasted from BCrypt internals :(. Ideally a method
     * to exports parts would be public. We only care about rounds
     * currently.
     */
    private int getRounds(String salt) {
        char minor = (char)0;
        int off = 0;

        if (salt.charAt(0) != '$' || salt.charAt(1) != '2')
            throw new IllegalArgumentException ("Invalid salt version");
        if (salt.charAt(2) == '$')
            off = 3;
        else {
            minor = salt.charAt(2);
            if (minor != 'a' || salt.charAt(3) != '$')
                throw new IllegalArgumentException ("Invalid salt revision");
            off = 4;
        }

        // Extract number of rounds
        if (salt.charAt(off + 2) > '$')
            throw new IllegalArgumentException ("Missing salt rounds");
        return Integer.parseInt(salt.substring(off, off + 2));
    }

}
