import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.TestNet3Params;

public class AddressTest {

    public static void main(String[] args) {
//        ECKey key = ECKey.fromPrivate("cTrY9Zj4mWxFrMqVLinaKFG6Fx4bn9jxu9XLFArcCHvpyQDvaDSy".getBytes());
        ECKey key =DumpedPrivateKey.fromBase58(TestNet3Params.get(),"cTrY9Zj4mWxFrMqVLinaKFG6Fx4bn9jxu9XLFArcCHvpyQDvaDSy").getKey();


//        System.out.println(TestNet3Params. key.getPubKeyHash());
        System.out.println(key.getPrivateKeyEncoded(TestNet3Params.get()).toBase58());
        System.out.println(key.getPubKey());

    }
}
