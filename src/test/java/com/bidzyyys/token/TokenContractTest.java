package com.bidzyyys.token;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenContractTest {
    private final Genson genson = new Genson();
    private final String address1 = "0x1234";
    private final String address2 = "0x4321";

    private TokenContract contract;
    private Context ctx;
    private ChaincodeStub stub;

    @BeforeEach
    public void basicInitialization() {
        contract = new TokenContract();
        ctx = mock(Context.class);
        stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
    }

    @Test
    public void balanceOfUnknownAddressTest() {
        when(stub.getStringState(address1)).thenReturn("");

        BigInteger amount = contract.balanceOf(ctx, address1);

        assertEquals(BigInteger.ZERO, amount);
    }

    @Test
    public void balanceOfNonZeroAssetsTest() {
        BigInteger balance = new BigInteger("69");
        when(stub.getStringState(address1)).thenReturn(genson.serialize(balance));

        assertEquals(balance, contract.balanceOf(ctx, address1));
    }

    @Test
    public void mintTest() {
        when(stub.getStringState(address1)).thenReturn("");

        assertEquals(BigInteger.ZERO, contract.balanceOf(ctx, address1));
        BigInteger amount = new BigInteger("69");
        TransferEvent event = contract.mint(ctx, address1, amount);

        when(stub.getStringState(address1)).thenReturn(genson.serialize(amount));

        assertEquals(amount, contract.balanceOf(ctx, address1));
        assertEquals(TokenContract.ZERO_ADDRESS, event.getSender());
        assertEquals(address1, event.getReceiver());
        assertEquals(amount, event.getAmount());
    }

    @Test
    public void burnInsufficientFundsTest() {
        when(stub.getStringState(address1)).thenReturn("");

        assertEquals(BigInteger.ZERO, contract.balanceOf(ctx, address1));
        BigInteger amount = new BigInteger("69");

        Throwable thrown = catchThrowable(() -> {
            contract.burn(ctx, address1, amount);
        });

        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                .hasMessage("Insufficient funds");

        assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("INSUFFICIENT_FUNDS".getBytes());
    }

    @Test
    public void burnTest() {
        BigInteger amount = new BigInteger("69");
        when(stub.getStringState(address1)).thenReturn(genson.serialize(amount));

        assertEquals(amount, contract.balanceOf(ctx, address1));

        TransferEvent event = contract.burn(ctx, address1, amount);

        when(stub.getStringState(address1)).thenReturn(genson.serialize(BigInteger.ZERO));

        assertEquals(BigInteger.ZERO, contract.balanceOf(ctx, address1));
        assertEquals(TokenContract.ZERO_ADDRESS, event.getReceiver());
        assertEquals(address1, event.getSender());
        assertEquals(amount, event.getAmount());
    }

    @Test
    public void transferFromInsufficientFundsTest() {
        when(stub.getStringState(address1)).thenReturn("");
        when(stub.getStringState(address1)).thenReturn("");

        assertEquals(BigInteger.ZERO, contract.balanceOf(ctx, address1));
        BigInteger amount = new BigInteger("69");

        Throwable thrown = catchThrowable(() -> {
            contract.burn(ctx, address1, amount);
        });

        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                .hasMessage("Insufficient funds");

        assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("INSUFFICIENT_FUNDS".getBytes());
    }

    @Test
    public void transferFromTest() {
        BigInteger amount = new BigInteger("69");

        when(stub.getStringState(address1)).thenReturn(genson.serialize(amount));
        when(stub.getStringState(address2)).thenReturn("");

        assertEquals(amount, contract.balanceOf(ctx, address1));
        assertEquals(BigInteger.ZERO, contract.balanceOf(ctx, address2));

        TransferEvent event = contract.transferFrom(ctx, address1, address2, amount);

        assertEquals(address2, event.getReceiver());
        assertEquals(address1, event.getSender());
        assertEquals(amount, event.getAmount());

        when(stub.getStringState(address2)).thenReturn(genson.serialize(amount));
        when(stub.getStringState(address1)).thenReturn("");

        assertEquals(amount, contract.balanceOf(ctx, address2));
        assertEquals(BigInteger.ZERO, contract.balanceOf(ctx, address1));
    }
}
