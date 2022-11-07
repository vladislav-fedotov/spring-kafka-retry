
# Retry on Custom Exception

## Show List of Topics
```shell
kafka-topics --list  --bootstrap-server localhost:29094
```

## Check Consumer Groups:
```shell
kafka-consumer-groups --bootstrap-server localhost:29094 --group group-id --describe
```

## Produce Some Messages:
```shell
kcat -P -b localhost:29094 -t advice-topic
1
2
3
```

## Remove all topics:
```shell
kafka-topics --bootstrap-server localhost:29094 --delete --topic 'advice-.*'
```