package sample

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/books")
class BookController {

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    def getBook(@PathVariable("id") Long id) {
        def book = Book.get(id) // id指定でエンティティを取得する
        if (!book) {
            return new ResponseEntity(HttpStatus.NOT_FOUND)
        }
        return [title: book.title, author: book.author] // マップを返すだけでJSONに変換される
    }

    @RequestMapping(method = RequestMethod.GET)
    def getBooks() {
        // list()で全レコードを取得して、それぞれのエンティティのtitleとauthorを抽出して返す
        Book.list().collect { [title: it.title, author: it.author] }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    def postBooks(@RequestBody Book book) {
        if (!book.save(flush: true)) { // データベースに保存する
            return new ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return new ResponseEntity(HttpStatus.CREATED)
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    def deleteBooks(@PathVariable("id") Long id) {
        def book = Book.get(id) // id指定でエンティティを取得する
        if (!book) {
            return new ResponseEntity(HttpStatus.NOT_FOUND)
        }
        book.delete() // データベースから削除する
    }

    // テスト用
    @RequestMapping("test")
    def test() {
        println ">" * 50

        Book.list()*.delete()

        def book = new Book(title: "プログラミングGROOVY", author: "中野 靖治、他")
        book.save() // データベースへの保存

        assert Book.count() == 1 // レコード数の取得

        def books = Book.where { title == "プログラミングGROOVY" }.list() // データベースからの取得
        assert books*.title == ["プログラミングGROOVY"]

        def book1 = new Book(title: "ダミータイトル", author: "")
        book1.save()             // 自動的にバリデーションが実行される
        assert book1.hasErrors() // 空文字禁止制約のためエラーが検出される

        def book2 = new Book(title: "ダミータイトル", author: "x" * 101) // author＝xを101個つなげた文字列
        book2.save()
        assert book2.hasErrors() // 100文字超過のためエラーが検出される

        println "PASSED!!"
        println "<" * 50
    }
}
