# bitcoinj_OP_RETURN

BitcoinJ 라이브러리를 활용 OP_RETURN 트랜잭션 전송 프로그래밍

maven 프로젝트 생성 후 git clone mvn clean - mvn install

src/main/java/SendTest.java에서 UTXO 주소를 가지고있는 Key값을 포함한 뒤 실행

코인전송 트랜잭션 생성 후 output 부분에 key값의 address주소로(즉, 내가 나에게) utxo주소의 코인양을 전송 과 동시에 ZERO COIN을 보내는 OP_RETURN output 발생 시킴으로 써 OP_RETURN 트랜잭션 생성  
