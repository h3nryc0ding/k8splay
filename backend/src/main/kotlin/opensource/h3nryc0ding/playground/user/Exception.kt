package opensource.h3nryc0ding.playground.user

class UserAlreadyExistsException(username: String) : RuntimeException("User with username $username already exists")
