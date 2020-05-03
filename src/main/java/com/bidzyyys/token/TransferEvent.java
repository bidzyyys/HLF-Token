package com.bidzyyys.token;

import lombok.AccessLevel;
import lombok.Getter;

import java.math.BigInteger;

/**
 * Event emitted while transferring funds
 */
public class TransferEvent {
    @Getter(AccessLevel.PROTECTED)
    private String sender;
    @Getter
    private String receiver;
    @Getter
    private BigInteger amount;

    public TransferEvent(String sender, String receiver, BigInteger amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }
}
