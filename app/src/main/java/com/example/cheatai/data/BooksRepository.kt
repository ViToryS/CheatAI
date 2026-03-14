package com.example.cheatai.data


interface BooksRepository {
    fun getBooks(): List<Book>
}

class StaticBooksRepository : BooksRepository {
    override fun getBooks(): List<Book> = listOf(
        Book(
            id = "1",
            title = "Гарри Поттер и филосовский камень",
            author = "Джоан Роулинг",
            description = "Роман о дьяволе, который посещает Москву...",
            coverUrl = "drawable://book_cover_1",
            pages = 480
        ),
        Book(
            id = "2",
            title = "Гарри Поттер и филосовский камень",
            author = "Джоан Роулинг",
            description = "Роман-антиутопия о тоталитарном обществе",
            coverUrl = "drawable://book_cover_1",
            pages = 328
        ),
        Book(
            id = "3",
            title = "Гарри Поттер и филосовский камень",
            author = "Джоан Роулинг",
            description = "Роман-антиутопия о тоталитарном обществе",
            coverUrl = "drawable://book_cover_1",
            pages = 328
        ),
        Book(
            id = "4",
            title = "Гарри Поттер и филосовский камень",
            author = "Джоан Роулинг",
            description = "Роман-антиутопия о тоталитарном обществе",
            coverUrl = "drawable://book_cover_1",
            pages = 328
        ),
    )
}
