# Auction Sniper

## 기본 용어

- 품목(Item) : 식별, 구매 가능한 것.
- 입찰자(Bidder) : 품목 구매에 관심이 있는 사람이나 조직.
- 입찰(Bid) : 입찰자가 어떤 품목에 대해 특정 가격을 지불하리라는 진술.
- 현재가(Current price) : 어떤 품목에 대해 현재 가장 높은 입찰.
- 매매 지시 지정 가격(Stop price) : 어떤 품목에 대해 입찰자가 지불할 의사가 있는 가장 높은 가격.
- 경매(Auction) : 어떤 품목에 대한 입찰을 관리하는 프로세스.
- 경매장(Auction house) : 경매를 주관하는 기관.

## 경매와의 상호 작용

### 경매 프로토콜 (명령)

- Join
    - 입찰자가 경매에 참여.
- Bid
    - 입찰자가 경매에 입찰가를 보냄.

### 경매 프로토콜 (이벤트)

- Price
    - 경매에서 현재 수락된 가격을 보고.
    - 포함된 데이터
        - 다음 입찰 때 높여야 할 최소 기준 가격.
        - 현재 가격을 제시한 입찰자 이름.
    - 이벤트 전달 시점
        - 입찰자가 경매에 참여할 때 -> 입찰자에게
        - 새로운 입찰이 수락될 때마다 -> 모든 입찰자에게
- Close
    - 경매 종료를 알림.

## 제작할 기능

- ~~단일 품목 : 경매 참여, 입찰하지 못한 상태로 낙찰 실패~~
- ~~단일 품목 : 경매 참여, 입찰, 낙찰 실패~~
- ~~단일 품목 : 경매 참여, 입찰, 낙찰~~
- 가격 상세 표시
- 여러 품목
- 사용자 인터페이스를 통해 품목을 추가
- 매매 지시 지정 가격에서 입찰을 중단
- 번역기 : 경매에서 유효하지 않은 메시지가 전달됨
- 번역기 : 올바르지 않은 메시지 버전
- 경매 : 전송시 XMPPException 처리

## 실행환경 구성

### 로컬 XMPP 브로커 구성

- [웹 문서](https://edgevpn.io/openfiredocker/) 참고
- Docker 사용하여 openfire 컨테이너 구동

```shell
docker run --name openfire -d -p 9090:9090 -p 5222:5222  quantumobject/docker-openfire
```

- `localhost:9090` 접속하여 로컬 XMPP 브로커 설정 진행
    - 위 웹 문서 참고하여 기본 설정 및 계정 생성 진행.
    - 책 p.108에 나와있는 계정, 비밀번호 추가한다.
