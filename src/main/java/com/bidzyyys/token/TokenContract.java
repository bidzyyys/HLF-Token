package com.bidzyyys.token;

import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;

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
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        name = "Daniel Bigos",
                        url = "https://github.com/bidzyyys")))
@Default
public final class TokenContract implements ContractInterface {
}
