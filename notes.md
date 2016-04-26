EventSourcing (our take on ES!) ... some notes on architecture ...

- An aggregate is a representations of an entity, or group of tightly interrelated entities.
- Aggregates may publish events (not dispatch commands)
- dispatcher of a Command receives a CommandResult<T>
- Event Publishing does NOT generate an EventResult<T>
- Communication directly between Aggregates IS ILLEGAL!

AccountAggregate
  Account <- aggregate root (ie. has a repository)
   - credits : List<Credits>
   - debits : List<Debits>


# Tests

1. Create account
    - set balance to 0
    - get account number in result of command

    - command is executed
        - uuid will be generated at the command level
        - event is then built and published

2. View account
    - see account details
