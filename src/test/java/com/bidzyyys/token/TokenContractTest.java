package com.bidzyyys.token;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenContractTest {
    private final Genson genson = new Genson();

    @Test
    public void balanceOfUnknownAddress() {
        TokenContract contract = new TokenContract();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);

        String address = "1234";
        when(stub.getStringState(address)).thenReturn("");

        BigInteger amount = contract.balanceOf(ctx, address);

        assertThat(amount).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void balanceOfNonZeroAssets() {
        TokenContract contract = new TokenContract();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);

        String address = "1234";
        BigInteger balance = new BigInteger("69");
        when(stub.getStringState(address)).thenReturn(genson.serialize(balance));

        assertThat(contract.balanceOf(ctx, address)).isEqualTo(balance);
    }
}
