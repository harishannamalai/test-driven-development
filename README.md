# Library Management Application.

This application is an implementation of a library management application.

## Expected Behaviour of the Library Management Application.
The expected behaviour of the application, is described by the various user stories that are outlined as below: 

1. User can view books in library
Scenario​: As a User
I want to see the books present in the library
So that I can chose which book to borrow
Given​, there are no books in the library
When​, I view the books in the library
Then​, I see an empty library
Given​, there are books in the library
When​, I view the books in the library
Then​, I see the list of books in the library

2. User can borrow a book from the library
Given​, there are books in the library
When​, I choose a book to add to my borrowed list
Then​, the book is added to my borrowed list
And​, the book is removed from the library
Note: Each User has a borrowing limit of 2 books at any point of time

3. User can borrow a copy of a book from the library
Given​, there are more than one copy of a book in the library
When​, I choose a book to add to my borrowed list
Then​, one copy of the book is added to my borrowed list
And​, the library has at least one copy of the book left
Given​, there is only one copy of a book in the library
When​, I choose a book to add to my borrowed list
Then​, one copy of the book is added to my borrowed list
And​, the book is removed from the library
Note: Only 1 copy of a book can be borrowed by a User at any point of time

4. User can return books to the library
Given​, I have 2 books in my borrowed list
When​, I return one book to the library
Then​, the book is removed from my borrowed list
And​, the library reflects the updated stock of the book
Given​, I have 2 books in my borrowed list
When​, I return both books to the library
Then​, my borrowed list is empty
And​, the library reflects the updated stock of the books

## Technical Details of the Application.
The application is built using the following technologies.
1. Spring Boot - The Self hosting application container.
1. a.Spring Web, Spring Data JPA. 
2. H2 - In Memory Database (currently Data is not persisted over restart).
3. MockMVC - Testing framework for testing the REST APIs.

## Details of the API.
1. `GET /books`  - Lists all the unique books in the Library.
2. `POST /books` - Create a new book entry or add one more copy to Library Catalog.
3. `GET /users` - Lists all the users in the Library System.
4. `POST /users` - Create a new user / return user information if user already exist.
5. `GET /catalog/books` - List all `AVAILABLE` books in the Catalog.
5. a `GET /catalog/books?bookId` - List all `AVAILABLE` books in the Catalog with the given Book Id.
6. `GET /catalog/user/{userId}` - List all books `BORROWED` by the user.
7. `POST /catalog/user/{userId}` - Perform the user action that is `BORROW` or `RETURN` for the given userId.

