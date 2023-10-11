* UserController: 
  * get blocked users list
  * unblock user

* Chat
  * Should ChatType be in the body of Chat? 
  * move mutedBy, participants and viewedBy to one class, so document in the database is smaller? (no user id duplicates)

* ChatService
  * updateLastMessage() - what if:
    1) invoked for message A
    2) chat fetched for message A
    3) invoked for message B
    4) chat fetched for message B
    5) message B saved as new last message
    6) message A saved as new last message
    7) result: message A overwrites newer Message B

* ChatController
  * createChat() - what should be put as response, chat view, chat id, chatDto?
     

* Message
  * Should MessageContent be in the body of Message? 
