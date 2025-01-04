* Look at reactive mongo repositories

* ChatService
  * updateLastMessage() - what if:
    1) invoked for message A
    2) chat fetched for message A
    3) invoked for message B
    4) chat fetched for message B
    5) message B saved as new last message
    6) message A saved as new last message
    7) result: message A overwrites newer Message B
	-> add check on timestamp

