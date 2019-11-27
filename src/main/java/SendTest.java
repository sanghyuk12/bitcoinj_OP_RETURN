import com.sun.org.apache.xpath.internal.operations.Neg;
import org.bitcoinj.core.*;
import org.bitcoinj.core.listeners.PreMessageReceivedEventListener;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.utils.Threading;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;

import java.io.File;
import java.math.BigInteger;

import static org.bitcoinj.core.Coin.*;

public class SendTest {
    public static void main(String[] args) throws Exception {

        // Network 설정
        final NetworkParameters params = TestNet3Params.get();

        // walletappkit 설정
        WalletAppKit kit = new WalletAppKit(params, new File("."), "doublespend");

        System.out.println("====");
        kit.startAsync();
        kit.awaitRunning();

        //wallet 정보 확인
        System.out.println("wallet : " + kit.wallet());
        System.out.println("==생성시작==");
        //key import
//
//        System.out.println("==생성끝==");\
//          ECKey key =DumpedPrivateKey.fromBase58(TestNet3Params.get(),"cTrY9Zj4mWxFrMqVLinaKFG6Fx4bn9jxu9XLFArcCHvpyQDvaDSy").getKey();
//        kit.wallet().removeKey(key);
//
//
//
//        kit.wallet().importKey(key);
//
//



        System.out.println("====1");
        System.out.println(kit.wallet());
        //트랜잭션 생성
        Transaction tx1 = new Transaction(params);
        //Output Data 지정
        //잔돈 받을 주소
        tx1.addOutput(Coin.valueOf(100), LegacyAddress.fromBase58(params, "n16MzRQnnmwWM7MzcbJZwsw6Jey3dK2sZ5"));
        // Opreturn 스크립트
        tx1.addOutput(ZERO, new ScriptBuilder().createOpReturnScript("hi".getBytes()));



        // input data 지정
        //지갑에 등록된 UTXO를 가져오거나 특정 트랜재션의 아웃풋을 들고 올수 있음
        tx1.addInput(kit.wallet().getUnspents().get(0));
        //tx1.addInput(kit.wallet().getTransaction(Sha256Hash.wrap("789be05692ac589f1fd5eac1657ea813b6dc67a7a26a6ce9f02053a63dc45a7b")).getOutput(0));

        //input data 서명
        ECKey key = ECKey.fromPrivate(new BigInteger("31654168476"));
//        String privateKeys = "cTrY9Zj4mWxFrMqVLinaKFG6Fx4bn9jxu9XLFArcCHvpyQDvaDSy";
//        DumpedPrivateKey dpk = DumpedPrivateKey.fromBase58(null, privateKeys);
//        ECKey ecKey = dpk.getKey();

        Script scriptPubKey = ScriptBuilder.createOutputScript(LegacyAddress.fromBase58(params,"n16MzRQnnmwWM7MzcbJZwsw6Jey3dK2sZ5"));
        Sha256Hash hash = tx1.hashForSignature(0,scriptPubKey,Transaction.SigHash.ALL,true);
        ECKey.ECDSASignature ecSig = key.sign(hash);
        TransactionSignature txSig = new TransactionSignature(ecSig,Transaction.SigHash.ALL,true);
        tx1.getInput(0).setScriptSig(ScriptBuilder.createInputScript(txSig,key));

        //생성된 트랜잭션 확인

        System.out.println("================================================================");
        System.out.println("Transaction : "+ tx1);
        System.out.println("TxId : " + tx1.getTxId());
        System.out.println("TxFee : " + tx1.getFee());
        System.out.println("Tx status : " + tx1.getConfidence().getDepthInBlocks());//상태
        System.out.println("================================================================");

        final Peer peer = kit.peerGroup().getConnectedPeers().get(0);

        System.out.println("Transactions status : " + kit.wallet().getPendingTransactions());
        //pending 한 트랜잭션 확인

        //트랜잭션 전파

        //
        try {
            Wallet.SendResult result = kit.wallet().sendCoins(SendRequest.forTx(tx1));
            System.out.println("Result :" + result);
        }catch (Exception e){
            e.printStackTrace();
        }

        peer.addPreMessageReceivedEventListener(Threading.SAME_THREAD,
                new PreMessageReceivedEventListener() {
                    @Override
                    public Message onPreMessageReceived(Peer peer, Message m) {
                        System.err.println("Got a message!" + m.getClass().getSimpleName() + ": " + m);
                        return m;
                    }
                }
        );
//        System.out.println("====3");
//        나오는 에러 메세지가 달라서 경우에 따라 아래 트랜잭션 전파 사용
        peer.sendMessage(tx1);

        System.out.println("====4");
        Thread.sleep(5000);
        kit.stopAsync();
        kit.awaitTerminated();
    }

}
