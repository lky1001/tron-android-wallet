pragma solidity ^0.4.23;
// https://github.com/ProofSuite/amp-dex/blob/develop/contracts/Exchange.sol
// https://github.com/ltfschoen/dex/blob/master/contracts/Exchange.sol

import "./Owned.sol";

contract Exchange is Owned {

    enum Errors {
        SIGNATURE_INVALID,                      // Signature is invalid
        MAKER_SIGNATURE_INVALID,                // Maker signature is invalid
        TAKER_SIGNATURE_INVALID,                // Taker signature is invalid
        ORDER_EXPIRED,                          // Order has already expired
        TRADE_ALREADY_COMPLETED_OR_CANCELLED,   // Trade has already been completed or it has been cancelled by taker
        TRADE_AMOUNT_TOO_BIG,                   // Trade buyToken amount bigger than the remianing amountBuy
        ROUNDING_ERROR_TOO_LARGE                // Rounding error too large
    }

    string constant public VERSION = "1.0.0";
}