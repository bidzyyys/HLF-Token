package com.bidzyyys.token;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.math.BigInteger;

/**
 * Java implementation of the ERC20 Token on Hyperledger Fabric
 */
@Contract(
        name = "TokenContract",
        info = @Info(
                title = "ERC20 Token contract",
                description = "HLF ERC20 token contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "The GNU General Public License v3.0",
                        url = "https://www.gnu.org/licenses/gpl-3.0.html"),
                contact = @Contact(
                        name = "Daniel Bigos",
                        url = "https://github.com/bidzyyys")))
@Default
public final class TokenContract implements ContractInterface {
    private final Genson genson = new Genson();

    /**
     * Retrieves balance of specified account.
     *
     * @param ctx     the transaction context
     * @param address address of the account
     * @return balance of the account, 0 if unknown
     */
    @Transaction()
    public BigInteger balanceOf(final Context ctx, final String address) {
        ChaincodeStub stub = ctx.getStub();
        String accountState = stub.getStringState(address);
        return accountState.isEmpty() ? BigInteger.ZERO : genson.deserialize(accountState, BigInteger.class);
    }

}
