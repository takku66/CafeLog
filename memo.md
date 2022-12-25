# web/ap単体の起動dockerコマンド
docker build -t takku66/cafelog .
docker run -dp 8888:8080 --name cafelog-java takku66/cafelog

# DBサーバーと接続するためのネットワーク設定
docker network create cafelog-app

##### powershellでやる場合
docker run -it -d `
--network cafelog-app --network-alias db `
-v /$(pwd)/docker/pg/data:/var/lib/postgresql/data `
-v /$(pwd)/docker/pg/scripts:/docker-entrypoint-initdb.d `
--name cafelog-postgres `
-e POSTGRES_PASSWORD=postgres `
-e POSTGRES_DB=cafelog `
-e POSTGRES_USER=postgres `
-p 5433:5432 `
postgres:14.0

##### Git bashでやる場合
docker run -it -d --network cafelog-app --network-alias db -v /$(pwd)/docker/pg/data:/var/lib/postgresql/data -v /$(pwd)/docker/pg/scripts:/docker-entrypoint-initdb.d --name cafelog-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=cafelog -e POSTGRES_USER=postgres -p 5433:5432 postgres:14.0

# docekr-composeを使って起動する場合の注意
docker-compose -f docker-compose.yml up -d
で起動することができる。
この時、dockerデスクトップ上は、cafelogというコンテナが１つ起動中になっており、
その中に入ると、tomcatとpostges用のコンテナが２つ立ち上がっているというイメージ。

そのため、docker-compose.xml内に２つ分のコンテナ起動のための記述を記載すると思うが、
container-nameを、cafelog以外にしなければいけない



# pythonの環境構築
一旦src/main/pythonにフォルダを作ったが、ここでよいのか？
まぁ一旦よしとして、方針としては、pythonで画像を読み込ませ、文字をjson形式で出力するようにすることが目標
マルチスレッドでアクセスされることも考慮しないといけないが、負荷がかかりすぎるため、ある意味シリアルな処理前提で組んだ方がいいかもしれない。

### まずは環境構築

ながらくpythonを触っていなかったため、バージョンが古くなっている
公式サイトから最新版のpythonを落としてきて、そのままインストール画面でupgradeを選択すればおｋ

次はVSCodeで使えるようにする。まずは拡張機能。
Python extension for Visual Studio Codeをインストール

#### 仮想環境の構築
python -m venv .プロジェクト名とか
で仮想環境用のフォルダを作る

作れたら、インタプリタの設定をする

ctrl + shift + pでコマンドパレット
python select interpreterを選択して、
インタープリターパスを直接入力
パスは、仮想環境用のフォルダ/Scripts/python.exe
を指定する。

### 適当に動作確認
main.py的なものやhello.py的なものを作って、お試しプログラム。
VSCode上の右下が、仮想環境用のフォルダ名になっていればおｋ


# Vue.js関連
### 起動方法
docker-compose upをすれば、自動的にコンテナ内部でyarn serveしてくれる
9000ポートでアクセスすればおｋ
