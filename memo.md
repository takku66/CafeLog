# web/ap単体の起動dockerコマンド
docker build -t takku66/cafelog .
docker run -dp 8888:8080 takku66/cafelog --name cafelog

# DBサーバーと接続するためのネットワーク設定
docker network create cafelog-app

docker run -it -d `
--network cafelog-app --network-alias postgres `
-v vol_scripts:/docker-entrypoint-initdb.d `
--name cafelog-postgres `
-e POSTGRES_PASSWORD=postgres `
-e POSTGRES_DB=cafelog `
-e POSTGRES_USER=postgres `
-p 5433:5432 `
postgres:14.0

# docekr-composeを使って起動する場合の注意
docker-compose -f docker-compose.yml up -d
で起動することができる。
この時、dockerデスクトップ上は、cafelogというコンテナが１つ起動中になっており、
その中に入ると、tomcatとpostges用のコンテナが２つ立ち上がっているというイメージ。

そのため、docker-compose.xml内に２つ分のコンテナ起動のための記述を記載すると思うが、
container-nameを、cafelog以外にしなければいけない


