# https://github.com/birdayz/kaf
kaf topic delete debezium-hermes12-action_record
kaf topic create debezium-hermes12-action_record -p 2

kaf topic delete darcy-connect-statuses
kaf topic create darcy-connect-sattuses -p 2 --compact