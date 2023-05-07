# LeafBank
Bank mobilalkalmazás

| Elem  | Útvonal |
|-------|---------|
|Firebase autentikáció meg van valósítva: Be lehet jelentkezni és regisztrálni | [LoginActivity](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/login/LoginActivity.java) <br /> [RegisterActivity](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/login/RegisterActivity.java)|
|Legalább 2 különböző animáció használata | [bankaccount_anim](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/res/anim/bankaccount_anim.xml) <br /> [login_anim](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/res/anim/login_anim.xml) |
|Legalább egy Lifecycle Hook használata a teljes projektben | [LoginActivity - onStart](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/login/LoginActivity.java) <br /> [HistoryActivity - onResume](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/history/HistoryInActivity.java) |
|Legalább egy olyan androidos erőforrás használata, amihez kell android permission | [HomeActivity - atmbtn_click](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/home/HomeActivity.java) |
|Legalább egy notification vagy alam manager vagy job scheduler használata  | [NotificationHandler](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/NotificationHandler.java) |
| CRUD műveletek mindegyike megvalósult és műveletek service-(ek)be vannak kiszervezve (AsyncTasks)| [C - BankaccountActivity - createBankaccount](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/bankaccount/BankaccountActivity.java) <br /> [R - TransferActivity - sendBtn_click](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/transfer/TransferActivity.java) <br /> [U - TransferActivity - sendMoney](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/transfer/TransferActivity.java) <br /> [D - ProfileActivity - deleteuserbtn_click](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/profile/ProfileActivity.java) |
| Legalább 2 komplex Firestore lekérdezés megvalósítása | [HistoryActivity - onResume](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/history/HistoryInActivity.java) <br /> [BankaccountActivity - onResume](https://github.com/DOBI24/LeafBank/blob/3611f864df0e98e504051e835fd2106986355aa1/Project/app/src/main/java/com/leafbank/bankaccount/BankaccountActivity.java) |
