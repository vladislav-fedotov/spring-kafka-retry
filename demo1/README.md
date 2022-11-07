## Show List of Topics
```shell
kafka-topics --list  --bootstrap-server localhost:29093
```

## Check Consumer Groups:
```shell
kafka-consumer-groups --bootstrap-server localhost:29093 --group group-id --describe
```


## Produce Some Messages (to specific partition):
```shell
kcat -P -b localhost:29093 -t advice-topic
1
2
3
```

## Remove all topics:
```shell
kafka-topics --bootstrap-server localhost:29093 --delete --topic 'advice-.*'
```