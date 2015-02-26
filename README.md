# multithreaded-bank-client-server
Implementation of a multithreaded bank account server which provides debit and credit facility. Clients are simulated using 5 threads in client side code.

The server creates new thread for each request. There is a single copy of account data associated with server. Thus, every thread accesses the account data in serialized manner i.e. the account data is implemented to provide synchronization among requests.

To simulate account access requests, there are 5 threads in the client side code, all of them get created at almost same time and access same account.
