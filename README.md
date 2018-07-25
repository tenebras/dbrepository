# dbrepository

## Basic usage

```sql
create database dbrepository;

create table comment(
  id serial primary key,
  value text,
  author_id varchar(128),
  at timestamp with time zone
);

```

```kotlin
data class Comment(
    val value: String, 
    val authorId: String, 
    val at: ZonedDateTime, 
    val id: Int? = null
)

class Comments(connection: Connection): Repository<Comment>(connection, DefaultEntityReader(), TableInfo(Comment::id)) {
    
    fun first() = 
        query { "select * from $table limit 1 " }
    
    fun findByAuthorId(authrorId: String) = 
        queryAll { "select * from $table where author_id=${bind(authorId)}" }
}

fun main(args: Array<String>) {

    val comments = Comments(connection())
    
    comments.add(Comment(
        value = "New comment",
        authorId = "tenebras"
        at = ZonedDateTime.now()
    ))
    
    val firstComment = comments.first()
    val commentsByAuthor = comments.findByAuthorId("tenebras")
   
   println( commentsByAuthor.toJson {
        mapOf("link" to "http://example.com/comments/${it.id}")
   })
}

fun connection() = DriverManager.getConnection("jdbc:postgresql://localhost/dbrepository")
```

Notes:
 - Comment constructor parameter `id` was omitted to force insert. Same result could be achieved with `id = null`
 - `Repository` constructor required `TableInfo`. It could be constructed from tableName, entity class and reference to primary key field. By specifying only primary key field (as in this example) it would use same class as entity and it's lower cased name as table name. 
 - By using `$table` variable inside sql you can easily move some boilerplate queries to base class 

## Provide column type for faster or overridden serialization
Note: This example was written only for demonstration. Please don't do shit like this in your projects :)
```sql
alter table comment add column replies jsonb default null;
```

```kotlin

data class Comment(
    ...
    val replies: Array<Comment>?
    ...
);

class Comments(connection: Connection): Repository<Comment>(connection, DefaultEntityReader(), TableInfo(Comment::id)) {
    ...
    
    init {
        columnType(
            Comment::replies to ColumnType::JSONB
        )
    }
    
    ...
}

fun main(args: Array<String>){
    ...
    comments.add( Comment(
        value = "New comment",
        authorId = "tenebras"
        at = ZonedDateTime.now(),
        replies = arrayOf(
            Comment(
                value = "First reply",
                authorId = "tenebras"
                at = ZonedDateTime.now()
            ),
            Comment(
                value = "Second reply",
                authorId = "tenebras"
                at = ZonedDateTime.now()
            )
        )
    ))
    ...
}
```
Notes: 
- columnType map would be used for both insert(add) and select 
## Useful extensions

### Array.toJson

Convert Array to JSON with possibility to modify each element or exclude some fields from element. 
Extending function will receive typed instance of element so you can use any of it's properties. 
Following example will add `link` field to each **`Comment`** object and exclude `at` and `authorId`:

```kotlin
val comments = arrayOf(
    Comment(1, "value1", "authorId1", ZonedDateTime.now()),
    Comment(2, "value2", "authorId2", ZonedDateTime.now())
)

comments.toJson( exclude = listOf("at", "authorId")) {
  mapOf("link" to "http://example.com/comments/${it.id}")
}
```

Result is:
```json
[
  {
    "id": 1,
    "value": "value1",
    "link": "http://example.com/comments/1"
  },
  {
    "id": 2,
    "value": "value2",
    "link": "http://example.com/comments/2"
  }
]
```

### and many more...