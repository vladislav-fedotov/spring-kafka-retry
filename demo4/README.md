# Retry with Consumer Record

## Show List of Topics
```shell
kafka-topics --list  --bootstrap-server localhost:29096
```

## Check Consumer Groups:
```shell
kafka-consumer-groups --bootstrap-server localhost:29096 --group group-id --describe
```

## Produce Some Messages:
```shell
kcat -P -b localhost:29096 -t advice-topic
1
2
3
```

## Produce Some Messages:
```shell
kcat -P -b localhost:29096 -t advice-topic
2
```

## Remove all topics:
```shell
kafka-topics --bootstrap-server localhost:29096 --delete --topic 'advice-.*'
```