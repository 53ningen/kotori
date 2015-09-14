Kotori
====

KotoriはJava製の掲示板アプリケーションです(・８・)

## Install

js関連のプラグインのインストールにはbowerが必要です。
bowerをインストールした後に下記コマンドを実行します。

```
$ bower install
```

## Setup

KotoriはDBにMySQLとRedisを使用します。
予め起動しておいてください。

MySQL/Redisへの接続はプロパティファイルによって指定します。
`src/main/resources` 以下に `dbsettings.properties` という名前で設定ファイルを置いてください。
設定ファイルは例えば以下のように記述します。

```dbsettings.properties
mysql_hostname=jdbc:mysql://localhost:3306/dbname
mysql_username=root
mysql_password=password
redis_hostname=localhost
```

`mysql_hostname`は必ずデータベース名を含めてください。
`redis_hostname`は省略が可能です。省略した場合は`localhost`が自動的に設定されます。

## Build

`gradlew`コマンドを使うことができます。
初めに下記コマンドを実行します。

```
$ ./gradlew
```

`gradlew`コマンドは以下のようにタスク名を書くことでタスクを実行できます。

```
$ ./gradlew compileJava	// コンパイル
$ ./gradlew test		// テスト実行
```

## Run

アプリケーションの起動は`gradlew`コマンドを使用して、`run`タスクを実行します。

```
$ ./gradlew run
```

デフォルトのポート番号は`4567`になります。ポート番号を指定したい場合は`-Pargs`オプションを使用してください。

```
$ ./gradlew -Pargs="9000"
```