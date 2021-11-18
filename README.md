# docker-java8-gradle
## 環境変数のロード＆DB初期化
- 注：RDSのデプロイおよび、パラメータストア / Secrets Managerの設定値の登録が前提
```
source mad-ws-load-env.sh
bash mad-ws-rds-init.sh
```

## Javaアプリケーションのビルド
```
./gradlew build
ls -l build/libs/docker-java8-gradle-0.1.0.jar
```

## Javaアプリケーションの実行
```
java -jar build/libs/docker-java8-gradle-0.1.0.jar
```
- `http://localhost:8080/`にアクセス
- `<Ctrl+C>`で終了

## Dockerイメージのビルド
- 注：「Javaアプリケーションのビルド」後に実行
```
docker build -t myapp/docker-java8-gradle .
```

## Dockerイメージを使ったJavaアプリケーションの実行
- 注：「Javaアプリケーションのビルド」後に実行
```
docker run -p 8080:8080 -e DB_NAME -e DB_SECRETS myapp/docker-java8-gradle
```
- `http://localhost:8080/`にアクセス
- `<Ctrl+C>`で終了
