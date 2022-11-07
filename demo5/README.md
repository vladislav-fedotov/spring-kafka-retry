# Retry Config with Customizer and TaskScheduler

## Show List of Topics
```shell
kafka-topics --list  --bootstrap-server localhost:29097
```

## Check Consumer Groups:
```shell
kafka-consumer-groups --bootstrap-server localhost:29097 --group group-id --describe
```

## Produce Some Messages:
```shell
kcat -P -b localhost:29097 -t advice-topic
1
2
3
```

## Produce Some Message:
```shell
kcat -P -b localhost:29097 -t advice-topic
2
```

## Produce Some Message to Specific Partition:
```shell
kcat -P -b localhost:29097 -t advice-topic -p 0
2
```

## Remove all topics:
```shell
kafka-topics --bootstrap-server localhost:29097 --delete --topic 'advice-.*'
```