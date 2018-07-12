package org.tron.core.config;

public interface Parameter {

    public class CommonConstant {
        public static final byte ADD_PRE_FIX_BYTE_MAINNET = (byte) 0x41;   //41 + address
        //public static final byte ADD_PRE_FIX_BYTE_TESTNET = (byte) 0xa0;   //a0 + address
        public static final int ADDRESS_SIZE = 21;
        public static final int BASE58CHECK_ADDRESS_SIZE = 35;

        public static byte getAddressPrefix() {
            return ADD_PRE_FIX_BYTE_MAINNET;
        }
    }
}
