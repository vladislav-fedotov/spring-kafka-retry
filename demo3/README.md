# Retry on Custom Nested Exception 

## Show List of Topics
```shell
kafka-topics --list  --bootstrap-server localhost:29095
```

## Check Consumer Groups:
```shell
kafka-consumer-groups --bootstrap-server localhost:29095 --group group-id --describe
```

## Produce Some Messages:
```shell
kcat -P -b localhost:29095 -t advice-topic
1
2
3
```

## Remove all topics:
```shell
kafka-topics --bootstrap-server localhost:29095 --delete --topic 'advice-.*'
```