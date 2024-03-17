--liquibase formatted sql
--changeset adrian:1

CREATE VIEW most_popular_books
AS
SELECT DENSE_RANK() OVER (ORDER BY COUNT(borrowings.copy_id) DESC) AS position,
	COUNT(borrowings.copy_id) AS number_of_borrowed_copies, title
FROM borrowings
	INNER JOIN copies
	ON borrowings.copy_id = copies.id
		INNER JOIN editions
		ON copies.edition_id = editions.id
			RIGHT JOIN books
			ON editions.book_id = books.id
WHERE picked_up_on >= NOW() - interval 1 year
GROUP BY title
ORDER BY number_of_borrowed_copies DESC, title ASC;


CREATE VIEW most_popular_authors
AS
SELECT DENSE_RANK() OVER (ORDER BY COUNT(borrowings.copy_id) DESC) AS position,
	COUNT(borrowings.copy_id) AS number_of_borrowed_copies, authors.name
FROM borrowings
	INNER JOIN copies
	ON borrowings.copy_id = copies.id
		INNER JOIN editions
		ON copies.edition_id = editions.id
			RIGHT JOIN books
			ON editions.book_id = books.id
				INNER JOIN book_author
                ON books.id = book_author.book_id
					INNER JOIN authors
                    ON book_author.author_id = authors.id
WHERE picked_up_on >= NOW() - interval 1 year
GROUP BY authors.name
ORDER BY number_of_borrowed_copies DESC, authors.name ASC;


CREATE VIEW most_popular_genres
AS
SELECT DENSE_RANK() OVER (ORDER BY COUNT(borrowings.copy_id) DESC) AS position,
	COUNT(borrowings.copy_id) AS number_of_borrowed_copies, genres.name
FROM borrowings
	INNER JOIN copies
	ON borrowings.copy_id = copies.id
		INNER JOIN editions
		ON copies.edition_id = editions.id
			RIGHT JOIN books
			ON editions.book_id = books.id
				INNER JOIN book_genre
                ON books.id = book_genre.book_id
					INNER JOIN genres
                    ON book_genre.genre_id = genres.id
WHERE picked_up_on >= NOW() - interval 1 year
GROUP BY genres.name
ORDER BY number_of_borrowed_copies DESC, genres.name ASC;